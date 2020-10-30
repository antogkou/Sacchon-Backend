package gr.pfizer.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Consults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long consult_id;

//    @ManyToOne
//    @JoinColumn(name= "measurement_id")
//    private Measurement measurement;

    @OneToMany(mappedBy = "consults")
    @JsonIgnore
    private List<Measurement> measurements = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private MyUser myUser;

    private Date consult_created_date;

    @PrePersist
    protected void onCreate() {
        consult_created_date = new Date();
    }

    private String consultText;
    private String medication;
    private double dosage;

    private String patient_email;
}
