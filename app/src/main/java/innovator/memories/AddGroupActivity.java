package innovator.memories;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import innovator.memories.fragments.AddGroupStep1;
import innovator.memories.fragments.AddGroupStep2;
import innovator.memories.fragments.FragmentInteractionListener;

public class AddGroupActivity extends BaseActivity implements FragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getFragmentManager().beginTransaction().add(R.id.fragmentContainer, AddGroupStep1.newInstance(null, null), "Step1").addToBackStack("Step1").commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount()>1)
            getFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }
}
