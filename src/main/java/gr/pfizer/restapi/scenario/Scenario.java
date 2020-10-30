package gr.pfizer.restapi.scenario;

import javax.persistence.EntityManager;

public class Scenario {
    private EntityManager em ;

    public Scenario(EntityManager em) {
        this.em = em;
    }
}
