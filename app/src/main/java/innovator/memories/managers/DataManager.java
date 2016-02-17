package innovator.memories.managers;

import android.content.Intent;
import android.os.Handler;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import innovator.memories.MemoriesApp;
import innovator.memories.models.User;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SachendraSingh on 2/16/16.
 */
public class DataManager{

    public static final String USERS_UPDATED = "UsersUpdated";


    private static DataManager manager;
    private final ValueEventListener usersListener;
    private Object users;

    public static DataManager getManager(){
        if (manager == null) {
            manager = new DataManager();
        }
        return manager;
    }

    public DataManager(){
        usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue();
                MemoriesApp.context.sendBroadcast(new Intent(USERS_UPDATED));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        FireBaseManager.getManager().getUsers(usersListener);
    }

    public Object getUsers(){
        return users;
    }

}
