package fpt.capstone.iAccount.controller;

import fpt.capstone.iAccount.dto.request.AccountDTO;
import fpt.capstone.iAccount.dto.response.ResponseData;
import fpt.capstone.iAccount.dto.response.ResponseError;
import fpt.capstone.iAccount.model.Account;
import fpt.capstone.iAccount.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private final AccountService accountService;

    @PostMapping("/create-account")
    public ResponseData<?> createAccount(@RequestBody AccountDTO accountDTO)
    {
        try{
            long account = accountService.createAccount(accountDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Create account",account, 1);
        }
        catch (Exception e){
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Create account failed");
        }
    }

    @PatchMapping("/patch-account/{id}")
    public ResponseData<?> patchAccount(@RequestBody AccountDTO accountDTO, @PathVariable(name = "id") long id)
    {
        return accountService.patchAccount(accountDTO,id)?
             new ResponseData<>(HttpStatus.OK.value(), "Update Account success", 1):
         new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update Account fail");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseData<?> deleteAccount( @PathVariable(name = "id") Long id)
    {
            return accountService.deleteAccount(id)?
             new ResponseData<>(HttpStatus
                    .OK.value(), "Delete Account success", 1):
         new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), "Delete Account fail");
    }

    @GetMapping("/detail/{id}")
    public ResponseData<?> detailAccount(@PathVariable(name = "id") Long id) {
        try {
            return new ResponseData<>(1, HttpStatus
                    .OK.value(), accountService.getDetailAccount(id));
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/get-list-account")
    public ResponseData<?> getListAccount(
            @RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage
    ) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    accountService.getListAccount(currentPage, perPage));
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), "Get list account failed");
        }
    }

    @PostMapping("/convert-new")
    public ResponseData<?> convertNewFromLead(@RequestParam long leadId, @RequestParam String accountName) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Convert Account success",
                    accountService.convertNewAccount(leadId, accountName), 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PatchMapping("/patch-list-account")
    public ResponseData<?> patchListAccount(@RequestParam Long[] id, @RequestBody AccountDTO accountDTO) {
        return accountService.patchListAccount(id, accountDTO) ?
                new ResponseData<>(1, HttpStatus.OK.value(), "Update List account success") :
                new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update account fail");
    }

    @GetMapping("/account-filter")
    public ResponseData<?> filterLeadsWithSpecifications(Pageable pageable,
                                                         @RequestParam(required = false) String[] search) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    accountService.filterAccount(pageable, search));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list filter account fail");
        }
    }

    @GetMapping("/list-account-type")
    public ResponseData<?> getListAccountType(){
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    accountService.getListType());
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list type account fail");
        }
    }
    @PostMapping("/upload-account-data")
    public ResponseData<?> uploadCustomersData(@RequestParam("file") MultipartFile file,
                                               @RequestParam("userId") String userId) {
        try {
            accountService.importFileAccount(file,userId);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Import file success", 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }
    @GetMapping("/export-file")
    public ResponseEntity<Resource> getFileExport() throws IOException {
        String filename = "accounts.xlsx";
        ByteArrayInputStream fileExport = accountService.getExportFileData();
        InputStreamResource file = new InputStreamResource(fileExport);

        ResponseEntity<Resource> response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);

        // Debugging headers
        HttpHeaders headers = response.getHeaders();
        System.out.println("Content-Disposition: " + headers.get(HttpHeaders.CONTENT_DISPOSITION));
        System.out.println("Content-Type: " + headers.get(HttpHeaders.CONTENT_TYPE));

        return response;
    }
}
