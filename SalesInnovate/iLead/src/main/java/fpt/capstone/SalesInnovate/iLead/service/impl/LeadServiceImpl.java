package fpt.capstone.SalesInnovate.iLead.service.impl;

import fpt.capstone.SalesInnovate.iLead.dto.Converter;
import fpt.capstone.SalesInnovate.iLead.dto.request.*;
import fpt.capstone.SalesInnovate.iLead.dto.response.LeadResponse;
import fpt.capstone.SalesInnovate.iLead.dto.response.PageResponse;
import fpt.capstone.SalesInnovate.iLead.model.*;
import fpt.capstone.SalesInnovate.iLead.repository.*;
import fpt.capstone.SalesInnovate.iLead.repository.specification.LeadSpecificationsBuilder;
import fpt.capstone.SalesInnovate.iLead.service.ExcelUploadService;
import fpt.capstone.SalesInnovate.iLead.service.LeadsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.experimental.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

import static fpt.capstone.SalesInnovate.iLead.util.AppConst.SEARCH_SPEC_OPERATOR;

@Slf4j
@Service
@AllArgsConstructor
public class LeadServiceImpl implements LeadsService {
    private final LeadsRepository leadsRepository;
    private final Converter leadConverter;
    private final LeadStatusRepository leadStatusRepository;
    private final IndustryRepository industryRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final SearchRepository searchRepository;
    private final LeadRatingRepository leadRatingRepository;
    private final LeadSalutionRepository leadSalutionRepository;
    private final AddressInformationRepository addressInformationRepository;
    private final ExcelUploadService excelUploadService;
    public void saveLeadsToDatabase(MultipartFile file, String userId) {
        if (excelUploadService.isValidExcelFile(file)) {
            try {
                List<Leads> leads =
                        excelUploadService.getLeadDataFromExcel(file.getInputStream(), userId);
                this.leadsRepository.saveAll(leads);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }
//    @Override
//    public List<LeadResponse> getAllLeads(){
//        List<Leads> leads = this.leadsRepository.findAll();
//        return leads.stream().map(lead -> this.leadConverter.entityToLeadResponse(lead)).collect(Collectors.toList());
//    }

    @Override
    @Transactional
    public boolean patchLead(LeadDTO leadDTOOld, long id) {

        Map<String, Object> patchMap = getPatchData(leadDTOOld);
        if (patchMap.isEmpty()) {
            return true;
        }

        Leads lead = leadsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Leads with id: " + id));

        if (lead != null) {
            for (Map.Entry<String, Object> entry : patchMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Field fieldDTO = ReflectionUtils.findField(LeadDTO.class, key);

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
                    case "leadSourceId":
                        lead.setLeadSource(leadSourceRepository.findById((Long) value).orElse(null));
                        break;
                    case "industryId":
                        lead.setIndustry(industryRepository.findById((Long) value).orElse(null));
                        break;
                    case "leadStatusID":
                        lead.setLeadStatus(leadStatusRepository.findById((Long) value).orElse(null));
                        break;
                    case "leadRatingId":
                        lead.setLeadRating(leadRatingRepository.findById((Long) value).orElse(null));
                        break;
                    case "addressInformation":
                        AddressInformationDTO dto = (AddressInformationDTO) value;
                        if(!Objects.equals(dto.getStreet(),lead.getAddressInformation().getStreet()))
                            lead.getAddressInformation().setStreet(dto.getStreet());
                        if(!Objects.equals(dto.getCity(),lead.getAddressInformation().getCity()))
                            lead.getAddressInformation().setCity(dto.getCity());
                        if(!Objects.equals(dto.getProvince(),lead.getAddressInformation().getProvince()))
                            lead.getAddressInformation().setProvince(dto.getProvince());
                        if(!Objects.equals(dto.getPostalCode(),lead.getAddressInformation().getPostalCode()))
                            lead.getAddressInformation().setPostalCode(dto.getPostalCode());
                        if(!Objects.equals(dto.getCountry(),lead.getAddressInformation().getCountry()))
                            lead.getAddressInformation().setCountry(dto.getCountry());
                        break;
                    case "leadSalutionId":
                        lead.setLeadSalution(leadSalutionRepository.findById((Long) value).orElse(null));
                        break;
                    default:
                        if (fieldDTO.getType().isAssignableFrom(value.getClass())) {
                            Field field = ReflectionUtils.findField(Leads.class, fieldDTO.getName());
                            assert field != null;
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, lead, value);
                        } else {
                            return false;
                        }
                }
            }
            lead.setEditDate(LocalDateTime.now());
            leadsRepository.save(lead);
            return true;
        }

        return false;
    }



    @Override
    @Transactional
    public boolean deleteLeadById(Long id) {
        Optional<Leads> leads = this.leadsRepository.findById(id);
        if (leads.isPresent()) {
            Leads deleteLead = leads.get();
            deleteLead.setIsDelete(1);
            leadsRepository.save(deleteLead);
            return true ;
        } else {
            log.info("There are not exists a lead containing that ID: " + id);
            return false ;
        }
    }

