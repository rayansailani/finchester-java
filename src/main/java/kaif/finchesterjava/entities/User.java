package kaif.finchesterjava.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String fullname;

	// @JsonDeserialize(using = TrimStringDeserializer.class)
	private String username;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String email;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String gender;

	private Long mobile;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String password;

	private Boolean isAuthorized;
	@JsonDeserialize(using = TrimStringDeserializer.class)

	private String role;
	@JsonDeserialize(using = TrimStringDeserializer.class)
	
	private String additionalFields;

	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "partner_id")
	@JsonIgnore
	private Partner partner;

	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	@JsonIgnore
	private List<Profile> profile = new ArrayList<>();

	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	@JsonIgnore
	private List<Loan> loan = new ArrayList<>();

	@ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<Bank> bank = new ArrayList<>();


	

	

}
