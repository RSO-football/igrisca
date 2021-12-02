package rso.football.igrisca.lib;

public class IgriscaMetadata {

    private Integer igrisceId;
    private String name;
    private double longitude;
    private double latitude;

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
