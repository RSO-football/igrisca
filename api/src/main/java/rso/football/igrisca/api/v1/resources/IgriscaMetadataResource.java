package rso.football.igrisca.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
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

    @Operation(description = "Get all igrisca metadata.", summary = "Get all metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of igrisca metadata",
                    content = @Content(schema = @Schema(implementation = IgriscaMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getIgriscaMetadata(){
        List<IgriscaMetadata> igriscaMetadata = igriscaMetadataBean.getIgriscaMetadataFilter(uriInfo);
        return Response.status(Response.Status.OK).entity(igriscaMetadata).build();
    }


    @Operation(description = "Get all igrisca id.", summary = "Get all id igrisca")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "String igrisca id",
                    content = @Content(
                            schema = @Schema(implementation = String.class))
            )})
    @GET
    @Path("/igriscaId")
    public Response getTrenerjiIdMetadata() {

        String trenerjiId = igriscaMetadataBean.getIgriscaIdMetadata();

        return Response.status(Response.Status.OK).entity(trenerjiId).build();
    }

    @Operation(description = "Get metadata for one igrisce.", summary = "Get metadata for one igrisce")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Igrisce metadata",
                    content = @Content(
                            schema = @Schema(implementation = IgriscaMetadata.class))
            )})
    @GET
    @Path("/{igriscaMetadataId}")
    public Response getIgriscaMetadata(@Parameter(description = "Metadata ID.", required = true)
                                        @PathParam("igriscaMetadataId") Integer igriscaMetadataId) {

        IgriscaMetadata igriscaMetadata = igriscaMetadataBean.getIgriscaMetadata(igriscaMetadataId);

        if (igriscaMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(igriscaMetadata).build();
    }

    @Operation(description = "Add igrisce metadata.", summary = "Add metadata")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Metadata successfully added."
            ),
            @APIResponse(responseCode = "400", description = "Bad request.")
    })
    @POST
    public Response createIgriscaMetadata(@RequestBody(
            description = "DTO object with igrisca metadata.",
            required = true, content = @Content(
            schema = @Schema(implementation = IgriscaMetadata.class))) IgriscaMetadata igriscaMetadata) {

        if (igriscaMetadata.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            igriscaMetadata = igriscaMetadataBean.createIgriscaMetadata(igriscaMetadata);
        }

        return Response.status(Response.Status.CREATED).entity(igriscaMetadata).build();

    }

    @Operation(description = "Update metadata for on igrisce.", summary = "Update metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Metadata successfully updated."
            ),
            @APIResponse(responseCode = "404", description = "Not found.")
    })
    @PUT
    @Path("{igriscaMetadataId}")
    public Response putIgriscaMetadata(@Parameter(description = "Metadata ID.", required = true)
                                       @PathParam("igriscaMetadataId") Integer igriscaMetadataId,
                                       @RequestBody(
                                               description = "DTO object with igrisce metadata.",
                                               required = true, content = @Content(
                                               schema = @Schema(implementation = IgriscaMetadata.class)))
                                               IgriscaMetadata igriscaMetadata) {

        igriscaMetadata = igriscaMetadataBean.putIgriscaMetadata(igriscaMetadataId, igriscaMetadata);

        if (igriscaMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();

    }


    @Operation(description = "Delete metadata for one igrisce.", summary = "Delete metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Metadata successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{igriscaMetadataId}")
    public Response deleteIgriscaMetadata(@Parameter(description = "Metadata ID.", required = true)
                                              @PathParam("igriscaMetadataId") Integer igriscaMetadataId) {

        boolean deleted = igriscaMetadataBean.deleteIgriscaMetadata(igriscaMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
