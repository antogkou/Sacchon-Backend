package gr.pfizer.restapi.resource.interfaces;


import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.EmailExistException;
import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.exception.PasswordValidationException;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface MyUserResource {

    @Get("json")
    public MyUserRepresentation getAccountDetails() throws NotFoundException;

    @Post("json")
    public MyUserRepresentation register(MyUserRepresentation myUserRepresentation) throws BadEntityException, PasswordValidationException, EmailExistException;

    @Put("json")
    public MyUserRepresentation update(MyUserRepresentation myUserRepresentation) throws BadEntityException, PasswordValidationException, NotFoundException;

    @Delete("json")
    public MyUserRepresentation delete(MyUserRepresentation myUserRepresentation) throws BadEntityException, NotFoundException;
}
