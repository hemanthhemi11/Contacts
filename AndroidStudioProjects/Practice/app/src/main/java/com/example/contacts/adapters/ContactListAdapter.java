package com.example.contacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contacts.R;
import com.example.contacts.model.ContactModel;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private List<ContactModel> getAllLists;
    private Context context;

    public ContactListAdapter(Context context, List<ContactModel> allList) {
        this.getAllLists = allList;
        this.context = context;
    }

    public List<ContactModel> getGetAllLists() {
        return getAllLists;
    }

    @NonNull
    @Override
    public ContactListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.MyViewHolder holder, int position) {
        holder.userName.setText(getAllLists.get(position).name);
        holder.phoneNum.setText(getAllLists.get(position).mobileNumber);
    }

    @Override
    public int getItemCount() {
        return getAllLists.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userName,phoneNum;
        MyViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.name);
            phoneNum = itemView.findViewById(R.id.number);
        }
    }
}