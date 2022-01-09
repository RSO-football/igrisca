package rso.football.igrisca.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rso.football.igrisca.lib.IgriscaMetadata;
import rso.football.igrisca.models.converters.IgriscaMetadataConverter;
import rso.football.igrisca.models.entities.IgriscaMetadataEntity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class IgriscaMetadataBean {
    
    private Logger log = Logger.getLogger(IgriscaMetadataBean.class.getName());
    
    @Inject
    private EntityManager em;

    private Client httpClient;

    @PostConstruct
    private void init() {
        String uniqueID = UUID.randomUUID().toString();
        log.info("Inicializacija zrna: " + IgriscaMetadata.class.getSimpleName() + " id: " + uniqueID);

        httpClient = ClientBuilder.newClient();
    }


    public List<IgriscaMetadata> getIgriscaMetadata(){
        TypedQuery<IgriscaMetadataEntity> query = em.createNamedQuery(
                "IgriscaMetadataEntity.getAll", IgriscaMetadataEntity.class);
        
        List<IgriscaMetadataEntity> resultList = query.getResultList();
        
        return resultList.stream().map(IgriscaMetadataConverter::toDto).collect(Collectors.toList());
    }

    @Timed(name = "get_igrisca_metadata_filter")
    public List<IgriscaMetadata> getIgriscaMetadataFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, IgriscaMetadataEntity.class, queryParameters).stream()
                .map(IgriscaMetadataConverter::toDto).collect(Collectors.toList());
    }

    @Metered(name = "get-igrisca-id-metadata")
    public String getIgriscaIdMetadata() {
        List<IgriscaMetadata> igriscaMetadata = getIgriscaMetadata();

        String result = "";
        for (IgriscaMetadata u : igriscaMetadata){
            result += u.getIgrisceId()+",";
        }
        if (result.length() > 0){
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    @Metered(name = "get-one-igrisce")
    public IgriscaMetadata getIgriscaMetadata(Integer id) {

        IgriscaMetadataEntity IgriscaMetadataEntity = em.find(IgriscaMetadataEntity.class, id);

        if (IgriscaMetadataEntity == null) {
            throw new NotFoundException();
        }

        IgriscaMetadata IgriscaMetadata = IgriscaMetadataConverter.toDto(IgriscaMetadataEntity);

        return IgriscaMetadata;
    }

    @Metered(name = "create_igrisce")
    public IgriscaMetadata createIgriscaMetadata(IgriscaMetadata IgriscaMetadata) {

        IgriscaMetadataEntity IgriscaMetadataEntity = IgriscaMetadataConverter.toEntity(IgriscaMetadata);

        try {
            beginTx();
            em.persist(IgriscaMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (IgriscaMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return IgriscaMetadataConverter.toDto(IgriscaMetadataEntity);
    }

    public String getSlikaIgriscaString(Integer igriscaMetadataId){
        String slika = null;
        try {
            slika = getSlikaIgrisca(igriscaMetadataId);
            if (slika == null || slika.length() < 80){
                slika = getSlikaIgrisca2(igriscaMetadataId);
            }
            slika = slika.replace("jsonFlickrApi(", "");
            slika = slika.substring(0, slika.length()-1);

            JSONObject jsonObjectSlike = new JSONObject(slika);
            JSONObject all_photos = jsonObjectSlike.getJSONObject("photos");
//            System.out.println(all_photos);
            JSONArray slike = all_photos.getJSONArray("photo");
            if (slike.length() == 0){
                return null;
            }
            JSONObject prva_slika = slike.getJSONObject(0);
            String url = "https://live.staticflickr.com/"+prva_slika.getString("server").toString()+"/"+prva_slika.getString("id").toString()+"_"+prva_slika.getString("secret").toString()+".jpg";
            System.out.println(url);
            String returnSlika = "{\"server\":"+prva_slika.getString("server").toString()+", \"idSlike\":"+prva_slika.getString("id").toString()+", \"secret\":\""+prva_slika.getString("secret").toString()+"\"}";
            System.out.println(returnSlika);
            return returnSlika;

        }catch (JSONException err){
            log.info("Error" + err.toString());
        }
        return null;
    }

    public String getSlikaIgrisca2(Integer igriscaMetadataId) {
        IgriscaMetadataEntity igriscaMetadataEntity = em.find(IgriscaMetadataEntity.class, igriscaMetadataId);

        if (igriscaMetadataEntity == null) {
            throw new NotFoundException();
        }

        IgriscaMetadata igriscaMetadata = IgriscaMetadataConverter.toDto(igriscaMetadataEntity);

        String apiKey = "5b28d8f7cff4929fef72785ddf0aa9c3";
        String url = "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key="+apiKey+"&accuracy=16&content_type=1&media=photos&geo_context=1&lat="+igriscaMetadata.getLatitude()+"&lon="+igriscaMetadata.getLongitude()+"&format=json";
        log.info("url api vreme je " + url);
        try{
            return httpClient
                    .target(url)
                    .request()
                    .get(String.class);
        } catch (WebApplicationException | ProcessingException e){
            throw new InternalServerErrorException(e);
        }

    }

    @Metered(name = "get-slika-igrisca")
    public String getSlikaIgrisca(Integer igriscaMetadataId) {
        IgriscaMetadataEntity igriscaMetadataEntity = em.find(IgriscaMetadataEntity.class, igriscaMetadataId);

        if (igriscaMetadataEntity == null) {
            throw new NotFoundException();
        }

        IgriscaMetadata igriscaMetadata = IgriscaMetadataConverter.toDto(igriscaMetadataEntity);

        String apiKey = "5b28d8f7cff4929fef72785ddf0aa9c3";
        String name = igriscaMetadata.getName();
        name = name.replace(" ", "+");
        String url = "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key="+apiKey+"&text="+name+"&accuracy=16&content_type=1&media=photos&geo_context=1&lat="+igriscaMetadata.getLatitude()+"&lon="+igriscaMetadata.getLongitude()+"&format=json";
        log.info("url api vreme je " + url);
        try{
            return httpClient
                    .target(url)
                    .request()
                    .get(String.class);
        } catch (WebApplicationException | ProcessingException e){
            throw new InternalServerErrorException(e);
        }

    }

    @Metered(name = "update_igrisce")
    public IgriscaMetadata putIgriscaMetadata(Integer id, IgriscaMetadata IgriscaMetadata) {

        IgriscaMetadataEntity c = em.find(IgriscaMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        IgriscaMetadataEntity updatedIgriscaMetadataEntity = IgriscaMetadataConverter.toEntity(IgriscaMetadata);

        try {
            beginTx();
            updatedIgriscaMetadataEntity.setId(c.getId());
            updatedIgriscaMetadataEntity = em.merge(updatedIgriscaMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return IgriscaMetadataConverter.toDto(updatedIgriscaMetadataEntity);
    }

    @Metered(name = "delete_igrisce")
    public boolean deleteIgriscaMetadata(Integer id) {

        IgriscaMetadataEntity IgriscaMetadata = em.find(IgriscaMetadataEntity.class, id);

        if (IgriscaMetadata != null) {
            try {
                beginTx();
                em.remove(IgriscaMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
