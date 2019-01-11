package restclient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("auth/")
    Call<Tokens> login(@Body LoginModel loginModel);

    @GET("auth/profile")
    Call<User> getProfile();

    @GET("drones")
    Call<List<Drone>> getDrones();

    @GET("drones/{droneId}")
    Call<Drone> getDrone(@Path("droneId") String droneId);
}
