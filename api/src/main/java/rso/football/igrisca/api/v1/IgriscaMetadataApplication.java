package rso.football.igrisca.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//TODO: change url to not localhost
@OpenAPIDefinition(info = @Info(title = "Igrisca API", version = "v1",
        contact = @Contact(email = "rb2600@student.uni-lj.si"),
        license = @License(name = "dev"), description = "API for managing igrisca metadata."),
        servers = @Server(url = "http://20.85.139.182:8080/"))
@ApplicationPath("/v1")
public class IgriscaMetadataApplication extends Application {

}
