package fpt.capstone.iUser.dto.request;

import fpt.capstone.iUser.model.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileShareDTO {
    private Long fileShareId;
    private String userId;
    private Long fileId;
}
