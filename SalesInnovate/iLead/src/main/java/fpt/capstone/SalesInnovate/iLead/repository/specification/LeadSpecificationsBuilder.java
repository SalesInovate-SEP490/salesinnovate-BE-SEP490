package fpt.capstone.SalesInnovate.iLead.repository.specification;

import fpt.capstone.SalesInnovate.iLead.model.Leads;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static fpt.capstone.SalesInnovate.iLead.repository.specification.SearchOperation.*;

public class LeadSpecificationsBuilder {
    public final List<SpecSearchCriteria> params;

    public LeadSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    // API
    public LeadSpecificationsBuilder with(final String key, final String operation, final Object value) {
        return with(null, key, operation, value);
    }

    public LeadSpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (searchOperation != null) {
            params.add(new SpecSearchCriteria(orPredicate, key, searchOperation, value));
        }
        return this;
    }

//    public Specification<Leads> build() {
//        if (params.isEmpty())
//            return null;
//
//        Specification<Leads> result = new LeadSpecification(params.get(0));
//
//        for (int i = 1; i < params.size(); i++) {
//            result = params.get(i).isOrPredicate()
//                    ? Specification.where(result).or(new LeadSpecification(params.get(i)))
//                    : Specification.where(result).and(new LeadSpecification(params.get(i)));
//        }
//
//        return result;
//    }

//    public LeadSpecificationsBuilder with(LeadSpecification spec) {
//        params.add(spec.getCriteria());
//        return this;
//    }
//
//    public LeadSpecificationsBuilder with(SpecSearchCriteria criteria) {
//        params.add(criteria);
//        return this;
//    }
}

