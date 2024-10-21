package fpt.capstone.iUser.repository;

import fpt.capstone.iUser.model.EmailTemplate;
import fpt.capstone.iUser.model.FileManager;
import fpt.capstone.iUser.model.FileShare;
import fpt.capstone.iUser.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileShareRepository extends JpaRepository<FileShare, Long> {
    @Query("SELECT f FROM FileShare f WHERE f.users.userId = :userId AND f.fileManager.fileId = :fileId")
    List<FileShare> findByUserIdOrFileId(@Param("userId") String userId,@Param("fileId") Long fileId);
    @Query("SELECT fs FROM FileShare fs WHERE fs.users.userId = :userId")
    Page<FileShare> findFileShareByUser(@Param("userId") String userId, Pageable pageable);
}
