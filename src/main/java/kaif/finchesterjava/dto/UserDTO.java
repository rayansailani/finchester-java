package kaif.finchesterjava.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kaif.finchesterjava.entities.TrimStringDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String fullname;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String username;

	@JsonDeserialize(using = TrimStringDeserializer.class)
	private String email;

	private Long mobile;

}
