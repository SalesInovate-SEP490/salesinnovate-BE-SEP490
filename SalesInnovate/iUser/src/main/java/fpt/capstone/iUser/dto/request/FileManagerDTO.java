package fpt.capstone.iUser.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Builder
public class FileManagerDTO {
    private Long id;
    private String fileCloudId;
    private String fileName;
    private Date createdDate;
}
