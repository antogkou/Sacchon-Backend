package gr.pfizer.restapi.router;

import gr.pfizer.restapi.resource.*;
import gr.pfizer.restapi.resource.ConsultsListResourceImpl;
import org.restlet.Application;
import org.restlet.routing.Router;

/**
 * This class represents the routes of the project. The method createApiRouter adds routes with a restricted access and
 * needs authentication in order to get a response. The publicResources methods adds routes that can be accessed by every
 * one. This class developed to split the configuration of the logic.
 * To add a new route just insert it in the Router object and add the implemented functionality. The following table
 * gives an overview of the API structure to be more clearer for the development
 *
 *  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + functionality					+ URL						+ Verb		+
 *  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Users							+ 							+			+
 * +	login						+ "/login"					+ POST		+
 * +	register					+ "/register"				+ POST		+
 * +								+							+			+
 *  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +								+							+			+
 * + Patients						+							+			+
 * +	show his profile			+ "/user-panel"				+ GET		+
 * +	delete 						+ "/delete"					+ DEL		+
 * +	mymeasurements 				+ "/myaccount/mymeasurements+ GET		+
 * +	add measurement 			+ "/measurements"			+ POST		+
 * +	delete measurement			+ "/measurements/{id}"		+ DEL		+
 * +	update measurement			+ "/measurements/{id}"		+ PUT		+
 * +	see doctor's consults		+ "/myaccount/consults"		+ GET		+
 * +	average results     		+ "/myaccount/avg"	        + GET		+
 * +								+							+			+
 *  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Doctor							+							+			+
 * +	find users without doctor 	+ "/users-without-doctor"	+ GET		+
 * +	see his patients 			+ "/my-patients"			+ GET		+
 * +	see his patient consults	+ /my-patients          	+ 		    +
 * +	                           	+ ?consults-of-patient&email+ GET		+
 * +	add consult					+ /myaccount/consult		+ POST		+
 * +	update consult				+ /myaccount/consult/{id}	+ PUT		+
 * +	delete consult				+ /myaccount/consult/{id}	+ DEL		+
 * +	patients without consult	+ /my-patients/             +   		+
 * +								+	        without-consult	+ GET		+
 * +    see his consults            + "/doctor/my-consults      + GET       +
 * +    find measurements by        + "/patient/measurements    +           +
 * +          patient email         +      ?email=test@mail.com + GET       +
 *  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Admin							+							+			+
 * +	see all users 				+ "/get-all-users"			+ GET		+
 * +	see patient data			+ /admin-panel?patient		+ GET		+
 * +	see doctor consultations	+ /admin-panel?doctor		+ GET		+
 * +	!?patients without consult	+ /admin-panel?need-consult+ GET		+
 * +	activity over a time range  + /admin-panel?inactive&	+ GET		+
 * +                                         from=YYYY-MM-DD&   + ---       +
 * +                                         to=YYYY-MM-DD&     + ---       +
 *  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 * @version 1.0
 * @since 1.0
 */
public class CustomRouter {

    private Application application;

    /**
     * Constructor that initialized an Application(Restlet) object
     * @param application
     */
    public CustomRouter(Application application) {
        this.application = application;
    }

    /**
     * This method returns private routes in a Router object. Those need Basic Authentication to get access.
     *
     * @return Router object that contains the private endpoints of the API
     */
    public Router createApiRouter() {

        Router router = new Router(application.getContext());

        // Patient
        router.attach("/user-panel", MyUserResourceImpl.class); // GET
        router.attach("/delete", MyUserResourceImpl.class); //DEL
        router.attach("/myaccount/mymeasurements", MeasurementResourceListImpl.class); //GET
        router.attach("/myaccount/avg", AnalyticsResourceImpl.class);
        router.attach("/myaccount/consults", PatientResourceImpl.class);
        router.attach("/myaccount/measurements/", MeasurementResourceImpl.class);

        // Doctor
        router.attach("/users-without-doctor", MyUserResourceListImpl.class); // GET
        router.attach("/my-patients", DoctorResourceImpl.class); // GET
        router.attach("/my-patients/without-consult", PatientsWithoutConsultResourceImpl.class); // GET
        //test
        router.attach("/patient/measurements",MeasurementByPatientResourceImpl.class); //GET
        router.attach("/doctor/my-consults", DoctorConsultResourceImpl.class); //GET

        // Admin
        router.attach("/get-all-users", MyUserResourceAllGetImpl.class); // GET
        router.attach("/admin-panel", AdminResourceImpl.class); // GET

        //with id GET a Consult
        router.attach("/consult/{consult_id}", ConsultsResourceImpl.class);
        //without id GET ALL Consults
        router.attach("/consult", ConsultsListResourceImpl.class);
        //test
        router.attach("/consult", ConsultsResourceImpl.class);

        //GET All Measurements
        router.attach("/measurements", MeasurementResourceListImpl.class);
        //GET,DELETE,PUT by ID
        router.attach("/measurements/{id}", MeasurementResourceImpl.class);
        //POST
        router.attach("/measurements", MeasurementResourceImpl.class);

        return router;
    }

    /**
     * This method returns public routes in a Router object. Those don't need authentication to get access.
     *
     * @return Object that contains the public endpoints of the API
     */
    public Router publicResources() {
        Router router = new Router();

        router.attach("/register", MyUserResourceImpl.class);
        router.attach("/login", MyUserResourceListImpl.class);
       // router.attach("/ping", PingServerResource.class);
        return router;
    }
}
