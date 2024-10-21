package fpt.capstone.SalesInnovate.iLead.service;


import fpt.capstone.SalesInnovate.iLead.dto.request.*;
import fpt.capstone.SalesInnovate.iLead.dto.response.LeadResponse;
import fpt.capstone.SalesInnovate.iLead.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface LeadsService {
    void saveLeadsToDatabase(MultipartFile file, String userId);

    long createLead(LeadDTO leadDTO);

    boolean patchLead(LeadDTO leadDTO, long id);

    boolean deleteLeadById(Long id);

    LeadResponse getLeadDetail(Long leadId);

    PageResponse<?> getAllLeadsWithSortByDefault(int pageNo, int pageSize);

    List<IndustryDTO> getAllIndustry();

    List<LeadSourceDTO> getAllLeadSource();

    List<LeadStatusDTO> geLeadStatus();

    PageResponse<?> filterLeadsWithSpecifications(Pageable pageable,  String[] search);

    List<LeadResponse> getLeadsByStatus(long id);

    boolean patchListLead(Long[] id, LeadDTO leadDTO);

    AddressInformationDTO getAddressInformationById(long id);

    LeadSalutionDTO getLeadSalutionById(long id);

    List<LeadSalutionDTO> geLeadSalution();

    ByteArrayInputStream getExportFileData() throws IOException;
}
