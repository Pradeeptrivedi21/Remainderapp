package com.dev.remainderapp.View.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.remainderapp.R;
import com.dev.remainderapp.ui.theme.localdb.EntityClass;

import java.util.List;

public class BirthdayEventAdapter extends RecyclerView.Adapter<BirthdayEventAdapter.ViewHolder> {
    Context context;
    List<EntityClass> entityClasses;

    onDeleteListner deleteListner;
    public BirthdayEventAdapter(Context context, List<EntityClass> entityClasses) {
        this.context = context;
        this.entityClasses = entityClasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.birthday_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.friendnameText.setText(entityClasses.get(position).getFriendname());
        holder.friendnotesText.setText(entityClasses.get(position).getBirthday_notes());
        holder.timeAndDateText.setText(entityClasses.get(position).getBirthdaydate() + " " + entityClasses.get(position).getEventtime());


        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListner.ondelete(entityClasses.get(position).getFriendname());
            }
        });

    }

    @Override
    public int getItemCount() {
        return entityClasses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView friendnameText,friendnotesText, timeAndDateText;
        private View toplayout;
        private ImageView img_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendnameText = (TextView) itemView.findViewById(R.id.person_name);
            friendnotesText = (TextView) itemView.findViewById(R.id.person_notes);
            timeAndDateText = (TextView) itemView.findViewById(R.id.time_and_date);
            toplayout =itemView.findViewById(R.id.toplayout);
            img_delete =itemView.findViewById(R.id.deleteButton);


        }
    }

    public void setDeleteListner(onDeleteListner deleteListner) {
        this.deleteListner = deleteListner;
    }
}


