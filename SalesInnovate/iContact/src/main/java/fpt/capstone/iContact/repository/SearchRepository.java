package fpt.capstone.iContact.repository;

import fpt.capstone.iContact.model.AddressInformation;
import fpt.capstone.iContact.model.Contact;
import fpt.capstone.iContact.model.Salution;
import fpt.capstone.iContact.repository.specification.SpecSearchCriteria;
import fpt.capstone.iContact.util.AppConst;
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

import static fpt.capstone.iContact.util.AppConst.ADDRESS_REGEX;
import static fpt.capstone.iContact.util.AppConst.SALUTION_REGEX;

@Component
@Slf4j
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<Contact> searchUserByCriteriaWithJoin(List<SpecSearchCriteria> params, Pageable pageable) {
        log.info("searchUserByCriteriaWithJoin");

        List<Contact> contacts = getAllLeadsWithJoin(params, pageable);

        Long totalElements = countAllLeadsWithJoin(params);

        return new PageImpl<>(contacts, pageable, totalElements);
    }

    private List<Contact> getAllLeadsWithJoin(List<SpecSearchCriteria> params, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> query = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> leadsRoot = query.from(Contact.class);

        List<Predicate> predicateList = new ArrayList<>();

        for (SpecSearchCriteria criteria : params) {
            String key = criteria.getKey();
            if (key.contains(SALUTION_REGEX)) {
                Join<Salution,Contact> salutionRoot = leadsRoot.join("leadSalution");
                predicateList.add(toJoinPredicate(salutionRoot, criteriaBuilder, criteria, SALUTION_REGEX));
            }else if (key.contains(ADDRESS_REGEX)) {
                Join<AddressInformation,Contact> addressRoot = leadsRoot.join("addressInformation");
                predicateList.add(toJoinPredicate(addressRoot, criteriaBuilder, criteria, ADDRESS_REGEX));
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

    private Long countAllLeadsWithJoin(List<SpecSearchCriteria> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Contact> leadsRoot = query.from(Contact.class);

        List<Predicate> predicateList = new ArrayList<>();

        for (SpecSearchCriteria criteria : params) {
            String key = criteria.getKey();
            if (key.contains(SALUTION_REGEX)) {
                Join<Salution,Contact> salutionRoot = leadsRoot.join("leadSalution");
                predicateList.add(toJoinPredicate(salutionRoot, criteriaBuilder, criteria, SALUTION_REGEX));
            }else if (key.contains(ADDRESS_REGEX)) {
                Join<AddressInformation,Contact> addressRoot = leadsRoot.join("addressInformation");
                predicateList.add(toJoinPredicate(addressRoot, criteriaBuilder, criteria, ADDRESS_REGEX));
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

    private Predicate toJoinPredicate(@NonNull Join<?,Contact> root,@NonNull  CriteriaBuilder builder,@NonNull  SpecSearchCriteria criteria, String regex) {
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
