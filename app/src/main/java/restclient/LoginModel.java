package restclient;

import android.text.TextUtils;

public class LoginModel {
    public final String email;
    public final String password;

    LoginModel(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            throw new IllegalArgumentException("Email is empty");
        }
        if (TextUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Password is empty");
        }
        this.email = email;
        this.password = password;
    }
}
