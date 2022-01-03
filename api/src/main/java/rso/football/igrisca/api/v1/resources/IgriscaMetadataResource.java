package rso.football.igrisca.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import rso.football.igrisca.lib.IgriscaMetadata;
import rso.football.igrisca.services.beans.IgriscaMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/igrisca")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, DELETE, PUT, HEAD, OPTIONS")
public class IgriscaMetadataResource {
    
    private Logger log = Logger.getLogger(IgriscaMetadataResource.class.getName());
    
    @Inject
    private IgriscaMetadataBean igriscaMetadataBean;
    
    @Context
    protected UriInfo uriInfo;
    
    @GET
    public Response getIgriscaMetadata(){
        List<IgriscaMetadata> igriscaMetadata = igriscaMetadataBean.getIgriscaMetadataFilter(uriInfo);
        return Response.status(Response.Status.OK).entity(igriscaMetadata).build();
    }

    @GET
    @Path("/igriscaId")
    public Response getTrenerjiIdMetadata() {

        String trenerjiId = igriscaMetadataBean.getIgriscaIdMetadata();

        return Response.status(Response.Status.OK).entity(trenerjiId).build();
    }

    @GET
    @Path("/{igriscaMetadataId}")
    public Response getIgriscaMetadata(@PathParam("igriscaMetadataId") Integer igriscaMetadataId) {

        IgriscaMetadata igriscaMetadata = igriscaMetadataBean.getIgriscaMetadata(igriscaMetadataId);

        if (igriscaMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(igriscaMetadata).build();
    }

    @POST
    public Response createIgriscaMetadata(IgriscaMetadata igriscaMetadata) {

        if (igriscaMetadata.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            igriscaMetadata = igriscaMetadataBean.createIgriscaMetadata(igriscaMetadata);
        }

        return Response.status(Response.Status.CONFLICT).entity(igriscaMetadata).build();

    }

    @PUT
    @Path("{igriscaMetadataId}")
    public Response putIgriscaMetadata(@PathParam("igriscaMetadataId") Integer igriscaMetadataId,
                                     IgriscaMetadata igriscaMetadata) {

        igriscaMetadata = igriscaMetadataBean.putIgriscaMetadata(igriscaMetadataId, igriscaMetadata);

        if (igriscaMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{igriscaMetadataId}")
    public Response deleteIgriscaMetadata(@PathParam("igriscaMetadataId") Integer igriscaMetadataId) {

        boolean deleted = igriscaMetadataBean.deleteIgriscaMetadata(igriscaMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
