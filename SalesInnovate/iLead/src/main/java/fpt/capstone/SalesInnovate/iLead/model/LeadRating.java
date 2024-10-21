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
@Table(name="lead_Rating")
public class LeadRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_rating_id")
    private Long leadRatingId ;
    @Column(name = "lead_rating_name")
    private String leadRatingName;
}
