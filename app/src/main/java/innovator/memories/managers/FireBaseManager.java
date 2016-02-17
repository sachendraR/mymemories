package innovator.memories.managers;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import innovator.memories.BuildConfig;
import innovator.memories.models.User;
import innovator.memories.utility.StringUtility;

/**
 * Created by SachendraSingh on 2/12/16.
 */
public class FireBaseManager {

    private static final String USERS = "users";
    private static FireBaseManager manager;
    private Firebase fireBase;

    public static FireBaseManager getManager(){
        if(manager == null) manager = new FireBaseManager();
        return manager;
    }

    private FireBaseManager(){
        fireBase = new Firebase(BuildConfig.HOST);
    }

    public void registerUser(User user){
        fireBase.child(USERS).child(StringUtility.firebaseFormatKey(user.getUserEmail())).setValue(user);
    }

    public void unauth() {
        fireBase.unauth();
    }

    public void authWithOAuthToken(String google, String s, Firebase.AuthResultHandler authResultHandler) {
        fireBase.authWithOAuthToken(google, s, authResultHandler);
    }

    public void getUsers(ValueEventListener listener){
        fireBase.child(USERS).addValueEventListener(listener);
    }

}
