package restclient;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.SparseArray;

import java.util.Date;

public class Telemetry {
    public static final String TAG = "TELEMETRY_MODEL";
    public static int WAITING = 0;
    public static int TAKING_CARGO = 1;
    public static int RELEASING_CARGO = 2;
    public static int MOVING = 3;
    private static Resources resources;
    private static String packageName;
    private static final SparseArray<String> telemetryStatuses = new SparseArray<String>();
    static {
        telemetryStatuses.append(0, "WAITING");
        telemetryStatuses.append(1, "TAKING_CARGO");
        telemetryStatuses.append(2, "RELEASING_CARGO");
        telemetryStatuses.append(3, "MOVING");
    }

    public static final String getStatusName(int status) {
        String name = telemetryStatuses.get(status);
        if (resources == null) {
            Log.w(TAG, "No resources provided");
            return name;
        }
        return resources.getString(
                resources.getIdentifier("telemetry_status_" + name, "string", packageName)
        );
    }

    public static void setContext(Context newContext) {
        if (newContext != null) {
            resources = newContext.getResources();
            packageName = newContext.getPackageName();
        }
    }

    private int status;
    private double latitude;
    private double longitude;
    private double batteryCharge;
    private String deviceId;
    private Date createdAt;

    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return getStatusName(this.status);
    }

    public void setStatus(int status) {
        this.status = status;
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

    public Double getBatteryCharge() {
        return batteryCharge;
    }

    public void setBatteryCharge(double batteryCharge) {
        this.batteryCharge = batteryCharge;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
