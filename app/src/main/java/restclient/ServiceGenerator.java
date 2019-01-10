package restclient;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ServiceGenerator {
    public static String TAG = "REST_API";
    public static final String BASE_API = "/api/v1/";
    private static String baseUrl = "http://192.168.0.102:5000" + BASE_API;

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
                                .addHeader("Authorization", tokens.accessToken)
                                .build();
                        return chain.proceed(request);
                    } else {
                        Log.d(TAG, "non-auth url: " + request.url().encodedPath());
                        return chain.proceed(chain.request());
                    }
                }
            });
    private static OkHttpClient okHttpClient = okHttpClientBuilder.build();
    private static Gson gson = new GsonBuilder().create();

    private ServiceGenerator() {
    }

    public static boolean hasTokens() {
        return tokens != null;
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
        if (baseUrl.charAt(baseUrl.length() - 1) == '/') {
            baseUrl = newBaseUrl.substring(0, baseUrl.length() - 1);
        } else {
            baseUrl = newBaseUrl;
        }

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
