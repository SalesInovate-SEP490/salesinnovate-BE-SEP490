package fpt.capstone.iOpportunity.dto;

import fpt.capstone.iOpportunity.dto.request.*;
import fpt.capstone.iOpportunity.dto.response.*;
import fpt.capstone.iOpportunity.model.*;
import fpt.capstone.iOpportunity.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class Converter {
    private final ForecastRepository forecastRepository;
    private final StageRepository stageRepository;
    private final TypeRepository typeRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final ProductRepository productRepository;
    private final PriceBookRepository priceBookRepository;
    private final ProductFamilyRepository productFamilyRepository;

    public Opportunity DTOToOpportunity(OpportunityDTO opportunityDTO){
        if (opportunityDTO == null) return null ;
        return Opportunity.builder()
                .userId(opportunityDTO.getUserId())
                .accountId(opportunityDTO.getAccountId())
                .opportunityName(opportunityDTO.getOpportunityName())
                .probability(opportunityDTO.getProbability())
                .forecast(forecastRepository.findById(opportunityDTO.getForecast()).orElse(null))
                .nextStep(opportunityDTO.getNextStep())
                .amount(opportunityDTO.getAmount())
                .closeDate(opportunityDTO.getCloseDate())
                .stage(stageRepository.findById(opportunityDTO.getStage()).orElse(null))
                .type(typeRepository.findById(opportunityDTO.getType()).orElse(null))
                .leadSource(leadSourceRepository.findById(opportunityDTO.getLeadSource()).orElse(null))
                .primaryCampaignSourceId(opportunityDTO.getPrimaryCampaignSourceId())
                .description(opportunityDTO.getDescription())
                .lastModifiedBy(opportunityDTO.getLastModifiedBy())
                .editDate(opportunityDTO.getEditDate())
                .createBy(opportunityDTO.getCreateBy())
                .createDate(opportunityDTO.getCreateDate())
                .isDeleted(opportunityDTO.getIsDeleted())
                .build();
    }


    public ForecastDTO entityToForecastDTO(Forecast forecast){
        return ForecastDTO.builder()
                .id(forecast.getForecastCategoryId())
                .forecastName(forecast.getForecastName())
                .build();
    }

    public StageDTO entityToStageDTO(Stage stage){
        return StageDTO.builder()
                .id(stage.getStageId())
                .stageName(stage.getStageName())
                .build();
    }

    public TypeDTO entityToTypeDTO(Type type){
        return TypeDTO.builder()
                .id(type.getTypeId())
                .typeName(type.getTypeName())
                .build();
    }

    public LeadSourceDTO entityToLeadSourceDTO(LeadSource leadSource){
        return LeadSourceDTO.builder()
                .leadSourceId(leadSource.getLeadSourceId())
                .leadSourceName(leadSource.getLeadSourceName())
                .build();
    }

    public PageResponse<?> convertToPageResponse(Page<?> pageResult, Pageable pageable) {
        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(pageResult.getTotalElements())
                .items(pageResult.getContent())
                .build();
    }


    public OpportunityResponse entityToOpportunityResponse(Opportunity existedOpportunity) {
        return OpportunityResponse.builder()
                .opportunityId(existedOpportunity.getOpportunityId())
                .opportunityName(existedOpportunity.getOpportunityName())
                .accountId(existedOpportunity.getAccountId())
                .amount(existedOpportunity.getAmount())
                .closeDate(existedOpportunity.getCloseDate())
                .opportunityName(existedOpportunity.getOpportunityName())
                .forecast(existedOpportunity.getForecast())
                .stage(existedOpportunity.getStage())
                .type(existedOpportunity.getType())
                .primaryCampaignSourceId(existedOpportunity.getPrimaryCampaignSourceId())
                .nextStep(existedOpportunity.getNextStep())
                .probability(existedOpportunity.getProbability())
                .leadSource(existedOpportunity.getLeadSource())
                .description(existedOpportunity.getDescription())
                .lastModifiedBy(existedOpportunity.getLastModifiedBy())
                .editDate(existedOpportunity.getEditDate())
                .createBy(existedOpportunity.getCreateBy())
                .createDate(existedOpportunity.getCreateDate())
                .isDeleted(existedOpportunity.getIsDeleted())
                .build();
    }


    public ProductResponse entityToProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productCode(product.getProductCode())
                .productDescription(product.getProductDescription())
                .isActive(product.getIsActive())
                .productFamily(productFamilyRepository.findById(product.getProductId()).orElse(null))
                .build();
    }

    public PriceBookResponse entityToPriceBookResponse(PriceBook priceBook) {
        return PriceBookResponse.builder()
                .priceBookId(priceBook.getPriceBookId())
                .priceBookName(priceBook.getPriceBookName())
                .priceBookDescription(priceBook.getPriceBookDescription())
                .isActive(priceBook.getIsActive())
                .isStandardPriceBook(priceBook.getIsStandardPriceBook())
                .build();
    }

    public OpportunityProductResponse OppproToOppproResponse(OpportunityProduct opportunityProduct){
        return OpportunityProductResponse.builder()
                .opportunityProductId(opportunityProduct.getOpportunityProductId())
                .opportunityId(opportunityProduct.getOpportunityId())
                .product(productRepository.findById(opportunityProduct.getProductId()).orElse(null))
                .quantity(opportunityProduct.getQuantity())
                .sales_price(opportunityProduct.getSales_price())
                .date(opportunityProduct.getDate())
                .line_description(opportunityProduct.getLine_description())
                .build();
    }
}
