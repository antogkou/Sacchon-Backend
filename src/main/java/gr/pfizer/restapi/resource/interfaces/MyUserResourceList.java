package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.BadEntityException;
import gr.pfizer.restapi.exception.LoginException;
import gr.pfizer.restapi.representation.LoginRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import java.util.List;

public interface MyUserResourceList {

    @Get("json")
    public List<MyUserRepresentation> getPatientsWithoutDoctor() throws BadEntityException;

    @Post("json")
    public MyUserRepresentation login(LoginRepresentation loginRepresentation) throws LoginException;
}
