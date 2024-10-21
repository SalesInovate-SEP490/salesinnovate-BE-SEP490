package fpt.capstone.SalesInnovate.iLead.dto.request;

import fpt.capstone.SalesInnovate.iLead.dto.response.LeadResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
public class LeadDTO {

    private Long leadId;
    private String userId;
    private Long leadSourceId;
    private Long industryId;
    private Long leadStatusID;
    private Long leadRatingId;
    private AddressInformationDTO addressInformation ;
    private Long leadSalutionId ;
    private String firstName;
    private String lastName;
    private String middleName;
    private Integer gender;
    private String title;
    private String email;
    private String phone;
    private String website;
    private String company;
    private Integer noEmployee;
    private String createdBy;
    private LocalDateTime createDate;
    private LocalDateTime editDate;
    private String editBy;
    private Integer isDelete;

}
