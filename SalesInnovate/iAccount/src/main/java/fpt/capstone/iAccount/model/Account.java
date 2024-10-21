package fpt.capstone.iAccount.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "account_name")
    private String accountName;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "parent_account_id")
    private Long parentAccountId ;
    @Column(name = "description")
    private  String description;
    @Column(name = "phone")
    private String phone;
    @Column(name = "website")
    private String website;
    @Column(name = "no_employee")
    private Integer noEmployee;
    @Column(name = "created_by")
    private String createdBy ;
    @Column(name = "create_date")
    private LocalDateTime createDate ;
    @Column(name = "edit_date")
    private LocalDateTime editDate;
    @Column(name = "edit_by")
    private String editBy ;
    @Column(name = "is_deleted")
    private Integer isDeleted ;

    @ManyToOne
    @JoinColumn(name = "account_type_id")
    private AccountType accountType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_information_id")
    private AddressInformation billingInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_information_id")
    private AddressInformation shippingInformation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "industry_id")
    private Industry industry;

}
