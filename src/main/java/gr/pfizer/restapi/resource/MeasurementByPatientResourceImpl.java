package gr.pfizer.restapi.resource;

import gr.pfizer.restapi.exception.NotFoundException;
import gr.pfizer.restapi.model.Measurement;
import gr.pfizer.restapi.model.MyUser;
import gr.pfizer.restapi.repository.ConsultsRepository;
import gr.pfizer.restapi.repository.MeasurementRepository;
import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.representation.MeasurementRepresentation;
import gr.pfizer.restapi.representation.MyUserRepresentation;
import gr.pfizer.restapi.resource.interfaces.MeasurementByPatientResource;
import org.restlet.Request;
import org.restlet.engine.Engine;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MeasurementByPatientResourceImpl extends ServerResource implements MeasurementByPatientResource {


    private MeasurementRepository measurementRepository;

    public static final Logger LOGGER = Engine.getLogger(MeasurementByPatientResourceImpl.class);

    protected void doInit(){
        LOGGER.info("Initialising measurement by patient resource starts");
        try{
            measurementRepository =
                    new MeasurementRepository(JpaUtil.getEntityManager());
        }catch (Exception e){

        }
        LOGGER.info("Initialising measurement by patient resource ends");
    }

    public List<MeasurementRepresentation> getMeasurementByPatient() throws NotFoundException {

        //String doctorEmail = Request.getCurrent().getClientInfo().getUser().toString();
        //String userEmail = "them@ser";

        try {
            String userEmail = getQueryValue("email");
            List<Measurement> measurements = measurementRepository.findAllByEmail(userEmail);
            List<MeasurementRepresentation> results = new ArrayList<>();

                measurements.forEach(measurement -> results.add(new MeasurementRepresentation(measurement)));

            return results;
        }catch (Exception e){
            throw new NotFoundException("Couldn't find the patients measurement");
        }

    }


}
