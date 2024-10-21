package fpt.capstone.SalesInnovate.iLead.dto.response;

import fpt.capstone.SalesInnovate.iLead.dto.request.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class LeadResponse implements Serializable {

    private Long leadId;
    private String userId ;
    private LeadSourceDTO source;
    private IndustryDTO industry;
    private LeadStatusDTO status;
    private LeadRatingDTO rating;
    private AddressInformationDTO addressInfor ;
    private LeadSalutionDTO salution ;
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

    @Override
    public LeadResponse clone() {
        try {
            LeadResponse clone = (LeadResponse) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
