package restclient;

public class DroneOrder {
    private String droneOrderId;
    private int status;
    private String deviceId;
    private int action;
    private Double latitude;
    private Double longitude;

    public String getDroneOrderId() {
        return droneOrderId;
    }

    public void setDroneOrderId(String droneOrderId) {
        this.droneOrderId = droneOrderId;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return DroneOrderStatus.getStatusName(status);
    }

    public void setStatus(int status) {
        this.status = status;
    }

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
