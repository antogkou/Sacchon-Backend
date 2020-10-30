package gr.pfizer.restapi.representation;

import gr.pfizer.restapi.model.Measurement;
import gr.pfizer.restapi.model.MyUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MeasurementRepresentation {
    private long measurement_id;
    private Date created_date;
    private double glucose_level;
    private double carb_intake;
    //    private String email;
//    private String password;
    private String uri;
    private String patient_email;


    public MeasurementRepresentation(
            Measurement measurement) {
        if (measurement != null) {
            measurement_id = measurement.getMeasurement_id();
            glucose_level = measurement.getGlucose_level();
            carb_intake = measurement.getCarb_intake();
            created_date = measurement.getMeasurement_created_date();
            patient_email = measurement.getPatient_email();
            uri = "http://localhost:9000/v1/team6/sacchon/measurements/" + measurement.getMeasurement_id();
        }
    }

    public Measurement createMeasurement() {
        Measurement measurement = new Measurement();
        measurement.setMeasurement_created_date(created_date);
        measurement.setGlucose_level(glucose_level);
        measurement.setCarb_intake(carb_intake);
        measurement.setPatient_email(patient_email);
        measurement.setMyUser(new MyUser());
        return measurement;
    }
}
