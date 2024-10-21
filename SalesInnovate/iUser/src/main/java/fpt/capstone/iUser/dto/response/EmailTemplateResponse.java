package fpt.capstone.iUser.dto.response;

import fpt.capstone.iUser.model.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
public class EmailTemplateResponse implements Serializable {
    private Long emailTemplateId;
    private String sendTo;
    private String mailSubject;
    private String htmlContent;
    private Date createDate;
    private int isDeleted;
    private Date deleteDate;
}
