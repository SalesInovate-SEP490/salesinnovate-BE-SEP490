package fpt.capstone.SalesInnovate.iLead.repository;

import fpt.capstone.SalesInnovate.iLead.model.*;
import fpt.capstone.SalesInnovate.iLead.repository.specification.SpecSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static fpt.capstone.SalesInnovate.iLead.util.AppConst.*;

@Component
@Slf4j
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<Leads> searchUserByCriteriaWithJoin(List<SpecSearchCriteria> params, Pageable pageable) {
        log.info("searchUserByCriteriaWithJoin");

        List<Leads> users = getAllLeadsWithJoin(params, pageable);

        Long totalElements = countAllLeadsWithJoin(params);

        return new PageImpl<>(users, pageable, totalElements);
    }

    private List<Leads> getAllLeadsWithJoin(List<SpecSearchCriteria> params, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Leads> query = criteriaBuilder.createQuery(Leads.class);
        Root<Leads> leadsRoot = query.from(Leads.class);

        List<Predicate> predicateList = new ArrayList<>();

        for (SpecSearchCriteria criteria : params) {
            String key = criteria.getKey();
            if (key.contains(INDUSTRY_REGEX)) {
                Join<Industry,Leads> industryRoot = leadsRoot.join("industry");
                predicateList.add(toJoinPredicate(industryRoot, criteriaBuilder, criteria, INDUSTRY_REGEX));
            } else if (key.contains(RATING_REGEX)) {
                Join< LeadRating,Leads> ratingRoot = leadsRoot.join("leadRating");
                predicateList.add(toJoinPredicate(ratingRoot, criteriaBuilder, criteria, RATING_REGEX));
            }else if (key.contains(SALUTION_REGEX)) {
                Join< LeadSalution,Leads> salutionRoot = leadsRoot.join("leadSalution");
                predicateList.add(toJoinPredicate(salutionRoot, criteriaBuilder, criteria, SALUTION_REGEX));
            }else if (key.contains(SOURCE_REGEX)) {
                Join< LeadSource,Leads> sourceRoot = leadsRoot.join("leadSource");
                predicateList.add(toJoinPredicate(sourceRoot, criteriaBuilder, criteria, SOURCE_REGEX));
            }else if (key.contains(STATUS_REGEX)) {
                Join< LeadStatus,Leads> statusRoot = leadsRoot.join("leadStatus");
                predicateList.add(toJoinPredicate(statusRoot, criteriaBuilder, criteria, STATUS_REGEX));
            }else if (key.contains(ADDRESS_REGEX)) {
                Join< AddressInformation,Leads> addressRoot = leadsRoot.join("addressInformation");
                predicateList.add(toJoinPredicate(addressRoot, criteriaBuilder, criteria, ADDRESS_REGEX));
            }
            else {
                predicateList.add(toPredicate(leadsRoot, criteriaBuilder, criteria));
            }
        }
        predicateList.add(criteriaBuilder.equal(leadsRoot.get("isDelete"),0));

        Predicate predicates = criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        query.where(predicates);

        List<Leads> leadsList = entityManager.createQuery(query)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return leadsList;
    }

    private Long countAllLeadsWithJoin(List<SpecSearchCriteria> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Leads> leadsRoot = query.from(Leads.class);

        List<Predicate> predicateList = new ArrayList<>();

        for (SpecSearchCriteria criteria : params) {
            String key = criteria.getKey();
            if (key.contains(INDUSTRY_REGEX)) {
                Join<Industry,Leads> industryRoot = leadsRoot.join("industry");
                predicateList.add(toJoinPredicate(industryRoot, criteriaBuilder, criteria, INDUSTRY_REGEX));
            } else if (key.contains(RATING_REGEX)) {
                Join< LeadRating,Leads> ratingRoot = leadsRoot.join("leadRating");
                predicateList.add(toJoinPredicate(ratingRoot, criteriaBuilder, criteria, RATING_REGEX));
            }else if (key.contains(SALUTION_REGEX)) {
                Join< LeadSalution,Leads> salutionRoot = leadsRoot.join("leadSalution");
                predicateList.add(toJoinPredicate(salutionRoot, criteriaBuilder, criteria, SALUTION_REGEX));
            }else if (key.contains(SOURCE_REGEX)) {
                Join< LeadSource,Leads> sourceRoot = leadsRoot.join("leadSource");
                predicateList.add(toJoinPredicate(sourceRoot, criteriaBuilder, criteria, SOURCE_REGEX));
            }else if (key.contains(STATUS_REGEX)) {
                Join< LeadStatus,Leads> statusRoot = leadsRoot.join("leadStatus");
                predicateList.add(toJoinPredicate(statusRoot, criteriaBuilder, criteria, STATUS_REGEX));
            }else if (key.contains(ADDRESS_REGEX)) {
                Join< AddressInformation,Leads> addressRoot = leadsRoot.join("addressInformation");
                predicateList.add(toJoinPredicate(addressRoot, criteriaBuilder, criteria, ADDRESS_REGEX));
            }
            else {
                predicateList.add(toPredicate(leadsRoot, criteriaBuilder, criteria));
            }
        }
        predicateList.add(criteriaBuilder.equal(leadsRoot.get("isDelete"),0));

        Predicate predicates = criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        query.select(criteriaBuilder.count(leadsRoot));
        query.where(predicates);

        return entityManager.createQuery(query).getSingleResult();
    }

    private Predicate toPredicate(@NonNull Root<?> root, @NonNull CriteriaBuilder builder,@NonNull  SpecSearchCriteria criteria) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> builder.like(root.get(criteria.getKey()),  criteria.getValue().toString() );
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }

    private Predicate toJoinPredicate(@NonNull Join<?,Leads> root,@NonNull  CriteriaBuilder builder,@NonNull  SpecSearchCriteria criteria, String regex) {
        String key = criteria.getKey();
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(key.replace(regex, "")), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(key.replace(regex, "")), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(key.replace(regex, "")), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(key.replace(regex, "")), criteria.getValue().toString());
            case LIKE -> builder.like(root.get(key.replace(regex, "")),   criteria.getValue().toString() );
            case STARTS_WITH -> builder.like(root.get(key.replace(regex, "")), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(key.replace(regex, "")), "%" + criteria.getValue());
            case CONTAINS -> builder.like(root.get(key.replace(regex, "")), "%" + criteria.getValue() + "%");
        };
    }

}
