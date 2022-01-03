package rso.football.igrisca.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.metrics.annotation.Timed;
import rso.football.igrisca.lib.IgriscaMetadata;
import rso.football.igrisca.models.converters.IgriscaMetadataConverter;
import rso.football.igrisca.models.entities.IgriscaMetadataEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class IgriscaMetadataBean {
    
    private Logger log = Logger.getLogger(IgriscaMetadataBean.class.getName());
    
    @Inject
    private EntityManager em;
    
    public List<IgriscaMetadata> getIgriscaMetadata(){
        TypedQuery<IgriscaMetadataEntity> query = em.createNamedQuery(
                "IgriscaMetadataEntity.getAll", IgriscaMetadataEntity.class);
        
        List<IgriscaMetadataEntity> resultList = query.getResultList();
        
        return resultList.stream().map(IgriscaMetadataConverter::toDto).collect(Collectors.toList());
    }

    @Timed
    public List<IgriscaMetadata> getIgriscaMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, IgriscaMetadataEntity.class, queryParameters).stream()
                .map(IgriscaMetadataConverter::toDto).collect(Collectors.toList());
    }

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

    public IgriscaMetadata getIgriscaMetadata(Integer id) {

        IgriscaMetadataEntity IgriscaMetadataEntity = em.find(IgriscaMetadataEntity.class, id);

        if (IgriscaMetadataEntity == null) {
            throw new NotFoundException();
        }

        IgriscaMetadata IgriscaMetadata = IgriscaMetadataConverter.toDto(IgriscaMetadataEntity);

        return IgriscaMetadata;
    }

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
