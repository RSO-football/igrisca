package rso.football.igrisca.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "igralci_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "IgralciMetadataEntity.getAll",
                        query = "SELECT igrisce FROM IgriscaMetadataEntity igrisce")
        })
public class IgriscaMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "booked")
    private boolean booked;

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

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
