package restclient;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.SparseArray;

public class Drone {
    public static final String TAG = "DRONE_MODEL";
    public static final int UNAUTHORIZED = 0;
    public static final int OFFLINE = 0;
    public static final int IDLE = 0;
    public static final int WORKING = 0;
    private static Resources resources;
    private static String packageName;
    private static final SparseArray<String> droneStatuses = new SparseArray<String>();
    static {
        droneStatuses.append(0, "UNAUTHORIZED");
        droneStatuses.append(1, "OFFLINE");
        droneStatuses.append(2, "IDLE");
        droneStatuses.append(3, "WORKING");
    }

    public static final String getStatusName(int status) {
        String name = droneStatuses.get(status);
        if (resources == null) {
            Log.w(TAG, "No resources provided");
            return name;
        }
        return resources.getString(
                resources.getIdentifier("drone_status_" + name, "string", packageName)
        );
    }

    public static void setContext(Context newContext) {
        if (newContext != null) {
            resources = newContext.getResources();
            packageName = newContext.getPackageName();
        }
    }

    private String droneId;
    private String ownerId;
    private String deviceId;
    private int status;
    private double baseLongitude;
    private double baseLatitude;
    private double batteryPower;
    private double enginePower;
    private double loadCapacity;

    private boolean canCarryLiquids;

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusName() {
        return Drone.getStatusName(status);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getBaseLongitude() {
        return baseLongitude;
    }

    public void setBaseLongitude(double baseLongitude) {
        this.baseLongitude = baseLongitude;
    }

    public Double getBaseLatitude() {
        return baseLatitude;
    }

    public void setBaseLatitude(double baseLatitude) {
        this.baseLatitude = baseLatitude;
    }

    public Double getBatteryPower() {
        return batteryPower;
    }

    public void setBatteryPower(double batteryPower) {
        this.batteryPower = batteryPower;
    }

    public Double getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(double enginePower) {
        this.enginePower = enginePower;
    }

    public Double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }

    public Boolean canCarryLiquids() {
        return canCarryLiquids;
    }

    public void setCanCarryLiquids(boolean canCarryLiquids) {
        this.canCarryLiquids = canCarryLiquids;
    }
}
