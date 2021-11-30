package rso.football.igrisca.lib;

public class IgriscaMetadata {

    private Integer igrisceId;
    private String name;
    private boolean booked;

    public Integer getIgrisceId() {
        return igrisceId;
    }

    public void setIgrisceId(Integer igrisceId) {
        this.igrisceId = igrisceId;
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
