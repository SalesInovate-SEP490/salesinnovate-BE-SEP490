package fpt.capstone.SalesInnovate.iLead.dto;

import fpt.capstone.SalesInnovate.iLead.dto.request.*;
import fpt.capstone.SalesInnovate.iLead.dto.response.LeadResponse;
import fpt.capstone.SalesInnovate.iLead.dto.response.PageResponse;
import fpt.capstone.SalesInnovate.iLead.model.*;
import fpt.capstone.SalesInnovate.iLead.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class Converter {
    @Autowired
    private LeadStatusRepository leadStatusRepository;
    @Autowired
    private IndustryRepository industryRepository;
    @Autowired
    private LeadsRepository leadsRepository;
    @Autowired
    private LeadSourceRepository leadSourceRepository;
    @Autowired
    private LeadRatingRepository leadRatingRepository;
    @Autowired
    private AddressInformationRepository addressInformationRepository;
    @Autowired
    private LeadSalutionRepository leadSalutionRepository ;

//    public LeadDTO LeadEntityToLeadDto(Leads lead){
//        LeadDTO dto = new LeadDTO();
//        dto.setCustomerId(lead.getLeadId());
//        dto.setAccountId(lead.getAccountId());
//
//        if(lead.getIndustry() != null) {
//            dto.setIndustryId(lead.getIndustry().getIndustryId());
//        }else {
//            dto.setIndustryId(null);
//        }
//        if(lead.getLeadStatus() != null) {
//            dto.setStatusId(lead.getLeadStatus().getLeadStatusId());
//        }
//        else {
//            dto.setStatusId(null);
//        }
//        if(lead.getLeadSource() != null) {
//            dto.setLeadSourceId(lead.getLeadSource().getLeadSourceId());
//        }
//        else {
//            dto.setLeadSourceId(null);
//        }
//        dto.setFirstName(lead.getFirstName());
//        dto.setLastName(lead.getLastName());
//        dto.setGender(lead.getGender());
//        dto.setTitle(lead.getTitle());
//        dto.setEmail(lead.getEmail());
//        dto.setPhone(lead.getPhone());
//        dto.setWebsite(lead.getWebsite());
//        dto.setCompany(lead.getCompany());
//        dto.setAddress(lead.getAddress());
//        dto.setCreated_by(lead.getCreatedBy());
//        dto.setEditBy(lead.getEditBy());
//        dto.setLastModifiedBy(lead.getLastModifiedBy());
//        dto.setRating(lead.getRating());
//
//        return dto;
//    }
//
//    public List<LeadDTO> LeadEntityToLeadDto(List<Leads> lead){
//        return lead.stream().map(x -> LeadEntityToLeadDto(x)).collect(Collectors.toList());
//    }
//
//    public List<Leads> LeadDtoToLeadEntity(List<LeadDTO> leadDto){
//        return leadDto.stream().map(x -> LeadDtoToLeadEntity(x)).collect(Collectors.toList());
//
//    }
    //Convert DTO to Entity
    public Leads LeadDtoToLeadEntity(LeadDTO leaDto){
        return Leads.builder()
                .userId("1")
                .industry(leaDto.getIndustryId()!=null?industryRepository
                        .findById(leaDto.getIndustryId()).orElse(null):null)
                .leadStatus(leaDto.getLeadStatusID()!=null?leadStatusRepository
                        .findById(leaDto.getLeadStatusID()).orElse(null):null)
                .leadSource(leaDto.getLeadSourceId()!=null?leadSourceRepository
                        .findById(leaDto.getLeadSourceId()).orElse(null):null)
                .leadRating(leaDto.getLeadRatingId()!=null?leadRatingRepository
                        .findById(leaDto.getLeadRatingId()).orElse(null):null)
                .addressInformation(DTOToAddressInformation(leaDto.getAddressInformation()))
                .leadSalution(leaDto.getLeadSalutionId()!=null?leadSalutionRepository
                        .findById(leaDto.getLeadSalutionId()).orElse(null):null)
                .firstName(leaDto.getFirstName())
                .lastName(leaDto.getLastName())
                .middleName(leaDto.getMiddleName())
                .gender(leaDto.getGender())
                .title(leaDto.getTitle())
                .email(leaDto.getEmail())
                .phone(leaDto.getPhone())
                .website(leaDto.getWebsite())
                .company(leaDto.getCompany())
                .noEmployee(leaDto.getNoEmployee())
                .createdBy(leaDto.getCreatedBy())
                .createDate(leaDto.getCreateDate())
                .editDate(leaDto.getEditDate())
                .editBy(leaDto.getEditBy())
                .isDelete(leaDto.getIsDelete())
                .build();
    }
//
//    public Log_Call_Leads LogCallLeadDTOToEntity(LogCallLeadDTO logCallLeadDTO){
//        Log_Call_Leads log_call_leads = new Log_Call_Leads();
//        log_call_leads.setSubject(logCallLeadDTO.getSubject());
//        log_call_leads.setComment(logCallLeadDTO.getComment());
//        if(logCallLeadDTO.getLeadId() != null) {
//            int id = logCallLeadDTO.getLeadId();
//            Leads lead = this.leadsRepository
//                    .findById(id).orElse(null);
//            log_call_leads.setLead(lead);
//        }else{
//            log_call_leads.setLead(null);
//        }
//        return log_call_leads;
//    }
//    public LogCallLeadDTO LogCallLeadEntityToDTO(Log_Call_Leads log_call_leads){
//        LogCallLeadDTO logCallLeadDTO = new LogCallLeadDTO();
//        logCallLeadDTO.setSubject(log_call_leads.getSubject());
//        logCallLeadDTO.setComment(log_call_leads.getComment());
//        logCallLeadDTO.setLeadId(log_call_leads.getLead().getLeadId());
//        return logCallLeadDTO;
//    }
//    public Leads updateLeadsFromLeadDTO(Leads lead,LeadDTO leadto){
//        lead.setCustomerId(leadto.getCustomerId());
//        lead.setAccountId(leadto.getAccountId());
//        if (leadto.getIndustryId() != null) {
//            lead.setIndustry(this.industryRepository
//                    .findById(leadto.getIndustryId()).orElse(null));
//        }else {
//            lead.setIndustry(null);
//        }
//        if (leadto.getStatusId() != null) {
//            lead.setLeadStatus(this.leadStatusRepository
//                    .findById(leadto.getStatusId()).orElse(null));
//        }else {
//            lead.setLeadStatus(null);
//        }
//        lead.setFirstName(leadto.getFirstName());
//        lead.setLastName(leadto.getLastName());
//        lead.setGender(leadto.getGender());
//        lead.setTitle(leadto.getTitle());
//        lead.setEmail(leadto.getEmail());
//        lead.setPhone(leadto.getPhone());
//        lead.setWebsite(leadto.getWebsite());
//        lead.setCompany(leadto.getCompany());
//        lead.setAddress(leadto.getAddress());
//        lead.setCreatedBy(leadto.getCreated_by());
//        lead.setEditBy(leadto.getEditBy());
//        lead.setLastModifiedBy(leadto.getLastModifiedBy());
//        lead.setRating(leadto.getRating());
//        return lead;
//    }
//    public Log_Call_Leads updateLogCallLeadFromLogCallLeadDTO(Log_Call_Leads log_call_leads,
//                                                              LogCallLeadDTO logCallLeadDTO) {
//        log_call_leads.setSubject(logCallLeadDTO.getSubject());
//        log_call_leads.setComment(logCallLeadDTO.getComment());
//        if(logCallLeadDTO.getLeadId() != null) {
//            int id = logCallLeadDTO.getLeadId();
//            Leads lead = this.leadsRepository
//                    .findById(id).orElse(null);
//            log_call_leads.setLead(lead);
//        }else{
//            log_call_leads.setLead(null);
//        }
//        return log_call_leads;
//    }

    public LeadResponse entityToLeadResponse(Leads lead){
        return LeadResponse.builder()
                .leadId(lead.getLeadId())
                . userId(lead.getUserId())
                . industry(entityToIndustryDTO(lead.getIndustry()))
                . status(entityToLeadStatusDTO(lead.getLeadStatus()))
                . source(entityToLeadSourceDTO(lead.getLeadSource()))
                . rating(entityToLeadRatingDTO(lead.getLeadRating()))
                . addressInfor(entityToAddressInformationDTO(lead.getAddressInformation()))
                . salution(entityToLeadSalutionDTO(lead.getLeadSalution()))
                . firstName(lead.getFirstName())
                . lastName(lead.getLastName())
                . middleName(lead.getMiddleName())
                . gender(lead.getGender())
                . title(lead.getTitle())
                . email(lead.getEmail())
                . phone(lead.getPhone())
                . website(lead.getWebsite())
                . company(lead.getCompany())
                . noEmployee(lead.getNoEmployee())
                . createdBy(lead.getCreatedBy())
                . createDate(lead.getCreateDate())
                . editDate(lead.getEditDate())
                . editBy(lead.getEditBy())
                . isDelete(lead.getIsDelete())
                .build();
    }

    public LeadSalutionDTO entityToLeadSalutionDTO(LeadSalution leadSalution) {
        return LeadSalutionDTO.builder()
                .leadSalutionId(leadSalution.getLeadSalutionId())
                .leadSalutionName(leadSalution.getLeadSalutionName())
                .build();
    }

    private AddressInformationDTO entityToAddressInformationDTO(AddressInformation addressInformation) {
        return AddressInformationDTO.builder()
                .addressInformationId(addressInformation.getAddressInformationId())
                .street(addressInformation.getStreet())
                .city(addressInformation.getCity())
                .province(addressInformation.getProvince())
                .postalCode(addressInformation.getPostalCode())
                .country(addressInformation.getCountry())
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


    private LeadRatingDTO entityToLeadRatingDTO(LeadRating leadRating) {
        if (leadRating == null) return null ;
        return LeadRatingDTO.builder()
                .leadRatingId(leadRating.getLeadRatingId())
                .leadRatingName(leadRating.getLeadRatingName())
                .build();
    }

    public IndustryDTO entityToIndustryDTO(Industry industry){
        if (industry == null) return null ;
        return IndustryDTO.builder()
                .industryId(industry.getIndustryId())
                .industryStatusName(industry.getIndustryStatusName())
                .build();
    }
    public LeadSourceDTO entityToLeadSourceDTO(LeadSource leadSource){
        if (leadSource == null) return null ;
        return LeadSourceDTO.builder()
                .leadSourceId(leadSource.getLeadSourceId())
                .leadSourceName(leadSource.getLeadSourceName())
                .build();
    }
    public LeadStatusDTO entityToLeadStatusDTO(LeadStatus leadStatus){
        if (leadStatus == null) return null ;
        return LeadStatusDTO.builder()
                .leadStatusId(leadStatus.getLeadStatusId())
                .leadStatusName(leadStatus.getLeadStatusName())
                .build();
    }

//    public LeadSalutionDTO entityToLeadSalutionDTO(LeadSalution leadSalution){
//        return LeadSalutionDTO.builder()
//                .leadSalutionId(leadSalution.getLeadSalutionId())
//                .leadSalutionName(leadSalution.getLeadSalutionName())
//                .build();
//    }

    public PageResponse<?> convertToPageResponse(Page<Leads> leads, Pageable pageable) {
        List<LeadResponse> response = leads.stream().map(this::entityToLeadResponse).toList();
        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(leads.getTotalPages())
                .items(response)
                .build();
    }
    public Leads convertFileImportToLeads(LeadImportFileDTO leadImportFileDTO,
                                          AddressInformation AddressInformation,String userId){
        Industry industry =
                industryRepository.findByIndustryStatusName(leadImportFileDTO.getIndustryStatusName());
        LeadSource leadSource =
                leadSourceRepository.findLeadSourceByLeadSourceName(leadImportFileDTO.getLeadSourceName());
        LeadStatus leadStatus =
                leadStatusRepository.findLeadStatusByLeadStatusName(leadImportFileDTO.getLeadStatusName());
        LeadRating leadRating =
                leadRatingRepository.findLeadRatingByLeadRatingName(leadImportFileDTO.getLeadRatingName());
        LeadSalution leadSalution= leadSalutionRepository
                .findLeadSalutionByLeadSalutionName(leadImportFileDTO.getLeadSalutionName());
        int gender;
        if(leadImportFileDTO.getGender().toLowerCase().equals("male")){
            gender =1;
        }
        else if( leadImportFileDTO.getGender().toLowerCase().equals("female") ){
            gender =0;
        }
        else throw new RuntimeException("Gender input wrong ");
        return Leads.builder()
                .userId(userId)
                .industry(industry)
                .leadStatus(leadStatus)
                .leadSource(leadSource)
                .leadRating(leadRating)
                .addressInformation(AddressInformation)
                .leadSalution(leadSalution)
                .firstName(leadImportFileDTO.getFirstName())
                .lastName(leadImportFileDTO.getLastName())
                . middleName(leadImportFileDTO.getMiddleName())
                . gender(gender)
                . title(leadImportFileDTO.getTitle())
                . email(leadImportFileDTO.getEmail())
                . phone(leadImportFileDTO.getPhone())
                . website(leadImportFileDTO.getWebsite())
                . company(leadImportFileDTO.getCompany())
                . noEmployee(leadImportFileDTO.getNoEmployee())
                . createDate(LocalDateTime.now())
                . isDelete(0)
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
    public LeadExportDTO LeadEntityToLeadExportDTO(Leads lead) {
        return LeadExportDTO.builder()
                .leadId(lead.getLeadId())
                .userId(Optional.ofNullable(lead.getUserId()).orElse(null))
                .leadSourceId(Optional.ofNullable(lead.getLeadSource()).map(LeadSource::getLeadSourceId).orElse(null))
                .industryId(Optional.ofNullable(lead.getIndustry()).map(Industry::getIndustryId).orElse(null))
                .leadStatusID(Optional.ofNullable(lead.getLeadStatus()).map(LeadStatus::getLeadStatusId).orElse(null))
                .leadRatingId(Optional.ofNullable(lead.getLeadRating()).map(LeadRating::getLeadRatingId).orElse(null))
                .addressInformationId(Optional.ofNullable(lead.getAddressInformation()).map(AddressInformation::getAddressInformationId).orElse(null))
                .leadSalutionId(Optional.ofNullable(lead.getLeadSalution()).map(LeadSalution::getLeadSalutionId).orElse(null))
                .firstName(Optional.ofNullable(lead.getFirstName()).orElse(null))
                .lastName(Optional.ofNullable(lead.getLastName()).orElse(null))
                .middleName(Optional.ofNullable(lead.getMiddleName()).orElse(null))
                .gender(Optional.ofNullable(lead.getGender()).orElse(null))
                .title(Optional.ofNullable(lead.getTitle()).orElse(null))
                .email(Optional.ofNullable(lead.getEmail()).orElse(null))
                .phone(Optional.ofNullable(lead.getPhone()).orElse(null))
                .website(Optional.ofNullable(lead.getWebsite()).orElse(null))
                .company(Optional.ofNullable(lead.getCompany()).orElse(null))
                .createdBy(Optional.ofNullable(lead.getCreatedBy()).orElse(null))
                .createDate(Optional.ofNullable(lead.getCreateDate()).orElse(null))
                .build();
    }
    public List<LeadExportDTO> LeadEntityListToLeadExportDTOList(List<Leads> leadsList) {
        return leadsList.stream()
                .map(this::LeadEntityToLeadExportDTO)
                .collect(Collectors.toList());
    }
}
