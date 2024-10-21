package fpt.capstone.SalesInnovate.iLead.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@AllArgsConstructor
public class LeadExportDTO {
    private Long leadId;
    private String userId;
    private Long leadSourceId;
    private Long industryId;
    private Long leadStatusID;
    private Long leadRatingId;
    private Long addressInformationId ;
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
}
