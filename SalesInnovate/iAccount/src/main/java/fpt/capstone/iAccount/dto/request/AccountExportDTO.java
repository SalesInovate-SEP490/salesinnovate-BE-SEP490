package fpt.capstone.iAccount.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Builder
@Getter
@Setter
public class AccountExportDTO {
    private Long accountId;
    private String accountName;
    private Long parentAccountId ;
    private Long industryId;
    private Long accountTypeId;
    private String website;
    private String phone;
    private String description;
    private Integer noEmployee;
    private Long addressInformationId;
    private String createdBy ;
    private LocalDateTime createDate ;
}
