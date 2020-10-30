package gr.pfizer.restapi.security.dao;



import gr.pfizer.restapi.security.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser{
   
    private String username;
    private String password;
    private Role role;


}