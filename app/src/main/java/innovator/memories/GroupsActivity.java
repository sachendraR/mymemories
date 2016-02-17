package innovator.memories;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import innovator.memories.adapters.GroupsPagerAdapter;
import innovator.memories.adapters.NavDrawerListAdapter;
import innovator.memories.managers.FireBaseManager;
import innovator.memories.managers.FlowManager;
import innovator.memories.managers.GoogleAuthManager;
import innovator.memories.managers.SessionManager;
import innovator.memories.models.NavDrawerItem;
import innovator.memories.models.User;

/**
 * Created by SachendraSingh on 2/11/16.
 */
public class GroupsActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ViewPager groupsPager;
    private ActionBarDrawerToggle mDrawerToggle;

    private RadioGroup groupsRG;
    private RadioButton rbMy, rbShared, rbAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_groups);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        View drawerHeader = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        User loggedInUser = SessionManager.getLoggedInUser(this);

        ((TextView)drawerHeader.findViewById(R.id.name)).setText(loggedInUser.getUserName());
        ((TextView)drawerHeader.findViewById(R.id.email)).setText(loggedInUser.getUserEmail());
        if(!TextUtils.isEmpty(loggedInUser.getUserProfileURL()))
            Picasso.with(this).load(loggedInUser.getUserProfileURL()).into((ImageView) drawerHeader.findViewById(R.id.circleView));

        mDrawerList.addHeaderView(drawerHeader);

        NavDrawerItem logoutItem = new NavDrawerItem(getString(R.string.action_logout), R.drawable.ic_logout);
        ArrayList<NavDrawerItem> navs = new ArrayList<NavDrawerItem>();
        navs.add(logoutItem);

        mDrawerList.setAdapter(new NavDrawerListAdapter(this, navs));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NavDrawerItem navItem = (NavDrawerItem) parent.getItemAtPosition(position);
                if(navItem.getTitle().equalsIgnoreCase(getString(R.string.action_logout))){
                    FireBaseManager.getManager().unauth();
                    GoogleAuthManager.getManager(GroupsActivity.this).signOut(GroupsActivity.this, new ResultCallback() {
                        @Override
                        public void onResult(Result result) {
                            if(result.getStatus().isSuccess())
                                FlowManager.logoutCompleted(GroupsActivity.this);
                        }
                    });
                }

                mDrawerLayout.closeDrawer(Gravity.LEFT);

            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        setUpViewPager();
        setUpRadioGroup();
        setUpActionTabs();

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(getString(R.string.groups_activity));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getString(R.string.groups_activity));
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.groups_activity));

    }

    private void setUpViewPager() {
        groupsPager = (ViewPager) findViewById(R.id.groupsPager);
        groupsPager.setAdapter(new GroupsPagerAdapter(this, getFragmentManager()));
        groupsPager.setCurrentItem(0);

        groupsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton)groupsRG.getChildAt(position)).setChecked(true);
//                getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpRadioGroup() {
        groupsRG = (RadioGroup) findViewById(R.id.groupsRadioGroup);
        groupsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 0;
                switch (checkedId){
                    case R.id.rbMyGroup:
                        index = 0;
                        break;
                    case R.id.rbSharedGroup:
                        index = 1;
                        break;
                    case R.id.rbAllGroup:
                        index = 2;
                        break;
                }

                groupsPager.setCurrentItem(index);

            }
        });
        ((RadioButton)groupsRG.getChildAt(0)).setChecked(true);
    }

    private void setUpActionTabs() {


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.my_groups_title)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.shared_groups_title)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.all_groups_title)));

        groupsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.groups_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        switch (item.getItemId()){
            case R.id.action_add_group:
                FlowManager.newGroupRequest(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
