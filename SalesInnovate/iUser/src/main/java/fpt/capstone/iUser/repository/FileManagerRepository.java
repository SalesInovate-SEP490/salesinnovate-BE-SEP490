package fpt.capstone.iUser.repository;

import fpt.capstone.iUser.model.FileManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.annotation.Native;

public interface FileManagerRepository extends JpaRepository<FileManager, Long> {
    @Query("SELECT f FROM FileManager f, FileShare fs WHERE fs.users.userId = :userId AND f.fileId = fs.fileManager.fileId")
    Page<FileManager> findFileShareByUserId(@Param("userId") String userId, Pageable pageable);
}
