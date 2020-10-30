package gr.pfizer.restapi.exception;

public class EmailExistException extends Exception {
    public EmailExistException(String msg){
        super(msg);
    }
}
