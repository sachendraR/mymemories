package innovator.memories;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by SachendraSingh on 2/12/16.
 */
public class MemoriesApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        Firebase.setAndroidContext(this);

    }
}
