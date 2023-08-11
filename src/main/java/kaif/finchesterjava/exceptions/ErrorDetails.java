package kaif.finchesterjava.exceptions;

import org.springframework.http.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDetails {

    private HttpStatus status;
    private String message;

    public ErrorDetails(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}

   

