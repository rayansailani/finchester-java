package kaif.finchesterjava.dto;

import kaif.finchesterjava.entities.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanWithProfile {
	private Loan loan;
    private String profileEmail;
    private Long profileMobile;
}
