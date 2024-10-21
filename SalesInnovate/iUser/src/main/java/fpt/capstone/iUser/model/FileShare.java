package fpt.capstone.iUser.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="file_share")
public class FileShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_share_id")
    private Long fileShareId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;
    @OneToOne
    @JoinColumn(name = "file_id")
    private FileManager fileManager;
}
