package fpt.capstone.iUser.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Builder
public class FileManagerResponse {
    private Long id;
    private Long fileCloudId;
    private String fileName;
    private int isDeleted;
}
