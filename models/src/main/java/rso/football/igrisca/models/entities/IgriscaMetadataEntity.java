package rso.football.igrisca.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "igrisca_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "IgriscaMetadataEntity.getAll",
                        query = "SELECT igrisce FROM IgriscaMetadataEntity igrisce")
        })
public class IgriscaMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
