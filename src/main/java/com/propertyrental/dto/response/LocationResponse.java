package com.propertyrental.dto.response;

public class LocationResponse {
    private Long id;
    private String city;
    private String area;
    private String state;
    private String pincode;

    public LocationResponse() {}

    public LocationResponse(Long id, String city, String area, String state, String pincode) {
        this.id = id;
        this.city = city;
        this.area = area;
        this.state = state;
        this.pincode = pincode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
}
