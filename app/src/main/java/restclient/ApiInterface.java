package restclient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("auth/")
    Call<Tokens> login(@Body LoginModel loginModel);

    @GET("auth/profile")
    Call<User> getProfile();

    @GET("drones")
    Call<List<Drone>> getDrones();

    @GET("drones/{droneId}")
    Call<Drone> getDrone(@Path("droneId") String droneId);

    @DELETE("drones/{droneId}")
    Call<Void> deleteDrone(@Path("droneId") String droneId);

    @GET("drone-orders/")
    Call<List<DroneOrder>> getOrders(@Query("device-ids") String deviceIds);

    @GET("drone-measurements/")
    Call<List<Telemetry>> getMeasurements(@Query("device-ids") String droneId);
}
