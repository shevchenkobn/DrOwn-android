package restclient;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NetworkManager {
    private static User profile;

    public static boolean hasProfile() {
        return profile != null;
    }

    public static User getProfile() {
        return profile;
    }

    public static String TAG = "REST_API";
    public static final String BASE_API = "/api/v1/";
    private static String baseUrl = "http://192.168.0.102:5000" + BASE_API;

    public static String getBaseUrl(boolean crop) {
        return crop ? baseUrl.substring(0, baseUrl.length() - BASE_API.length() + 1) : baseUrl;
    }

    private static Tokens tokens;

    private static Retrofit retrofit;
    private static ApiInterface api;
    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!isNonAuthUrl(request)) {
                        if (tokens == null) {
                            throw new IOException("No tokens");
                        }
                        request = request.newBuilder()
                                .addHeader("Authorization", "Bearer " + tokens.accessToken)
                                .build();
                        return chain.proceed(request);
                    } else {
                        Log.d(TAG, "non-auth url: " + request.url().encodedPath());
                        return chain.proceed(chain.request());
                    }
                }
            }).addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if (request.url().encodedPath().equalsIgnoreCase(BASE_API + "auth/profile/")) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<User>() {}.getType();

                        profile = gson.fromJson(response.body().string(), type);
                    }

                    return response;
                }
            });
    private static OkHttpClient okHttpClient = okHttpClientBuilder.build();
    private static final JsonDeserializer<Boolean> anyToBoolean = new JsonDeserializer<Boolean>() {
        public Boolean deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException
        {
            if (json.isJsonPrimitive()) {
                JsonPrimitive primitive = json.getAsJsonPrimitive();
                if (primitive.isBoolean()) {
                    return primitive.getAsBoolean();
                }
                if (primitive.isNumber()) {
                    return primitive.getAsDouble() != 0;
                }
                if (primitive.isString()) {
                    String string = primitive.getAsString();
                    return Boolean.parseBoolean(string);
                }
            }
            if (json.isJsonArray()) {
                JsonArray arr = json.getAsJsonArray();
                return arr.size() != 0;
            }
            return json.isJsonObject();
        }
    };
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Boolean.class, anyToBoolean)
            .registerTypeAdapter(boolean.class, anyToBoolean)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    private NetworkManager() {
    }

    public static Call<List<Telemetry>> getMeasurements(String deviceId) {
        return getService().getMeasurements(deviceId + "," + deviceId);
    }

    public static Call<List<DroneOrder>> getOrders(String deviceId) {
        return getService().getOrders(deviceId + "," + deviceId);
    }

    public static boolean hasTokens() {
        return tokens != null;
    }

    public static boolean logout() {
        Boolean hadTokens = tokens != null;
        return !hadTokens;
    }

    public static ApiInterface getService() {
        if (retrofit == null) {
            createRetrofit();
        }
        if (api == null) {
            api = retrofit.create(ApiInterface.class);
        }
        return api;
    }

    public static void changeBaseUrl(String newBaseUrl) {
        if (TextUtils.isEmpty(newBaseUrl)) {
            throw new IllegalArgumentException("newBaseUrl is empty");
        }
        if (newBaseUrl.charAt(newBaseUrl.length() - 1) == '/') {
            baseUrl = newBaseUrl.substring(0, newBaseUrl.length() - 1);
        } else {
            baseUrl = newBaseUrl;
        }
        baseUrl += BASE_API;

        createRetrofit();
    }

    public static boolean authenticate(String email, String password) {
        try {
            retrofit2.Response<Tokens> response = getService().login(new LoginModel(email, password)).execute();
            if (response.isSuccessful()) {
                tokens = getService().login(new LoginModel(email, password)).execute().body();
                return true;
            }
            Log.w(TAG, response.errorBody().string());
        } catch (IOException err) {
            Log.e(TAG, "Error while authentication", err);
        }
        return false;
    }

    private static boolean isNonAuthUrl(Request request) {
        String method = request.method();
        String url = request.url().encodedPath();
        return url.equalsIgnoreCase(BASE_API + "auth/") && method.equalsIgnoreCase("post");
    }

    private static void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        api = null;
    }
}
