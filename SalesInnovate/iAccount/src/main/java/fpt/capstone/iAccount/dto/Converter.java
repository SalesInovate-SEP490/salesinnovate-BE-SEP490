package fpt.capstone.iAccount.dto;

import fpt.capstone.iAccount.dto.request.*;
import fpt.capstone.iAccount.dto.response.AccountResponse;
import fpt.capstone.iAccount.dto.response.PageResponse;
import fpt.capstone.iAccount.model.Account;
import fpt.capstone.iAccount.model.AccountType;
import fpt.capstone.iAccount.model.AddressInformation;
import fpt.capstone.iAccount.model.Industry;
import fpt.capstone.iAccount.repository.AccountRepository;
import fpt.capstone.iAccount.repository.AccountTypeRepository;
import fpt.capstone.iAccount.repository.IndustryRepository;
import fpt.capstone.iAccount.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Converter {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountTypeRepository accountTypeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IndustryRepository industryRepository;


//    public AccountDTO entityToAccountDTO(Account account) {
//        return AccountDTO.builder()
//                .accountName(account.getAccountName())
//                .userId(account.getUserId())
//                .parentAccountId(account.getParentAccountId())
//                .description(account.getDescription())
//                .phone(account.getPhone())
//                .website(account.getWebsite())
//                .billingInformation(account.getBillingInformation())
//                .shippingInformationId(account.getShippingInformationId())
//                .editBy(account.getEditBy())
//                .roleId(account.getRole().getRoleId())
//                .accountTypeId(account.getAccountType().getAccountId())
//                .industryId(account.getIndustryId())
//                .build();
//    }
    public Account DTOtoEntity(AccountDTO accountDTO){
        return Account.builder()
                .accountId(accountDTO.getAccountId())
                .accountName(accountDTO.getAccountName())
                .userId(accountDTO.getUserId())
                .parentAccountId(accountDTO.getParentAccountId())
                .description(accountDTO.getDescription())
                .phone(accountDTO.getPhone())
                .website(accountDTO.getWebsite())
                .noEmployee(accountDTO.getNoEmployee())
                .billingInformation(DTOToAddressInformation(accountDTO.getBillingInformation()))
                .shippingInformation(DTOToAddressInformation(accountDTO.getShippingInformation()))
                .createdBy(accountDTO.getCreatedBy())
                .editBy(accountDTO.getEditBy())
                .accountType(accountTypeRepository
                        .findById(accountDTO.getAccountTypeId()).orElse(null))
                .industry(industryRepository
                        .findById(accountDTO.getIndustryId()).orElse(null))
                .build();

    }

    public AccountResponse entityToAccountResponse(Account account){
        return AccountResponse.builder()
                .accountId((account.getAccountId()))
                .accountName(account.getAccountName())
                .userId(account.getUserId())
                .parentAccountId(account.getParentAccountId())
                .description(account.getDescription())
                .phone(account.getPhone())
                .website(account.getWebsite())
                .accountType(entityToAccountTypeDTO(account.getAccountType()))
                .industry(entityToIndustryDTO(account.getIndustry()))
                .billingInformation(account.getBillingInformation())
                .shippingInformation(account.getShippingInformation())
                .editBy(account.getEditBy())
                .build();
    }

    public PageResponse<?> convertToPageResponse(Page<Account> account, Pageable pageable) {
        List<AccountResponse> response = account.stream()
                .map(this::entityToAccountResponse).toList();
        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(account.getTotalPages())
                .items(response)
                .build();
    }


    public AddressInformation DTOToAddressInformation(AddressInformationDTO addressInformation) {
        if (addressInformation == null) return null ;
        return AddressInformation.builder()
                .addressInformationId(addressInformation.getAddressInformationId())
                .street(addressInformation.getStreet())
                .city(addressInformation.getCity())
                .province(addressInformation.getProvince())
                .postalCode(addressInformation.getPostalCode())
                .country(addressInformation.getCountry())
                .build();
    }
    public AccountTypeDTO entityToAccountTypeDTO(AccountType type){
        if(type==null) return null ;
        return AccountTypeDTO.builder()
                .accountTypeName(type.getAccountTypeName())
                .accountTypeName(type.getAccountTypeName())
                .build();
    }
    public IndustryDTO entityToIndustryDTO (Industry industry){
        if(industry==null) return null ;
        return IndustryDTO.builder()
                .industryId(industry.getIndustryId())
                .industryStatusName(industry.getIndustryStatusName())
                .build();
    }
    public Account convertFileImportToAccount(AccountImportDTO accountImportDTO,
                                              AddressInformation AddressInformation, String userId){
        Industry industry =
                industryRepository.findByIndustryStatusName(accountImportDTO.getIndustryStatusName());
        AccountType accountType =
                accountTypeRepository.findByAccountTypeName(accountImportDTO.getAccountTypeName());

        return Account.builder()
                .userId(userId)
                .industry(industry)
                .accountType(accountType)
                .accountName(accountImportDTO.getAccountName())
                .parentAccountId(accountImportDTO.getParentAccountId())
                .description(accountImportDTO.getDescription())
                .phone(accountImportDTO.getPhone())
                .website(accountImportDTO.getWebsite())
                .noEmployee(accountImportDTO.getNoEmployee())
                .createDate(LocalDateTime.now())
                .billingInformation(AddressInformation)
                .shippingInformation(AddressInformation)
                .isDeleted(0)
                .build();
    }
    public AddressInformation DataToAddressInformation(String street, String city,
                                                       String provice, String postalCode,
                                                       String country) {
        return AddressInformation.builder()
                .street(street)
                .city(city)
                .province(provice)
                .postalCode(postalCode)
                .country(country)
                .build();
    }
    public AccountExportDTO AccountEntityToAccountExportDTO(Account account) {
        return AccountExportDTO.builder()
                .accountId(account.getAccountId())
                .accountName(Optional.ofNullable(account.getAccountName()).orElse(null))
                .parentAccountId(Optional.ofNullable(account.getParentAccountId()).orElse(null))
                .industryId(Optional.ofNullable(account.getIndustry()).map(Industry::getIndustryId).orElse(null))
                .accountTypeId(Optional.ofNullable(account.getAccountType()).map(AccountType::getAccountTypeId).orElse(null))
                .addressInformationId(Optional.ofNullable(account.getShippingInformation()).map(AddressInformation::getAddressInformationId).orElse(null))
                .phone(Optional.ofNullable(account.getPhone()).orElse(null))
                .website(Optional.ofNullable(account.getWebsite()).orElse(null))
                .description(Optional.ofNullable(account.getDescription()).orElse(null))
                .createdBy(Optional.ofNullable(account.getCreatedBy()).orElse(null))
                .createDate(Optional.ofNullable(account.getCreateDate()).orElse(null))
                .build();
    }
    public List<AccountExportDTO> AccountEntityListToLeadExportDTOList(List<Account> accountList) {
        return accountList.stream()
                .map(this::AccountEntityToAccountExportDTO)
                .collect(Collectors.toList());
    }
}
