package fpt.capstone.iAccount.service.impl;

import fpt.capstone.iAccount.dto.Converter;
import fpt.capstone.iAccount.dto.request.AccountDTO;
import fpt.capstone.iAccount.dto.request.AccountTypeDTO;
import fpt.capstone.iAccount.dto.request.AddressInformationDTO;
import fpt.capstone.iAccount.dto.response.AccountResponse;
import fpt.capstone.iAccount.dto.response.PageResponse;
import fpt.capstone.iAccount.dto.response.ResponseData;
import fpt.capstone.iAccount.dto.response.ResponseError;
import fpt.capstone.iAccount.model.Account;
import fpt.capstone.iAccount.model.AccountType;
import fpt.capstone.iAccount.model.AddressInformation;
import fpt.capstone.iAccount.repository.*;
import fpt.capstone.iAccount.repository.specification.AccountSpecificationsBuilder;
import fpt.capstone.iAccount.service.AccountClientService;
import fpt.capstone.iAccount.service.AccountService;
import fpt.capstone.proto.lead.LeadDtoProto;
import fpt.capstone.iAccount.service.ExcelUploadService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fpt.capstone.iAccount.util.AppConst.SEARCH_SPEC_OPERATOR;

@Slf4j
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    @Autowired
    private final AccountRepository accountRepository;
    private final Converter converter;
    private final AccountClientService accountClientService ;
    private final IndustryRepository industryRepository;
    private final AddressInformationRepository addressInformationRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final SearchRepository searchRepository ;
    private final ExcelUploadService excelUploadService;
    @Override
    public Long createAccount(AccountDTO accountDTO) {
        try{
            Account account = converter.DTOtoEntity(accountDTO);
            account.setCreateDate(LocalDateTime.now());
            account.setEditDate(LocalDateTime.now());
            account.setIsDeleted(0);
            account.setCreatedBy("1");
            account.setEditBy("1");
            account.setUserId("1");
            if(account.getBillingInformation().getAddressInformationId()==null){
                AddressInformation addressInformation = converter.DTOToAddressInformation(accountDTO.getBillingInformation());
                addressInformationRepository.save(addressInformation);
                account.setBillingInformation(addressInformation);
            }
            if(account.getShippingInformation().getAddressInformationId()==null){
                AddressInformation addressInformation = converter.DTOToAddressInformation(accountDTO.getShippingInformation());
                addressInformationRepository.save(addressInformation);
                account.setShippingInformation(addressInformation);
            }
            accountRepository.save(account);
            return account.getAccountId();
        }
        catch (Exception e){
            log.info(e.getMessage(),e.getCause());
            return null ;
        }
    }

    @Override
    public boolean deleteAccount(Long id) {
        try{
            Optional<Account> account = accountRepository.
                    findById(id);
            if(account.isPresent()){
                Account accountExisted = account.get();
                accountExisted.setIsDeleted(1);
                accountExisted.setEditDate(LocalDateTime.now());
                accountRepository.save(accountExisted);
                return true ;
            }
            log.info("account not existed");
            return false ;
        }
        catch (Exception e){
            e.printStackTrace();
            return false ;        }
    }

    @Override
    public boolean patchListAccount(Long[] id, AccountDTO accountDTO) {
        if (id != null){
            List<Account> accountsList = new ArrayList<>();
            try {
                for (long i : id){
                    Account account = accountRepository.findById(i).orElse(null);
                    if(account != null) accountsList.add(account);
                }
                boolean checked ;
                for (Account l : accountsList) {
                    checked =patchAccount(accountDTO, l.getAccountId());
                    if(!checked) {
                        return false ;
                    }
                }
                return true ;
            }catch (Exception e){
                return false ;
            }
        }
        return false ;
    }

    @Override
    public PageResponse<?> filterAccount(Pageable pageable, String[] search) {
        AccountSpecificationsBuilder builder = new AccountSpecificationsBuilder();

        if (search != null) {
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String l : search) {
                Matcher matcher = pattern.matcher(l);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2),matcher.group(3));
                }
            }

            Page<Account> leadPage = searchRepository.searchUserByCriteriaWithJoin(builder.params, pageable);
            return converter.convertToPageResponse(leadPage, pageable);
        }
        return getListAccount(pageable.getPageNumber(),pageable.getPageSize());
    }

    @Override
    public List<AccountType> getListType() {
        return accountTypeRepository.findAll();
    }

    @Override
    public void importFileAccount(MultipartFile file, String userId) {
        if (excelUploadService.isValidExcelFile(file)) {
            try {
                List<Account> accounts =
                        excelUploadService.getLeadDataFromExcel(file.getInputStream(), userId);
                this.accountRepository.saveAll(accounts);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

    @Override
    public ByteArrayInputStream getExportFileData() throws IOException {
        List<Account> accounts = accountRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = excelUploadService.dataToExecel(accounts);
        return byteArrayInputStream;
    }

    @Override
    public PageResponse<?> getListAccount( int pageNo, int pageSize) {
        try{
            int page = 0;
            if (pageNo > 0) {
                page = pageNo - 1;
            }

            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));

            Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

            Specification<Account> spec = new Specification<Account>() {
                @Override
                public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.notEqual(root.get("isDeleted"), 1);
                }
            };

            Page<Account> accounts = accountRepository.findAll(spec,pageable);
            return converter.convertToPageResponse(accounts, pageable);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AccountResponse getDetailAccount(Long accountId) {
        try{
            Account account = accountRepository.findById(accountId).orElse(null);
            if(account != null && account.getIsDeleted()==0){
                return converter.entityToAccountResponse(account);
            }
            log.info("Not exist account in database");
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long convertNewAccount(long leadId, String accountName) {
        LeadDtoProto proto = accountClientService.getLead(leadId);
        try {
            Account account = Account.builder()
                    .accountName(accountName)
                    .userId("1")
                    .phone(proto.getPhone())
                    .industry(industryRepository.findById(proto.getIndustry().getIndustryId()).orElse(null) )
                    .noEmployee(proto.getNoEmployee())
                    .website(proto.getWebsite())
                    .shippingInformation(addressInformationRepository
                            .findById(proto.getAddressInfor().getAddressInformationId()).orElse(null) )
                    .isDeleted(0)
                    .createdBy("1")
                    .createDate(LocalDateTime.now())
                    .editBy("1")
                    .build();
            accountRepository.save(account);
            return account.getAccountId() ;
        }catch (Exception e){
            log.info(e.getMessage(),e.getCause());
            throw new RuntimeException();
        }
    }

    @Override
    public boolean patchAccount(AccountDTO accountDTO, long id) {

        Map<String, Object> patchMap = getPatchData(accountDTO);
        if (patchMap.isEmpty()) {
            return true;
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find account with id: " + id));

        if (account != null) {
            for (Map.Entry<String, Object> entry : patchMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Field fieldDTO = ReflectionUtils.findField(AccountDTO.class, key);

                if (fieldDTO == null) {
                    continue;
                }

                fieldDTO.setAccessible(true);
                Class<?> type = fieldDTO.getType();

                try {
                    if (type == long.class && value instanceof String) {
                        value = Long.parseLong((String) value);
                    } else if (type == Long.class && value instanceof String) {
                        value = Long.valueOf((String) value);
                    }
                } catch (NumberFormatException e) {
                    return false;
                }

                switch (key) {
                    case "industryId":
                        account.setIndustry(industryRepository.findById((Long) value).orElse(null));
                        break;
                    case "accountTypeId":
                        account.setAccountType(accountTypeRepository.findById((Long) value).orElse(null));
                        break;
                    case "billingInformation":
                        AddressInformationDTO dto = (AddressInformationDTO) value;
                        if(!Objects.equals(dto.getStreet(),account.getBillingInformation().getStreet()))
                            account.getBillingInformation().setStreet(dto.getStreet());
                        if(!Objects.equals(dto.getCity(),account.getBillingInformation().getCity()))
                            account.getBillingInformation().setCity(dto.getCity());
                        if(!Objects.equals(dto.getProvince(),account.getBillingInformation().getProvince()))
                            account.getBillingInformation().setProvince(dto.getProvince());
                        if(!Objects.equals(dto.getPostalCode(),account.getBillingInformation().getPostalCode()))
                            account.getBillingInformation().setPostalCode(dto.getPostalCode());
                        if(!Objects.equals(dto.getCountry(),account.getBillingInformation().getCountry()))
                            account.getBillingInformation().setCountry(dto.getCountry());
                        break;
                    case "shippingInformation":
                        AddressInformationDTO dto1 = (AddressInformationDTO) value;
                        if(!Objects.equals(dto1.getStreet(),account.getShippingInformation().getStreet()))
                            account.getShippingInformation().setStreet(dto1.getStreet());
                        if(!Objects.equals(dto1.getCity(),account.getShippingInformation().getCity()))
                            account.getShippingInformation().setCity(dto1.getCity());
                        if(!Objects.equals(dto1.getProvince(),account.getShippingInformation().getProvince()))
                            account.getShippingInformation().setProvince(dto1.getProvince());
                        if(!Objects.equals(dto1.getPostalCode(),account.getShippingInformation().getPostalCode()))
                            account.getShippingInformation().setPostalCode(dto1.getPostalCode());
                        if(!Objects.equals(dto1.getCountry(),account.getShippingInformation().getCountry()))
                            account.getShippingInformation().setCountry(dto1.getCountry());
                        break;
                    default:
                        if (fieldDTO.getType().isAssignableFrom(value.getClass())) {
                            Field field = ReflectionUtils.findField(Account.class, fieldDTO.getName());
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, account, value);
                        } else {
                            return false;
                        }
                }
            }
            account.setEditDate(LocalDateTime.now());
            accountRepository.save(account);
            return true;
        }

        return false;
    }

    private Map<String, Object> getPatchData(Object obj) {
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        Map<String, Object> patchMap = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value != null) {
                    patchMap.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                log.info(e.getMessage());
            }
        }
        return patchMap;
    }
}
