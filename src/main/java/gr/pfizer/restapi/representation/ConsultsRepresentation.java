package gr.pfizer.restapi.representation;

import gr.pfizer.restapi.model.Consults;
import gr.pfizer.restapi.model.MyUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ConsultsRepresentation {
    private String consultText;
    private String uri;
    private String medication;
    private Date consult_date;
    private double dosage;
    private String email;
    private String patient_email;
    private long consult_id;

    public ConsultsRepresentation(
            Consults consults){
        if(consults != null){
            consult_id = consults.getConsult_id();
            consultText = consults.getConsultText();
            medication = consults.getMedication();
            dosage = consults.getDosage();
            consult_date = consults.getConsult_created_date();
            patient_email = consults.getPatient_email();
            uri = "http://localhost:9000/v1/team6/sacchon/consult/" + consults.getConsult_id();
        }

    }

    public Consults createConsult(){
        Consults consults = new Consults();
        consults.setConsultText(consultText);
        consults.setDosage(dosage);
        consults.setMedication(medication);
        consults.setMyUser(new MyUser());
        return consults;
    }

}
