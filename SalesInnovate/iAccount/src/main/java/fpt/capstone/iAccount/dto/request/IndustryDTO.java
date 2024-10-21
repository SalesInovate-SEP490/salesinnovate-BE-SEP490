package fpt.capstone.iAccount.dto.request;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndustryDTO {
    private Long industryId;
    private String industryStatusName;

}
