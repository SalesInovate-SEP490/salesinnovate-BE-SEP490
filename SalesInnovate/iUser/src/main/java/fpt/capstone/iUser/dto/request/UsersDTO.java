package fpt.capstone.iUser.dto.request;

import fpt.capstone.iUser.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private String userId;
    private String userName;
    private String passWord;
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleDTO> roles ;
}
