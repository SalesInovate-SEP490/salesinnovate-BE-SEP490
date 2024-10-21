package fpt.capstone.iAccount.dto.request;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountImportDTO {
    private String accountName;
    private String website;
    private String phone;
    private String description;
    private Integer noEmployee;
    private Long parentAccountId ;

    private String industryStatusName;
    private String accountTypeName;

    private String street;
    private String city ;
    private String province;
    private String postalCode;
    private String country ;
}
