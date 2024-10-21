package fpt.capstone.iUser.dto.request;

import fpt.capstone.iUser.model.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private String id;
    private String name;

}
