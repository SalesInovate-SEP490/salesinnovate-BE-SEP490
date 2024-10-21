package fpt.capstone.iUser.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="file_manager")
public class FileManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;
    @Column(name = "file_cloud_id")
    private String fileCloudId;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "created_date")
    private Date createdDate;
}
