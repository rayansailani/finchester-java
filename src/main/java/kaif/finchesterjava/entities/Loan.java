package kaif.finchesterjava.entities;

import java.util.Date;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private UUID loanId;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String reason;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String jobType;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String companyName;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String monthlyIncome;
	
	private Long loanAmount;
	private Integer loanTenure;
	private Boolean isLoanActive;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String status;

	@Temporal(TemporalType.DATE)
	private Date date;

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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profile_id")
	@JsonIgnore
	private Profile profile;

}
