package fpt.capstone.iUser.controller;

import fpt.capstone.iUser.dto.request.FileShareDTO;
import fpt.capstone.iUser.dto.response.FileResponse;
import fpt.capstone.iUser.dto.response.ResponseData;
import fpt.capstone.iUser.dto.response.ResponseError;
import fpt.capstone.iUser.service.FileManagerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/file-manager")
public class FileManagerController {
    private final FileManagerService fileManagerService;
    @PostMapping("/upload")
    public ResponseData<?> uploadFile(
            @RequestParam(value  = "userId") String userId,
            @RequestParam(value  = "file", required = false) MultipartFile file)
    {
        try{
            String fileName = file.getOriginalFilename();
            File templFile = File.createTempFile(file.getOriginalFilename(), null);
            file.transferTo(templFile);
            FileResponse fileResponse = fileManagerService.uploadFile(templFile,fileName,userId);
            return new ResponseData<>(HttpStatus.OK.value(),"Upload file success", fileResponse,1);
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), "Upload file failed");
        }
    }
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFileFromDrive(@PathVariable Long fileId) {
        Resource resource = fileManagerService.getFileFromDrive(fileId);

        if (resource != null && resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/get-list-file")
    public ResponseData<?> getListFile(
            @RequestParam String userId,
            @RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage
    ) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    fileManagerService.getListFileFromDrive(userId, currentPage, perPage));
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), "Get list file failed");
        }
    }
    @DeleteMapping("/delete-file-share/{id}")
    public ResponseData<?> deleteFileShare(
            @PathVariable(name = "id") Long id
    ) {
        try {
            boolean isDelete = fileManagerService.deleteFileShare(id);
            if(isDelete == true){
                return new ResponseData<>(HttpStatus
                        .OK.value(), "Delete File success", 1);
            }
            return new ResponseData<>(HttpStatus
                    .NOT_FOUND.value(), "Not exist File share", 0);
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @DeleteMapping("/delete-file/{id}")
    public ResponseData<?> deleteFile(
            @PathVariable(name = "id") Long id
    ) {
        try {
            boolean isDelete = fileManagerService.deleteFile(id);
            if(isDelete == true){
            return new ResponseData<>(HttpStatus
                    .OK.value(), "Delete File success", 1);
            }
            return new ResponseData<>(HttpStatus
                    .NOT_FOUND.value(), "Not exist File share", 0);
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @PostMapping("/share-file")
    public ResponseData<?> ShareFile(
            @RequestBody FileShareDTO fileShareDTO){
        try {
            Long fileId = fileManagerService.shareFileForOther(fileShareDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Share File success", fileId, 1);
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }
    @GetMapping("/get-list-file-share")
    public ResponseData<?> getListFileShare(
            @RequestParam String userId,
            @RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage
    ) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    fileManagerService.getListFileShare(userId, currentPage, perPage));
        } catch (Exception e) {
            return new ResponseError(0,
                    HttpStatus.BAD_REQUEST.value(), "Get list file failed");
        }
    }
}
