package fpt.capstone.iAccount.dto.request;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountTypeDTO {
    private Long accountTypeId;
    private String accountTypeName;
}
