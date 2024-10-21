package fpt.capstone.iUser.dto.response;

import fpt.capstone.iUser.model.Users;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private String id;
    private String name;

}
