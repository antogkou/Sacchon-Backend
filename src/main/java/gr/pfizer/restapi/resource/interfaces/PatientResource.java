package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.representation.ConsultsRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface PatientResource {

    @Get("json")
    List<ConsultsRepresentation> getAllPatientsConsults() throws NotFoundException;
}
