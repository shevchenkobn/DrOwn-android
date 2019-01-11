package restclient;

public class DroneOrderCreate {
    private String deviceId;
    private int action;
    private Double latitude;
    private Double longitude;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getAction() {
        return action;
    }

    public String getActionName() {
        return DroneOrderAction.getActionName(this.action);
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
