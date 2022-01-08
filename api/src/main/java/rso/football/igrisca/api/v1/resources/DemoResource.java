package rso.football.igrisca.api.v1.resources;

import rso.football.igrisca.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/demo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {

    private Logger log = Logger.getLogger(DemoResource.class.getName());

    @Inject
    private RestProperties restProperties;

    @POST
    @Path("/break")
    public Response makeUnhealthy(){
        restProperties.setBroken(true);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/unbreak")
    public Response makeHealthy(){
        restProperties.setBroken(false);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/break")
    public Response getHealth(){
        return Response.status(Response.Status.OK).entity(restProperties.getBroken()).build();
    }

}
