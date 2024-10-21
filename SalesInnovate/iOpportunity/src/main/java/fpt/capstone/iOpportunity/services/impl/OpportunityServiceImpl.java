package fpt.capstone.iOpportunity.services.impl;

import fpt.capstone.iOpportunity.dto.Converter;
import fpt.capstone.iOpportunity.dto.request.*;
import fpt.capstone.iOpportunity.dto.response.*;
import fpt.capstone.iOpportunity.model.*;
import fpt.capstone.iOpportunity.repositories.*;
import fpt.capstone.iOpportunity.repositories.OpportunityProductRepository;
import fpt.capstone.iOpportunity.repositories.specification.SpecificationsBuilder;
import fpt.capstone.iOpportunity.services.OpportunityClientService;
import fpt.capstone.iOpportunity.services.OpportunityService;
import fpt.capstone.proto.lead.LeadDtoProto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fpt.capstone.iOpportunity.util.AppConst.SEARCH_SPEC_OPERATOR;

@Slf4j
@Service
@AllArgsConstructor
public class OpportunityServiceImpl implements OpportunityService {
    //    private static final Logger log = LoggerFactory.getLogger(OpportunityServiceImpl.class);
    private final OpportunityRepository opportunityRepository;
    private final Converter converter;
    private final ForecastRepository forecastRepository;
    private final StageRepository stageRepository;
    private final TypeRepository typeRepository;
    private final OpportunityClientService opportunityClientService;
    private final CoOppRelationRepository coOppRelationRepository;
    private final LeadSourceRepository leadSourceRepository;
    private final SearchRepository searchRepository;
    private final PriceBookRepository priceBookRepository;
    private final ProductRepository productRepository;
    private final OpportunityProductRepository opportunityProductRepository;
    private final ProductPriceBookRepository productPriceBookRepository;

    @Override
    public Long createOpportunity(OpportunityDTO opportunityDTO) {
        Opportunity opportunity = converter.DTOToOpportunity(opportunityDTO);
        if (opportunity.getForecast() == null) {
            throw new RuntimeException("Forecast Category not existed");
        } else if (opportunity.getStage() == null) {
            throw new RuntimeException("Stage not existed");
        }
        opportunity.setUserId("1");
        opportunity.setCreateBy("1");
        opportunity.setCreateDate(LocalDateTime.now());
        opportunity.setLastModifiedBy("1");
        opportunity.setEditDate(LocalDateTime.now());
        opportunity.setIsDeleted(false);
        opportunityRepository.save(opportunity);
        return opportunity.getOpportunityId();
    }


    @Override
    public void deleteOpportunity(Long id) {
        Optional<Opportunity> opportunityOptional = opportunityRepository.findById(id);
        if (!opportunityOptional.isPresent()) {
            throw new RuntimeException("Opportunity not existed");
        }
        Opportunity existedOpportunity = opportunityOptional.get();
        existedOpportunity.setIsDeleted(true);
        opportunityRepository.save(existedOpportunity);
    }

    @Override
    public OpportunityResponse getDetailOpportunity(Long id) {
        Opportunity opportunityOptional = opportunityRepository.findById(id).orElse(null);
        if (opportunityOptional == null || opportunityOptional.getIsDeleted()) {
            throw new RuntimeException("Opportunity not existed");
        }
        return converter.entityToOpportunityResponse(opportunityOptional);

    }

