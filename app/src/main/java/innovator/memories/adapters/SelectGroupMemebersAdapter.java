package innovator.memories.adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import innovator.memories.R;
import innovator.memories.models.User;

/**
 * Created by SachendraSingh on 2/17/16.
 */
public class SelectGroupMemebersAdapter extends RecyclerView.Adapter<SelectGroupMemebersAdapter.SelectGroupMemberViewHolder> {

    private final Context context;
    private ArrayList<User> users = new ArrayList();

    public SelectGroupMemebersAdapter(Context context, final HashMap users){
        this.context = context;

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                populateUserArray(users);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                notifyDataSetChanged();

            }
        }.execute((Void[]) null);

    }

    private void populateUserArray(HashMap users) {
        if(users == null) return;

        for (Object item : users.entrySet()){
            Map.Entry ent = (Map.Entry) item;
            User user = new User((String)((HashMap)ent.getValue()).get("userName"),
                    (String)((HashMap)ent.getValue()).get("email"),
                    (String)((HashMap)ent.getValue()).get("profilePicURL"));
            this.users.add(user);
        }
    }

    @Override
    public SelectGroupMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_member_item, parent);

        SelectGroupMemberViewHolder vh = new SelectGroupMemberViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(SelectGroupMemberViewHolder holder, int position) {
        final User item = this.users.get(position);
        holder.tvUserEmail.setText(item.getUserEmail());
        holder.ibRemove.setImageResource(holder.isSelected? android.R.drawable.ic_delete: android.R.drawable.ic_menu_add);
        holder.ibRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setIsSelected(!item.isSelected());
                notifyDataSetChanged();
            }
        });
        holder.itemView.setBackgroundColor(context.getResources().getColor(holder.isSelected ? android.R.color.darker_gray:android.R.color.transparent));
    }

    public ArrayList getSelectedEmails(){
        ArrayList tmpList = new ArrayList();

        for(int i = 0;i<getItemCount();i++){
            if(users.get(i).isSelected())
                tmpList.add(users.get(i));
        }

        return tmpList;
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class SelectGroupMemberViewHolder extends RecyclerView.ViewHolder{

        public TextView tvUserEmail;
        public ImageButton ibRemove;
        public boolean isSelected;
        private View itemView;

        public SelectGroupMemberViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            this.tvUserEmail = (TextView) itemView.findViewById(R.id.tvMemberEmail);
            this.ibRemove = (ImageButton) itemView.findViewById(R.id.ibRemove);

        }
    }

}
