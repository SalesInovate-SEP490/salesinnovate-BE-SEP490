package fpt.capstone.iOpportunity.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StageDTO {
    private Long id;
    private String stageName;
}