    @Override
    public PageResponse<?> getAllLeadsWithSortByDefault(int pageNo, int pageSize) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Specification<Leads> spec = new Specification<Leads>() {
            @Override
            public Predicate toPredicate(Root<Leads> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.notEqual(root.get("isDelete"), 1);
            }
        };

        Page<Leads> leads = leadsRepository.findAll(spec, pageable);
        return leadConverter.convertToPageResponse(leads, pageable);
    }

    @Override
    public List<IndustryDTO> getAllIndustry() {
        List<Industry> industries = industryRepository.findAll();

        return industries.stream().map(leadConverter::entityToIndustryDTO).toList();
    }

    @Override
    public List<LeadSourceDTO> getAllLeadSource() {
        List<LeadSource> leadSources = leadSourceRepository.findAll();

        return leadSources.stream().map(leadConverter::entityToLeadSourceDTO).toList();
    }

    @Override
    public List<LeadStatusDTO> geLeadStatus() {
        List<LeadStatus> leadStatuses = leadStatusRepository.findAll();

        return leadStatuses.stream().map(leadConverter::entityToLeadStatusDTO).toList();
    }

    @Override
    public PageResponse<?> filterLeadsWithSpecifications(Pageable pageable, String[] search) {
        LeadSpecificationsBuilder builder = new LeadSpecificationsBuilder();

        if (search != null) {
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String l : search) {
                Matcher matcher = pattern.matcher(l);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2),matcher.group(3));
                }
            }

            Page<Leads> leadPage = searchRepository.searchUserByCriteriaWithJoin(builder.params, pageable);
            return leadConverter.convertToPageResponse(leadPage, pageable);
        }
        return getAllLeadsWithSortByDefault(pageable.getPageNumber(),pageable.getPageSize());
    }

    @Override
    public List<LeadResponse> getLeadsByStatus(long id) {
        List<Leads> leadsList = leadsRepository.findByStatus(id);
        return leadsList.stream().map(leadConverter::entityToLeadResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean patchListLead(Long[] id, LeadDTO leadDTO) {
        if (id != null){
            List<Leads> leadsList = new ArrayList<>();
            try {
                for (long i : id){
                    Leads lead = leadsRepository.findById(i).orElse(null);
                    if(lead != null) leadsList.add(lead);
                }
                boolean checked ;
                for (Leads l : leadsList) {
                    checked =patchLead(leadDTO, l.getLeadId());
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
    public AddressInformationDTO getAddressInformationById(long id) {
        AddressInformation addressInformation = addressInformationRepository.findById(id).orElse(null);
        if(addressInformation != null){
            return AddressInformationDTO.builder()
                    .addressInformationId(addressInformation.getAddressInformationId())
                    .street(addressInformation.getStreet())
                    .city(addressInformation.getCity())
                    .province(addressInformation.getProvince())
                    .postalCode(addressInformation.getPostalCode())
                    .country(addressInformation.getCountry())
                    .build();
        }
        return null;
    }

    @Override
    public LeadSalutionDTO getLeadSalutionById(long id) {
        LeadSalution leadSalution = leadSalutionRepository.findById(id).orElse(null);
        if(leadSalution != null){
            return LeadSalutionDTO.builder()
                    .leadSalutionId(leadSalution.getLeadSalutionId())
                    .leadSalutionName(leadSalution.getLeadSalutionName())
                    .build();
        }
        return null;
    }

    @Override
    public List<LeadSalutionDTO> geLeadSalution() {
        List<LeadSalution> leadSalutions = leadSalutionRepository.findAll();

        return leadSalutions.stream().map(leadConverter::entityToLeadSalutionDTO).toList();
    }

    @Override
    public ByteArrayInputStream getExportFileData() throws IOException {
        List<Leads> leads = leadsRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = excelUploadService.dataToExecel(leads);
        return byteArrayInputStream;
    }

    @Override
    @Transactional
    public long createLead(LeadDTO leadDTO) {
        Leads lead = leadConverter.LeadDtoToLeadEntity(leadDTO);
        lead.setCreateDate(LocalDateTime.now());
        lead.setEditDate(LocalDateTime.now());
        lead.setCreatedBy("1");
        lead.setIsDelete(0);
        if(lead.getAddressInformation().getAddressInformationId()==null){
            AddressInformation addressInformation = leadConverter.DTOToAddressInformation(leadDTO.getAddressInformation());
            addressInformationRepository.save(addressInformation);
            lead.setAddressInformation(addressInformation);
        }
        leadsRepository.save(lead);
        return lead.getLeadId();
    }

    @Override
    public LeadResponse getLeadDetail(Long leadId) {
        Leads lead = leadsRepository.findById(leadId).orElseThrow(() -> new RuntimeException("User not found"));
        if(lead.getIsDelete()==1) return null ;
        return leadConverter.entityToLeadResponse(lead);
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
