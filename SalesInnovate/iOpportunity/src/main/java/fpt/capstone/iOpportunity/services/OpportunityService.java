package fpt.capstone.iOpportunity.services;

import fpt.capstone.iOpportunity.dto.request.*;
import fpt.capstone.iOpportunity.dto.response.*;
import fpt.capstone.iOpportunity.model.Opportunity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OpportunityService {
    Long createOpportunity(OpportunityDTO opportunityDTO);
    void deleteOpportunity(Long id);
    OpportunityResponse getDetailOpportunity(Long id);
    PageResponse<?> getListOpportunity( int pageNo, int pageSize);
    List<ForecastDTO> getListForecastCategory();
    List<StageDTO> getListStage();
    List<TypeDTO> getListType();
    List<LeadSourceDTO> getLeadSource();
    PageResponse<?> getListOpportunityByAccount(int pageNo, int pageSize,long accountId);
    PageResponse<?> getListOpportunityByContact(int pageNo, int pageSize,long contactId);

    Long convertNewOpportunity(String opportunityName,long leađId,
                                  long accountId, long contactId);

    Long convertExistOpportunity(long contactId, long opportunityId);

    boolean patchOpportunity(OpportunityDTO opportunityDTO, long id);

    boolean patchListOpportunity(Long[] id, OpportunityDTO opportunityDTO);

    PageResponse<?> filterOpportunity(Pageable pageable, String[] search);

    Boolean addPricebook (long opportunityId,long pricebookId);

    Integer countProduct (long opportunityId);

    Boolean addProductToOpportunity(OpportunityPriceBookProductDTO dto);

    OpportunityPriceBookProductResponse getProduct(long opportunityId);

    List<PriceBookResponse> searchPriceBookToAdd (long opportunityId,String search);

    List<ProductResponse> searchProductToAdd (long opportunityId, String search);
}
