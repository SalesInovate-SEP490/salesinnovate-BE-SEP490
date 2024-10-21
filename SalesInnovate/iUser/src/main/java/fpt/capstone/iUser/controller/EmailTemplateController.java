package fpt.capstone.iUser.controller;

import fpt.capstone.iUser.dto.request.EmailTemplateDTO;
import fpt.capstone.iUser.dto.response.EmailTemplateResponse;
import fpt.capstone.iUser.dto.response.ResponseData;
import fpt.capstone.iUser.dto.response.ResponseError;
import fpt.capstone.iUser.service.EmailTemplateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/email")
public class EmailTemplateController {
    @Autowired
    private final EmailTemplateService emailTemplateService;

    @PostMapping("/create-email-template")
    public ResponseData<?> createEmail(
            @RequestBody EmailTemplateDTO emailTemplateDTO
    ) {
        try {
            Long EmailId = emailTemplateService.createEmailTemplate(emailTemplateDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Create Email success", EmailId, 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/update-email-template")
    public ResponseData<?> updateEmail(
            @RequestBody EmailTemplateDTO emailTemplateDTO
    ) {
        try {
            Long EmailId = emailTemplateService.updateEmailTemplate(emailTemplateDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Update Email success", EmailId, 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseData<?> deleteEmail(
            @PathVariable(name = "id") Long id
    ) {
        try {
            emailTemplateService.deleteEmailTemplate(id);
            return new ResponseData<>(HttpStatus
                    .OK.value(), "Delete Email success", 1);
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseData<?> detailEmail(
            @PathVariable(name = "id") Long id
    ) {
        try {
            EmailTemplateResponse emailTemplateResponse =
                    emailTemplateService.getDetailEmailTeplate(id);
            if(emailTemplateResponse != null)
            return new ResponseData<>(1, HttpStatus
                    .OK.value(), emailTemplateResponse);
            return new ResponseData<>(1, HttpStatus
                    .NOT_FOUND.value(), "Not found email teamplate");
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/get-list-email")
    public ResponseData<?> getListEmail(
            @RequestParam String userId,
            @RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage
    ) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    emailTemplateService.getListEmailTemplateByUserId(userId, currentPage, perPage));
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), "Get list mail failed");
        }
    }

    @PostMapping("/send-mail")
    public ResponseData<?> sendEmail(
            @RequestParam("emailtemplate") Long emailTemplateId,
            @RequestParam(value  = "file", required = false) MultipartFile[] file
    ) {
        try {
            emailTemplateService.sendEmail(emailTemplateId, file);
            return new ResponseData<>(1, HttpStatus.OK.value(),"Send mail success" );
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), "Send mail failed");
        }
    }
}
