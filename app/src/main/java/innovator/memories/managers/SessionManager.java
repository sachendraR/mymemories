package innovator.memories.managers;

import android.content.Context;

import innovator.memories.models.User;
import innovator.memories.utility.TinyDB;

/**
 * Created by SachendraSingh on 2/11/16.
 */
public class SessionManager {

    private static final String LOGGED_IN_USER = "loggedInUser";

    public static void initiateSessionWithUser(Context context, User user){
        new TinyDB(context).putObject(LOGGED_IN_USER, user);
    }

    public static User getLoggedInUser(Context context){
        return (User) new TinyDB(context).getObject(LOGGED_IN_USER, User.class);
    }

    public static void removeUser(Context context){
        new TinyDB(context).remove(LOGGED_IN_USER);
    }

}