    @Override
    public PageResponse<?> getListOpportunity(int pageNo, int pageSize) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Specification<Opportunity> spec = new Specification<Opportunity>() {
            @Override
            public Predicate toPredicate(Root<Opportunity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("isDeleted"), false);
            }
        };

        Page<Opportunity> opportunities = opportunityRepository.findAll(spec, pageable);
        return converter.convertToPageResponse(opportunities, pageable);
    }

    @Override
    public List<ForecastDTO> getListForecastCategory() {
        List<Forecast> listForecast = forecastRepository.findAll();
        return listForecast.stream().map(converter::entityToForecastDTO).toList();
    }

    @Override
    public List<StageDTO> getListStage() {
        List<Stage> listStage = stageRepository.findAll();
        return listStage.stream().map(converter::entityToStageDTO).toList();
    }

    @Override
    public List<TypeDTO> getListType() {
        List<Type> listType = typeRepository.findAll();
        return listType.stream().map(converter::entityToTypeDTO).toList();
    }

    @Override
    public List<LeadSourceDTO> getLeadSource() {
        List<LeadSource> leadSources = leadSourceRepository.findAll();
        return leadSources.stream().map(converter::entityToLeadSourceDTO).toList();
    }

    @Override
    public PageResponse<?> getListOpportunityByAccount(int pageNo, int pageSize, long accountId) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Specification<Opportunity> spec = new Specification<Opportunity>() {
            @Override
            public Predicate toPredicate(Root<Opportunity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
                predicates.add(criteriaBuilder.equal(root.get("accountId"), accountId));
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<Opportunity> opportunities = opportunityRepository.findAll(spec, pageable);
        return converter.convertToPageResponse(opportunities, pageable);
    }

    @Override
    public PageResponse<?> getListOpportunityByContact(int pageNo, int pageSize, long contactId) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, "createDate"));

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        List<CoOppRelation> list = coOppRelationRepository.getListOpportunityByContact(contactId);
        if (!list.isEmpty()) {
            List<Opportunity> opportunities = new ArrayList<>();
            for (CoOppRelation relation : list) {
                opportunities.add(opportunityRepository.findById(relation.getOpportunityId()).orElse(null));
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), opportunities.size());

            Page<Opportunity> opportunitiesPage = new PageImpl<>(opportunities.subList(start, end), pageable, opportunities.size());
            return converter.convertToPageResponse(opportunitiesPage, pageable);
        }
        return null;
    }

    @Override
    public Long convertNewOpportunity(String opportunityName, long leadId, long accountId, long contactId) {
        try {
            LeadDtoProto proto = opportunityClientService.getLead(leadId);
            LeadSource leadSource = leadSourceRepository.findById(proto.getSource().getLeadSourceId()).orElse(null);
            Opportunity opportunity = Opportunity.builder()
                    .opportunityName(opportunityName)
                    .accountId(accountId)
                    .probability(10F)
                    .leadSource(leadSource)
                    .forecast(forecastRepository.findById(1L).orElse(null))
                    .stage(stageRepository.findById(1L).orElse(null))
                    .type(typeRepository.findById(1L).orElse(null))
                    .userId("1")
                    .createBy("1")
                    .createDate(LocalDateTime.now())
                    .lastModifiedBy("1")
                    .editDate(LocalDateTime.now())
                    .isDeleted(false)
                    .build();
            opportunityRepository.save(opportunity);
            //Thêm quan hệ giứa account và contact
            CoOppRelation coOppRelation = CoOppRelation.builder()
                    .opportunityId(opportunity.getOpportunityId())
                    .contactId(contactId)
                    .build();
            coOppRelationRepository.save(coOppRelation);
            return opportunity.getOpportunityId();
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

    @Override
    public Long convertExistOpportunity(long contactId, long opportunityId) {
        try {
            //Thêm quan hệ giữa  account và contact
            List<CoOppRelation> list = coOppRelationRepository.countNumRelation(opportunityId, contactId);
            if (list.size() <= 0) {
                CoOppRelation coOppRelation = CoOppRelation.builder()
                        .opportunityId(opportunityId)
                        .contactId(contactId)
                        .build();
                coOppRelationRepository.save(coOppRelation);
                return opportunityId;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public boolean patchOpportunity(OpportunityDTO opportunityDTO, long id) {
        Map<String, Object> patchMap = getPatchData(opportunityDTO);
        if (patchMap.isEmpty()) {
            return true;
        }

        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Opportunity with id: " + id));

        if (opportunity != null) {
            for (Map.Entry<String, Object> entry : patchMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Field fieldDTO = ReflectionUtils.findField(OpportunityDTO.class, key);

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
                    case "forecast":
                        opportunity.setForecast(forecastRepository.findById((Long) value).orElse(null));
                        break;
                    case "stage":
                        opportunity.setStage(stageRepository.findById((Long) value).orElse(null));
                        break;
                    case "type":
                        opportunity.setType(typeRepository.findById((Long) value).orElse(null));
                        break;
                    case "leadSource":
                        opportunity.setLeadSource(leadSourceRepository.findById((Long) value).orElse(null));
                        break;
                    default:
                        if (fieldDTO.getType().isAssignableFrom(value.getClass())) {
                            Field field = ReflectionUtils.findField(Opportunity.class, fieldDTO.getName());
                            assert field != null;
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, opportunity, value);
                        } else {
                            return false;
                        }
                }
            }
            opportunity.setEditDate(LocalDateTime.now());
            opportunityRepository.save(opportunity);
            return true;
        }

        return false;
    }

    @Override
    public boolean patchListOpportunity(Long[] id, OpportunityDTO opportunityDTO) {
        if (id != null) {
            List<Opportunity> opportunityList = new ArrayList<>();
            try {
                for (long i : id) {
                    opportunityRepository.findById(i).ifPresent(opportunityList::add);
                }
                boolean checked;
                for (Opportunity l : opportunityList) {
                    checked = patchOpportunity(opportunityDTO, l.getOpportunityId());
                    if (!checked) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public PageResponse<?> filterOpportunity(Pageable pageable, String[] search) {
        SpecificationsBuilder builder = new SpecificationsBuilder();

        if (search != null) {
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String l : search) {
                Matcher matcher = pattern.matcher(l);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
                }
            }

            Page<Opportunity> page = searchRepository.searchUserByCriteriaWithJoin(builder.params, pageable);
            return converter.convertToPageResponse(page, pageable);
        }
        return getListOpportunity(pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public Boolean addPricebook (long opportunityId,long pricebookId) {
        try {
            Opportunity opportunity= opportunityRepository.findById(opportunityId).orElse(null);
            PriceBook priceBook = priceBookRepository.findById(pricebookId).orElse(null);
            if(opportunity == null || priceBook == null ) return false ;
            if(priceBook.getIsActive()==0) return false ;
            opportunity.setPriceBook(priceBook.getPriceBookId());
            opportunityRepository.save(opportunity);
            return true ;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Integer countProduct(long opportunity) {
        try{
            Specification<OpportunityProduct> spec = new Specification<OpportunityProduct>() {
                @Override
                public Predicate toPredicate(Root<OpportunityProduct> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.equal(root.get("opportunityId"), opportunity));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }
            };
            List<OpportunityProduct> list = opportunityProductRepository.findAll(spec);
            return list.size();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Boolean addProductToOpportunity(OpportunityPriceBookProductDTO dto) {
        try {
            Opportunity opportunity = opportunityRepository.findById(dto.getOpportunityId()).orElse(null);
            PriceBook priceBook = priceBookRepository.findById(opportunity.getPriceBook()).orElse(null);

            if(opportunity==null||priceBook==null) return false ;
            for (OpportunityProductDTO opportunityProductDTO : dto.getOpportunityProductDTOS()){

                Specification<ProductPriceBook> spec = new Specification<ProductPriceBook>() {
                    @Override
                    public Predicate toPredicate(Root<ProductPriceBook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                        List<Predicate> predicates = new ArrayList<>();
                        predicates.add(criteriaBuilder.equal(root.get("productId"), opportunityProductDTO.getProductId()));
                        predicates.add(criteriaBuilder.equal(root.get("priceBookId"), priceBook.getPriceBookId()));
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                };
                List<ProductPriceBook> list = productPriceBookRepository.findAll(spec);

                OpportunityProduct opportunityProduct = OpportunityProduct.builder()
                        .opportunityId(opportunity.getOpportunityId())
                        .productId(opportunityProductDTO.getProductId())
                        .sales_price(list.get(0).getListPrice())
                        .quantity(opportunityProductDTO.getQuantity())
                        .date(opportunityProductDTO.getDate())
                        .line_description(opportunityProductDTO.getLine_description())
                    .build();

                opportunityProductRepository.save(opportunityProduct);
            }
            return true ;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public OpportunityPriceBookProductResponse getProduct(long opportunityId) {
        try{
            Opportunity opportunity = opportunityRepository.findById(opportunityId).orElse(null);
            PriceBook priceBook = priceBookRepository.findById(opportunity.getPriceBook()).orElse(null);

            if(opportunity==null||priceBook==null)
                throw new EntityNotFoundException("cannot find product or pricebook");

            Specification<OpportunityProduct> spec = new Specification<OpportunityProduct>() {
                @Override
                public Predicate toPredicate(Root<OpportunityProduct> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.equal(root.get("opportunityId"), opportunityId));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }
            };
            List<OpportunityProduct> list = opportunityProductRepository.findAll(spec);
            OpportunityPriceBookProductResponse response =OpportunityPriceBookProductResponse.builder()
                    .opportunity(opportunity)
                    .priceBook(priceBook)
                    .products(list.stream().map(converter::OppproToOppproResponse).toList())
                    .build();
            return response;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<PriceBookResponse> searchPriceBookToAdd(long opportunityId, String search) {
        try{
            Opportunity opportunity = opportunityRepository.findById(opportunityId).orElse(null);
            if(opportunity==null)
                throw new EntityNotFoundException("Can not find Opportunity with Id"+opportunityId);
            if(opportunity.getPriceBook()==null){
                Specification<PriceBook> specProduct = new Specification<PriceBook>() {
                    @Override
                    public Predicate toPredicate(Root<PriceBook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                        List<Predicate> predicates = new ArrayList<>();
                        predicates.add(criteriaBuilder.like(root.get("priceBookName"), "%"+search+"%"));
                        predicates.add(criteriaBuilder.equal(root.get("isActive"), 1));

                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                };
                List<PriceBook> listSearchProduct = priceBookRepository.findAll(specProduct);
                return listSearchProduct.stream().map(converter::entityToPriceBookResponse).toList();
            }else{
                Specification<PriceBook> specProduct = new Specification<PriceBook>() {
                    @Override
                    public Predicate toPredicate(Root<PriceBook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                        List<Predicate> predicates = new ArrayList<>();
                        predicates.add(criteriaBuilder.like(root.get("priceBookName"), "%"+search+"%"));
                        predicates.add(criteriaBuilder.equal(root.get("isActive"), 1));
                        predicates.add(criteriaBuilder.notEqual(root.get("priceBookId"), opportunity.getPriceBook()));
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                };
                List<PriceBook> listSearchProduct = priceBookRepository.findAll(specProduct);
                return listSearchProduct.stream().map(converter::entityToPriceBookResponse).toList();
            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ProductResponse> searchProductToAdd(long opportunityId, String search) {
        try{
            Opportunity opportunity = opportunityRepository.findById(opportunityId).orElse(null);
            PriceBook priceBook = priceBookRepository.findById(opportunity.getPriceBook()).orElse(null);

            if(opportunity==null||priceBook==null)
                throw new EntityNotFoundException("cannot find product or pricebook");
            Specification<ProductPriceBook> spec= new Specification<ProductPriceBook>() {
                @Override
                public Predicate toPredicate(Root<ProductPriceBook> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.equal(root.get("priceBookId"), priceBook.getPriceBookId()));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }
            };
            List<ProductPriceBook> listProductPriceBooks = productPriceBookRepository.findAll(spec);
            List<Product> productList = getListProductByProductPriceBook(listProductPriceBooks);

            Specification<Product> specProduct = new Specification<Product>() {
                @Override
                public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.notLike(root.get("productName"), "%"+search+"%"));
                    predicates.add(criteriaBuilder.notEqual(root.get("isActive"), 1));
                    return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
                }
            };
            List<Product> listSearchProduct = productRepository.findAll(specProduct);

            Specification<OpportunityProduct> specOppProduct = new Specification<OpportunityProduct>() {
                @Override
                public Predicate toPredicate(Root<OpportunityProduct> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(criteriaBuilder.equal(root.get("opportunityId"),opportunityId));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }
            };
            List<OpportunityProduct> listOppProduct = opportunityProductRepository.findAll(specOppProduct);
            List<Product> productOppList = getListProductByOppProduct(listOppProduct);

            if (productList != null) {
                productList.removeAll(listSearchProduct);
                productList.removeAll(productOppList);
            }
            return productList.stream().map(converter::entityToProductResponse).toList();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
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
                log.info(e.getMessage(), e.getCause());
            }
        }
        return patchMap;
    }

    private List<Product> getListProductByProductPriceBook(List<ProductPriceBook> list){
        List<Product> products = new ArrayList<>();
        for (ProductPriceBook productPriceBook: list){
            Product product = productRepository.findById(productPriceBook.getProductId()).orElse(null);
            if(product != null) products.add(product);
        }
        return products;
    }

    private List<Product> getListProductByOppProduct(List<OpportunityProduct> listOppProduct) {
        List<Product> products = new ArrayList<>();
        for (OpportunityProduct productPriceBook: listOppProduct){
            Product product = productRepository.findById(productPriceBook.getProductId()).orElse(null);
            if(product != null) products.add(product);
        }
        return products;
    }
}


