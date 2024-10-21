package fpt.capstone.iAccount.dto.response;

import fpt.capstone.iAccount.dto.request.AccountTypeDTO;
import fpt.capstone.iAccount.dto.request.AddressInformationDTO;
import fpt.capstone.iAccount.dto.request.IndustryDTO;
import fpt.capstone.iAccount.model.AccountType;
import fpt.capstone.iAccount.model.AddressInformation;
import fpt.capstone.iAccount.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
public class AccountResponse implements Serializable {
    private Long accountId;
    private String accountName;
    private String userId;
    private Long parentAccountId ;
    private IndustryDTO industry;
    private AccountTypeDTO accountType;
    private String description;
    private String phone;
    private String website;
    private Integer noEmployee;
    private AddressInformation billingInformation ;
    private AddressInformation shippingInformation;
    private String postalCode;
    private String createdBy ;
    private Date createDate ;
    private Date editDate;
    private String editBy ;
    private Integer isDeleted ;


}
