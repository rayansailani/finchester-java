package kaif.finchesterjava.entities;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String name;

    @JsonDeserialize(using = TrimStringDeserializer.class)
    private String email;

    private Long mobile;

    @Column(length = 25000)
    private String config;
    
    private String additionalFields;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partner", orphanRemoval = true)
    @JsonIgnore
    private List<User> user = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partner", orphanRemoval = true)
    @JsonIgnore
    private List<Profile> profile = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partner", orphanRemoval = true)
    @JsonIgnore
    private List<Loan> loan = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partner", orphanRemoval = true)
    @JsonIgnore
    private List<Bank> bank = new ArrayList<>();

}
