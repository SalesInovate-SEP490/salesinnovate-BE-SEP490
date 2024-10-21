package fpt.capstone.iAccount.util;

public interface AppConst {

    String SEARCH_OPERATOR = "(\\w+?)(:|<|>)(.*)";
    String SEARCH_SPEC_OPERATOR = "(\\w+?)([:!><~*`@])(.*)";
    String SORT_BY = "(\\w+?)(:)(.*)";
    String INDUSTRY_REGEX = "industry_";
    String TYPE_REGEX = "rating_";
    String SHIPPING_REGEX = "shipping_";
    String BILLING_REGEX = "billing_";

}
