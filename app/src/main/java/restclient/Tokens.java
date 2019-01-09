package restclient;

import com.google.gson.annotations.SerializedName;

public class Tokens {
    @SerializedName("refreshToken")
    public String refreshToken;

    @SerializedName("accessToken")
    public String accessToken;
}
