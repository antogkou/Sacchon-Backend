package gr.pfizer.restapi.representation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRepresentation {
    String email;
    String password;
}
