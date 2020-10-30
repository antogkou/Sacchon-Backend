package gr.pfizer.restapi;

import gr.pfizer.restapi.repository.util.JpaUtil;
import gr.pfizer.restapi.router.CustomRouter;
import gr.pfizer.restapi.scenario.Scenario;
import gr.pfizer.restapi.security.Shield;
import gr.pfizer.restapi.security.cors.CorsFilter;
import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;

import javax.persistence.EntityManager;

import java.util.logging.Logger;


public class ApiMain extends Application {

    public static final Logger LOGGER = Engine.getLogger(ApiMain.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("Contacts application starting...");

        EntityManager em = JpaUtil.getEntityManager();
        Scenario scenario = new Scenario(em);

        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 9000);
        c.getDefaultHost().attach("/v1/team6/sacchon", new ApiMain());
        c.start();
//        em.close();
//        JpaUtil.shutdown();
        LOGGER.info("Team 6 Web API started");
        LOGGER.info("URL: http://localhost:9000/v1/team6/sacchon/measurements");
        LOGGER.info("URL: http://localhost:9000/v1/team6/sacchon/consult");

    }

    public ApiMain() {

        setName("TeamSixAPI");
        setDescription("Full Web API tutorial");

//        getRoles().add(new Role(this, Shield.ROLE_USER));
//        getRoles().add(new Role(this, Shield.ROLE_ADMIN));
//        getRoles().add(new Role(this, Shield.ROLE_OWNER));

//        ClientResource resource = new ClientResource("http://localhost:9000/");
//
//        // Send an authenticated request using the Basic authentication scheme.
//        resource.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "email", "password");
//        // Send the request
//        resource.get();
//        // Should be 200
//        System.out.println(resource.getStatus());

    }


    @Override
    public Restlet createInboundRoot() {

        CustomRouter customRouter = new CustomRouter(this);
        Shield shield = new Shield(this);

        Router publicRouter = customRouter.publicResources();
        ChallengeAuthenticator apiGuard = shield.createApiGuard();

        // Create the api router, protected by a guard

        Router apiRouter = customRouter.createApiRouter();
        apiGuard.setNext(apiRouter);

        publicRouter.attachDefault(apiGuard);

        // return publicRouter;

        CorsFilter corsFilter = new CorsFilter(this);
        return corsFilter.createCorsFilter(publicRouter);

        //return apiRouter;

    }
}
