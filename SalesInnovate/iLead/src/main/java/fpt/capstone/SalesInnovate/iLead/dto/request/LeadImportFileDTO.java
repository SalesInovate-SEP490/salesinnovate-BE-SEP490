package fpt.capstone.SalesInnovate.iLead.dto.request;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LeadImportFileDTO {
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private String title;
    private String email;
    private String phone;
    private String website;
    private String company;
    private Integer noEmployee;

    private String leadSourceName;
    private String industryStatusName;
    private String leadStatusName;
    private String leadRatingName;
    private String street;
    private String city ;
    private String province;
    private String postalCode;
    private String country ;
    private String leadSalutionName ;
}
