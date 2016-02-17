package innovator.memories.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import innovator.memories.R;
import innovator.memories.fragments.AllGroupFragment;
import innovator.memories.fragments.MyGroupFragment;
import innovator.memories.fragments.SharedGroupFragment;

/**
 * Created by SachendraSingh on 2/11/16.
 */
public class GroupsPagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;

    public GroupsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MyGroupFragment();
            case 1:
                return new SharedGroupFragment();
            case 2:
                return new AllGroupFragment();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.my_groups_title);
            case 1:
                return context.getString(R.string.shared_groups_title);
            case 2:
                return context.getString(R.string.all_groups_title);
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
