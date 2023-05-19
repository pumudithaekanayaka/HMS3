package ati.sn0w_w01f.hms3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    ArrayList<UserItems> arrayList;
    ButtonSelectListner buttonSelectListner;

    public UserAdapter(Context context, ArrayList<UserItems> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        buttonSelectListner = (UserListActivity)context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserItems userItems = arrayList.get(position);
        holder.Firstname.setText(userItems.getFirstname());
        holder.Nic.setText(userItems.getNatid());
        holder.Contact.setText(userItems.getContact());

        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectListner.ButtonSelected(arrayList.get(holder.getAdapterPosition()));
            }
        });

        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSelectListner.ButtonSelected(arrayList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView Firstname,Nic,Contact;
        Button buttonView,buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Firstname = itemView.findViewById(R.id.text_list_first_name);
            Nic = itemView.findViewById(R.id.text_list_nic);
            Contact = itemView.findViewById(R.id.text_list_contact);

            buttonView = itemView.findViewById(R.id.buttonViewUser);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdateUser);
        }
    }
}
