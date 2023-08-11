package kaif.finchesterjava.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String accountType;

    private Long accountNo;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String ifscCode;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String bankName;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String branch;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String city;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String state;
    
    private Long aadhaarNo;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String panNo;

    @Column(length = 2000)
    private String additionalFields;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    @JsonIgnore
    private Partner partner;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @JsonIgnore
    private Profile profile;

}
