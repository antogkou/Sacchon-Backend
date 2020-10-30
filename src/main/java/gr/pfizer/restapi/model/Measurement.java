package gr.pfizer.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.restlet.security.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long measurement_id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private MyUser myUser;

//    @OneToMany(mappedBy = "measurement")
//    private List<Consults> consults = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "consults_id")
    private Consults consults;

    private Date measurement_created_date;

    @PrePersist
    protected void onCreate() {
        measurement_created_date = new Date();
    }

    private double glucose_level;
    private double carb_intake;

    private String patient_email;
}
