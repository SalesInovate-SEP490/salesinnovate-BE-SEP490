package fpt.capstone.SalesInnovate.iLead.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IndustryDTO {
    private Long industryId ;
    private String industryStatusName ;

}