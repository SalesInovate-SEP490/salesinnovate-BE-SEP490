package fpt.capstone.iUser.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class EmailTemplateDTO {
    private Long id;
    private String sendTo;
    private String mailSubject;
    private String htmlContent;
    private int isDeleted;
    private String userId;
}
