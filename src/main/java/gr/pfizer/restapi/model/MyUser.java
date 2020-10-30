package gr.pfizer.restapi.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class MyUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long user_id;

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zipCode;
    private String phoneNumber;
    private String email;
    private String password;
    private String userRole;
    private Date createdDate;
    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }

    private int hasActiveDoctor;
    private int isActive;


    //patient flow
    @OneToMany(mappedBy = "myUser")
    private List<Measurement> measurements = new ArrayList<>();

    //doctor flow
    @OneToMany(mappedBy = "myUser")
    private List<Consults> consults = new ArrayList<>();

}
