package restclient;

public class DroneOrder extends DroneOrderCreate {
    private String droneOrderId;
    private int status;

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
}
