package restclient;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.SparseArray;

public class DroneOrderStatus {
    public static final String TAG = "DRONE_ORDER_STATUS";

    public static int STARTED = 0;
    public static int ERROR = 1;
    public static int ENQUEUED = 2;
    public static int SKIPPED = 3;
    public static int DONE = 4;
    public static int TOO_FAR_GEO = 5;
    public static int HAS_LOAD = 6;
    public static int HAS_NO_LOAD = 7;
    private static Resources resources;
    private static String packageName;
    private static final SparseArray<String> orderStatuses = new SparseArray<String>();
    static {
        orderStatuses.append(0, "STARTED");
        orderStatuses.append(1, "ERROR");
        orderStatuses.append(2, "ENQUEUED");
        orderStatuses.append(3, "SKIPPED");
        orderStatuses.append(4, "DONE");
        orderStatuses.append(5, "TOO_FAR_GEO");
        orderStatuses.append(6, "HAS_LOAD");
        orderStatuses.append(7, "HAS_NO_LOAD");
    }

    public static final String getStatusName(int status) {
        String name = orderStatuses.get(status);
        if (resources == null) {
            Log.w(TAG, "No resources provided");
            return name;
        }
        return resources.getString(
                resources.getIdentifier("order_status_" + name, "string", packageName)
        );
    }


    public static void setContext(Context newContext) {
        if (newContext != null) {
            resources = newContext.getResources();
            packageName = newContext.getPackageName();
        }
    }

}
