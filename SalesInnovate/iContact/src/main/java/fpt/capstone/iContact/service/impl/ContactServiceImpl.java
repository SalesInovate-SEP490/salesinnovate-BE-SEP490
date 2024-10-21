package fpt.capstone.iContact.service.impl;

import fpt.capstone.iContact.dto.Convert;
import fpt.capstone.iContact.dto.request.AddressInformationDTO;
import fpt.capstone.iContact.dto.request.ContactDTO;
import fpt.capstone.iContact.dto.response.ContactResponse;
import fpt.capstone.iContact.dto.response.PageResponse;
import fpt.capstone.iContact.exception.ResourceNotFoundException;
import fpt.capstone.iContact.model.Contact;
import fpt.capstone.iContact.repository.*;
import fpt.capstone.iContact.repository.specification.ContactSpecificationsBuilder;
import fpt.capstone.iContact.service.ContactClientService;
import fpt.capstone.iContact.service.ContactService;

import fpt.capstone.iContact.service.ExcelUploadService;
import fpt.capstone.proto.lead.LeadDtoProto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fpt.capstone.iContact.util.AppConst.SEARCH_SPEC_OPERATOR;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactClientService contactClientService;
    private final ContactRepository contactRepository ;
    private final CoOppRelationRepository coOppRelationRepository ;
    private final Convert convert;
    private final SalutionRepository salutionRepository;
    private final AddressInformationRepository addressInformationRepository;
    private final SearchRepository searchRepository;
    private final ExcelUploadService excelUploadService;
    @Override
    public Long createFromLead(long leadId, long accountId,
    long salution,String firstName,String middleName,String lastName) {
        LeadDtoProto proto = contactClientService.getLead(leadId);
        try {
            // add new contact
            Contact contact = Contact.builder()
                    .accountId(accountId)
                    .userId("1")
                    .firstName(firstName)
                    .lastName(lastName)
                    .middleName(middleName)
                    .title(proto.getTitle())
                    .email(proto.getEmail())
                    .phone(proto.getPhone())
                    .contactSalution(salutionRepository.findById(proto.getSalution().getLeadSalutionId()).orElse(null))
                    .addressInformation(addressInformationRepository
                            .findById(proto.getAddressInfor().getAddressInformationId()).orElse(null))
                    .createdBy("1")
                    .editBy("1")
                    .createDate(LocalDateTime.now())
                    .editDate(LocalDateTime.now())
                    .isDeleted(0)
                    .build();
            contactRepository.save(contact);

            log.info("Contact convert success!!");
            return contact.getContactId();
        }catch (Exception e){
            log.info(e.getMessage(),e.getCause());
        }
        return null ;
    }

    @Override
    public Long existingFromLead(long contactId, long accountId) {
        //check Account relation
        Contact contact = getContactById(contactId);
        if(contact != null && contact.getIsDeleted()==0){
            return contact.getContactId() ;
        }
        return null;
    }

    @Override
    public PageResponse<?> getAllContactsWithSortByDefault(int pageNo, int pageSize) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Specification<Contact> spec = new Specification<Contact>() {
            @Override
            public Predicate toPredicate(Root<Contact> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.notEqual(root.get("isDeleted"), 1);
            }
        };

        Page<Contact> leads = contactRepository.findAll(spec, pageable);
        return convert.convertToPageResponse(leads, pageable);
    }

    @Override
    public PageResponse<?> getAllContactByAccount(int pageNo, int pageSize, long id) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Specification<Contact> spec = new Specification<Contact>() {
            @Override
            public Predicate toPredicate(Root<Contact> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.notEqual(root.get("isDelete"), 1));
                predicates.add(criteriaBuilder.equal(root.get("accountId"), id));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<Contact> contacts = contactRepository.findAll(spec, pageable);
        return convert.convertToPageResponse(contacts, pageable);
    }

    @Override
    public ContactResponse getContactDetail(long id) {
        Contact contact = getContactById(id);
        return convert.entityToContactResponse(contact);
    }

    @Override
    public boolean deleteContact(long id) {
        Contact contact = getContactById(id);
        if(contact != null){
            contact.setIsDeleted(1);
            return true;
        }
        return false;
    }

    @Override
    public Long createContact(ContactDTO contactDTO) {
        Contact contact = convert.DTOToEntity(contactDTO);
        if(contact!=null){
            contact.setCreatedBy("1");
            contact.setEditBy("1");
            contact.setCreateDate(LocalDateTime.now());
            contact.setEditDate(LocalDateTime.now());
            contact.setIsDeleted(0);
            contactRepository.save(contact);
            return contact.getContactId();
        }
        return null;
    }

    @Override
    @Transactional
    public boolean patchContact(ContactDTO leadDTOOld, long id) {

        Map<String, Object> patchMap = getPatchData(leadDTOOld);
        if (patchMap.isEmpty()) {
            return true;
        }

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find contact with id: " + id));

        if (contact != null) {
            for (Map.Entry<String, Object> entry : patchMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Field fieldDTO = ReflectionUtils.findField(ContactDTO.class, key);

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
                    case "contactSalutionId":
                        contact.setContactSalution(salutionRepository.findById((Long) value).orElse(null));
                        break;
                    case "addressInformation":
                        AddressInformationDTO dto = (AddressInformationDTO) value;
                        if(!Objects.equals(dto.getStreet(),contact.getAddressInformation().getStreet()))
                            contact.getAddressInformation().setStreet(dto.getStreet());
                        if(!Objects.equals(dto.getCity(),contact.getAddressInformation().getCity()))
                            contact.getAddressInformation().setCity(dto.getCity());
                        if(!Objects.equals(dto.getProvince(),contact.getAddressInformation().getProvince()))
                            contact.getAddressInformation().setProvince(dto.getProvince());
                        if(!Objects.equals(dto.getPostalCode(),contact.getAddressInformation().getPostalCode()))
                            contact.getAddressInformation().setPostalCode(dto.getPostalCode());
                        if(!Objects.equals(dto.getCountry(),contact.getAddressInformation().getCountry()))
                            contact.getAddressInformation().setCountry(dto.getCountry());
                        break;
                    default:
                        if (fieldDTO.getType().isAssignableFrom(value.getClass())) {
                            Field field = ReflectionUtils.findField(Contact.class, fieldDTO.getName());
                            assert field != null;
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, contact, value);
                        } else {
                            return false;
                        }
                }
            }
            contact.setEditDate(LocalDateTime.now());
            contactRepository.save(contact);
            return true;
        }

        return false;
    }

    @Override
    public PageResponse<?> filterContact(Pageable pageable, String[] search) {
        ContactSpecificationsBuilder builder = new ContactSpecificationsBuilder();

        if (search != null) {
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String l : search) {
                Matcher matcher = pattern.matcher(l);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2),matcher.group(3));
                }
            }

            Page<Contact> leadPage = searchRepository.searchUserByCriteriaWithJoin(builder.params, pageable);
            return convert.convertToPageResponse(leadPage, pageable);
        }
        return getAllContactsWithSortByDefault(pageable.getPageNumber(),pageable.getPageSize());
    }

    @Override
    public ByteArrayInputStream getExportFileData() throws IOException {
        List<Contact> accounts = contactRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = excelUploadService.dataToExecel(accounts);
        return byteArrayInputStream;
    }

    private Contact getContactById(long contactId) {
        Contact contact= contactRepository.findById(
                contactId).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        return  (contact.getIsDeleted() != null && contact.getIsDeleted() == 0)?contact:null;
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
