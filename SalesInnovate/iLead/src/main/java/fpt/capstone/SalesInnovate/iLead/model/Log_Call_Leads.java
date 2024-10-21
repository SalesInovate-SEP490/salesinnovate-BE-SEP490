package fpt.capstone.SalesInnovate.iLead.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="log_call_leads")
public class Log_Call_Leads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long log_call_id;
    private String subject;
    private String comment;

    @ManyToOne
    @JoinColumn(name="lead_id", nullable=false)
    private Leads lead;
}
