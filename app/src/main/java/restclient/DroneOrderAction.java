package restclient;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.SparseArray;

public class DroneOrderAction {
    public static final String TAG = "DRONE_ORDER_ACTION";
    public static int STOP_AND_WAIT = 0;
    public static int MOVE_TO_LOCATION = 1;
    public static int TAKE_CARGO = 2;
    public static int RELEASE_CARGO = 3;

    private static Resources resources;
    private static String packageName;
    private static final SparseArray<String> droneActions = new SparseArray<String>();
    static {
        droneActions.append(0, "STOP_AND_WAIT");
        droneActions.append(1, "MOVE_TO_LOCATION");
        droneActions.append(2, "TAKE_CARGO");
        droneActions.append(3, "RELEASE_CARGO");
    }

    public static final String getStatusName(int status) {
        String name = droneActions.get(status);
        if (resources == null) {
            Log.w(TAG, "No resources provided");
            return name;
        }
        return resources.getString(
                resources.getIdentifier("order_action_" + name, "string", packageName)
        );
    }

    public static void setContext(Context newContext) {
        if (newContext != null) {
            resources = newContext.getResources();
            packageName = newContext.getPackageName();
        }
    }

}
