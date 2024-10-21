package fpt.capstone.SalesInnovate.iLead.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressInformationDTO {
    private Long addressInformationId ;
    private String street;
    private String city ;
    private String province;
    private String postalCode;
    private String country ;
}
