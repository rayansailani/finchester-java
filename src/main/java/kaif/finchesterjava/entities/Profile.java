package kaif.finchesterjava.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long mobile;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String email;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String fullname;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String residence;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String address;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String city;
    private Long pincode;

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile", orphanRemoval = true)
    @JsonIgnore
    private List<Loan> loan = new ArrayList<>();

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "profile", orphanRemoval = true)
    @JsonIgnore
    private Bank bank;

}
