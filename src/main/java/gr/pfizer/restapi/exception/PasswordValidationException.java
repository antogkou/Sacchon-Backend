package gr.pfizer.restapi.exception;

public class PasswordValidationException extends Exception {
    public PasswordValidationException(String msg){
        super(msg);
    }
}
