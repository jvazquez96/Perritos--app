package itesm.mx.perritos.Utils;

import android.app.Application;

/**
 * Created by jorgevazquez on 5/4/17.
 */

public class CurrentUser {

    private static CurrentUser mInstance = null;

    public CurrentUser() {}

    public static synchronized CurrentUser getmInstance() {
        if (mInstance == null) {
            mInstance = new CurrentUser();
        }
        return mInstance;
    }

    private String userEmail;

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
