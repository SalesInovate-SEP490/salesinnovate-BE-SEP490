package fpt.capstone.iAccount.dto.request;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Builder
@Getter
@Setter
public class AccountDTO {
    private Long accountId;
    private String accountName;
    private String userId;
    private Long parentAccountId ;
    private Long industryId;
    private Long accountTypeId;
    private String website;
    private String phone;
    private String description;
    private Integer noEmployee;
    private AddressInformationDTO billingInformation ;
    private AddressInformationDTO shippingInformation;
    private String createdBy ;
    private Date createDate ;
    private Date editDate;
    private String editBy ;
    private Integer isDeleted ;

}
