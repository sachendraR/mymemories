package innovator.memories.managers;

import android.app.Activity;
import android.content.Intent;

import innovator.memories.AddGroupActivity;
import innovator.memories.GroupsActivity;
import innovator.memories.LoginActivity;
import innovator.memories.models.User;
import innovator.memories.utility.StringUtility;

/**
 * Created by SachendraSingh on 2/11/16.
 */
public class FlowManager {

    public static void signInComplete(Activity signInActivity, User loggedInUser){

        if(!(signInActivity instanceof LoginActivity))
            throw new IllegalArgumentException("Supplied activity is not an instance of LoginActivity");

        SessionManager.initiateSessionWithUser(signInActivity, loggedInUser);
        FireBaseManager.getManager().registerUser(loggedInUser);
        signInActivity.startActivity(new Intent(signInActivity, GroupsActivity.class));
        signInActivity.finish();

    }

    public static void logoutCompleted(Activity activity) {
        SessionManager.removeUser(activity);

        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();

    }

    public static void newGroupRequest(Activity activity){
        activity.startActivityForResult(new Intent(activity, AddGroupActivity.class), 102);
    }
}
