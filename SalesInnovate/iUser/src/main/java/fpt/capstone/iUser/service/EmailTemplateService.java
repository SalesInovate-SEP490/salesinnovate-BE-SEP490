package fpt.capstone.iUser.service;

import fpt.capstone.iUser.dto.request.EmailTemplateDTO;
import fpt.capstone.iUser.dto.response.EmailTemplateResponse;
import fpt.capstone.iUser.dto.response.PageResponse;
import fpt.capstone.iUser.dto.response.ResponseData;
import org.springframework.web.multipart.MultipartFile;

public interface EmailTemplateService {
    Long createEmailTemplate(EmailTemplateDTO emailTemplateDTO);
    Long updateEmailTemplate(EmailTemplateDTO emailTemplateDTO);
    boolean deleteEmailTemplate(Long id);
    PageResponse<?> getListEmailTemplateByUserId(String userId, int pageNo, int pageSize);
    EmailTemplateResponse getDetailEmailTeplate(Long EmailTemplateId);
    void sendEmail(Long id, MultipartFile[] file);

}
