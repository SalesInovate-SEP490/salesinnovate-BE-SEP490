package fpt.capstone.iUser.service;

import fpt.capstone.iUser.dto.request.FileShareDTO;
import fpt.capstone.iUser.dto.response.FileResponse;
import fpt.capstone.iUser.dto.response.PageResponse;
import fpt.capstone.iUser.model.FileShare;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

public interface FileManagerService {
     FileResponse uploadFile(File file,String fileName,String userId);
     Resource getFileFromDrive(Long fileId);
     PageResponse<?> getListFileFromDrive(String userId, int pageNo, int pageSize);
     boolean deleteFileShare(Long fileShareId);
     boolean deleteFile(Long fileId);
     Long shareFileForOther(FileShareDTO fileShareDTO);
     PageResponse<?> getListFileShare(String userId, int pageNo, int pageSize);
}
