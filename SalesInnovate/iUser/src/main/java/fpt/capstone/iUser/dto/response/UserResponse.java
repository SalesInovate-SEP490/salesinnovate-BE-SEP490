package fpt.capstone.iUser.dto.response;

import fpt.capstone.iUser.model.Role;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createDate;
    private List<RoleResponse> roles ;
}
