package rso.football.igrisca.api.v1.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

@Readiness
@ApplicationScoped
public class PoslovniHealthCheck implements HealthCheck {

    private static final String url = "https://github.com/kumuluz/kumuluzee";

    private static final Logger LOG = Logger.getLogger(PoslovniHealthCheck.class.getSimpleName());

    @Override
    public HealthCheckResponse call() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");

            if (connection.getResponseCode() == 200){
                return HealthCheckResponse.named(PoslovniHealthCheck.class.getSimpleName()).up().build();
            }
        } catch (Exception e){
            LOG.severe(e.getMessage());
        }
        return HealthCheckResponse.named(PoslovniHealthCheck.class.getSimpleName()).down().build();
    }
}
