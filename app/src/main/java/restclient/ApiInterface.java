package restclient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("auth/")
    Call<Tokens> login(@Body LoginModel loginModel);
}
