package gr.pfizer.restapi.resource.interfaces;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import org.restlet.resource.Get;

import java.util.List;

/**
 * Interface that is about Admin panel. Consists of all the admin functionality that is necessary for the admin. Contain
 * many Get request to avoid several files which are about similar logic. The endpoints separate from each other and is
 * able to determined by the restlet by a URL parameter.
 *
 * E.g http://localhost:9000/v1/team6/sacchon/admin-panel?patient&email=test@gmail.com to get the results of the
 * getPatientData method.
 */
public interface AdminResource {

    /**
     * Retrieves patients measurements and return it to the admin. Given the users email as a URL parameter retrieves all
     * the measurements from the patient if is attached to the doctor who connected to the system. The authentication
     * of the doctor achieved by retrieving his email from the basic authentication credentials.
     *
     * @return A list that contains the measurements of a patient.
     * @throws NotFoundException
     */
    @Get("?patient")
    List<MeasurementRepresentation> getPatientData() throws NotFoundException;

    /**
     * Retrieves doctor's measurements and return it to the user. Given the users email as a URL parameter retrieves all
     * the measurements from the patient if is attached to the doctor who connected to the system. The authentication
     * of the doctor achieved by retrieving his email from the basic authentication credentials.
     *
     * @return A list that contains the consults of a doctor.
     * @throws NotFoundException
     */
    @Get("?doctor")
    List<Consults> getDoctorData() throws NotFoundException;

    /**
     * Retrieves all users(doctors or patients), that are inactive and return them as a json list. Inactive doctor
     * considered a doctor that has no consults submitted on the system in a time period. Similar the inactive patient
     * has no submitted measurements in the given period.
     * The period of inactivity is given using URL params "from" and "to".
     *
     * E.g http://localhost:9000/v1/team6/sacchon/admin-panel?inactive&from=14-04-2020&to=12-05-2020
     *
     * @return A list of the users that are inactive in a period
     * @throws NotFoundException
     */
    @Get("?inactive")
    List<MyUserRepresentation> getInactiveUsers() throws NotFoundException;

    /**
     * Retrieves patients that are able to get consult. Patients after a month of submitted measurements needs consults
     * from a doctor. In this method all patients that can be consulted are listed in the result.
     *
     * @return A list of the users that are need consult for the last month
     * @throws NotFoundException
     */
    @Get("?need-consult")
    List<MyUserRepresentation> getAllUsersWithoutConsult() throws NotFoundException;
}
