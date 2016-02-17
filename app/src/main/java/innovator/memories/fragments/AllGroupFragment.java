package innovator.memories.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import innovator.memories.BaseFragment;
import innovator.memories.R;

/**
 * Created by SachendraSingh on 2/11/16.
 */
public class AllGroupFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.all_groups_layout, null);
        return rootView;
    }
}
