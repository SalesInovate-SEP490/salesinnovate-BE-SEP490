package fpt.capstone.SalesInnovate.iLead.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="industry")
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "industry_id")
    private Long industryId ;
    @Column(name = "industry_status_name")
    private String industryStatusName ;

//    @OneToMany(mappedBy = "industry",
//            fetch = FetchType.LAZY,
//            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
//    )
//    private List<Leads> Leadss;
}
