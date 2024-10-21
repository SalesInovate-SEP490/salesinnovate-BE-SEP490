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
@Table(name="email_template")
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_template_id")
    private Long emailTemplateId;
    @Column(name = "send_to")
    private String sendTo;
    @Column(name = "mail_subject")
    private String mailSubject;
    @Column(name = "html_content")
    private String htmlContent;
    @Column(name = "is_deleted")
    private int isDeleted;
    @Column(name = "deleted_date")
    private Date deleteDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
