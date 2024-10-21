package fpt.capstone.iAccount.repository;

import fpt.capstone.iAccount.model.Account;
import fpt.capstone.iAccount.model.AccountType;
import fpt.capstone.iAccount.model.AddressInformation;
import fpt.capstone.iAccount.model.Industry;
import fpt.capstone.iAccount.repository.specification.SpecSearchCriteria;
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

import static fpt.capstone.iAccount.util.AppConst.*;



@Component
@Slf4j
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<Account> searchUserByCriteriaWithJoin(List<SpecSearchCriteria> params, Pageable pageable) {
        log.info("searchUserByCriteriaWithJoin");

        List<Account> users = getAllAccountsWithJoin(params, pageable);

        Long totalElements = countAllAccountsWithJoin(params);

        return new PageImpl<>(users, pageable, totalElements);
    }

    private List<Account> getAllAccountsWithJoin(List<SpecSearchCriteria> params, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> leadsRoot = query.from(Account.class);

        List<Predicate> predicateList = new ArrayList<>();

        for (SpecSearchCriteria criteria : params) {
            String key = criteria.getKey();
            if (key.contains(INDUSTRY_REGEX)) {
                Join<Industry,Account> industryRoot = leadsRoot.join("industry");
                predicateList.add(toJoinPredicate(industryRoot, criteriaBuilder, criteria, INDUSTRY_REGEX));
            } else if (key.contains(TYPE_REGEX)) {
                Join<AccountType,Account> ratingRoot = leadsRoot.join("accountType");
                predicateList.add(toJoinPredicate(ratingRoot, criteriaBuilder, criteria, TYPE_REGEX));
            }else if (key.contains(SHIPPING_REGEX) ) {
                Join<AddressInformation,Account> addressRoot = leadsRoot.join("shippingInformation");
                predicateList.add(toJoinPredicate(addressRoot, criteriaBuilder, criteria, SHIPPING_REGEX));
            }else if (key.contains(BILLING_REGEX) ) {
                Join<AddressInformation,Account> addressRoot = leadsRoot.join("billingInformation");
                predicateList.add(toJoinPredicate(addressRoot, criteriaBuilder, criteria, BILLING_REGEX));
            }
            else {
                predicateList.add(toPredicate(leadsRoot, criteriaBuilder, criteria));
            }
        }
        predicateList.add(criteriaBuilder.equal(leadsRoot.get("isDeleted"),0));

        Predicate predicates = criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        query.where(predicates);

        return entityManager.createQuery(query)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private Long countAllAccountsWithJoin(List<SpecSearchCriteria> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Account> leadsRoot = query.from(Account.class);

        List<Predicate> predicateList = new ArrayList<>();

        for (SpecSearchCriteria criteria : params) {
            String key = criteria.getKey();
            if (key.contains(INDUSTRY_REGEX)) {
                Join<Industry,Account> industryRoot = leadsRoot.join("industry");
                predicateList.add(toJoinPredicate(industryRoot, criteriaBuilder, criteria, INDUSTRY_REGEX));
            } else if (key.contains(TYPE_REGEX)) {
                Join<AccountType,Account> ratingRoot = leadsRoot.join("accountType");
                predicateList.add(toJoinPredicate(ratingRoot, criteriaBuilder, criteria, TYPE_REGEX));
            }else if (key.contains(SHIPPING_REGEX) ) {
                Join<AddressInformation,Account> addressRoot = leadsRoot.join("shippingInformation");
                predicateList.add(toJoinPredicate(addressRoot, criteriaBuilder, criteria, SHIPPING_REGEX));
            }else if (key.contains(BILLING_REGEX) ) {
                Join<AddressInformation,Account> addressRoot = leadsRoot.join("billingInformation");
                predicateList.add(toJoinPredicate(addressRoot, criteriaBuilder, criteria, BILLING_REGEX));
            }
            else {
                predicateList.add(toPredicate(leadsRoot, criteriaBuilder, criteria));
            }
        }
        predicateList.add(criteriaBuilder.equal(leadsRoot.get("isDeleted"),0));

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

    private Predicate toJoinPredicate(@NonNull Join<?,Account> root,@NonNull  CriteriaBuilder builder,@NonNull  SpecSearchCriteria criteria, String regex) {
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
