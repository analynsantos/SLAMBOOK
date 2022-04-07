package com.example.slambook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    Context context;
    int layout;
    ArrayList<ForIndividualEntry_Data> dataList = new ArrayList<>();
    OnConvertViewClickListener listen;
    SQLiteDBHelper myDB;

    public RecyclerviewAdapter(Context context, int layout, ArrayList<ForIndividualEntry_Data> dataList) {
        this.context = context;
        this.layout = layout;
        this.dataList = dataList;
        myDB = new SQLiteDBHelper(context);
    }

    @NonNull
    @Override
    public RecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = inflater.inflate(layout,parent,false);
        ViewHolder vh = new ViewHolder(convertView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        ForIndividualEntry_Data data = dataList.get(position);
        vh.profile.setImageURI(Uri.parse(data.getPersonPicture()));
        vh.name.setText(data.getFirstname() + " " + data.getMiddlename() + " " + data.getLastname());
        vh.remark.setText(data.getRemark());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;
        TextView remark;
        public ViewHolder (@NonNull View convertView){
            super(convertView);
            this.profile = itemView.findViewById(R.id.profile);
            this.name = itemView.findViewById(R.id.completeName);
            this.remark = itemView.findViewById(R.id.remarkDisplay);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listen.ConvertViewOnClick(position);
                }
            });

            convertView.findViewById(R.id.editButton_rv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: UPDATE DATABASE.
                    Intent i = new Intent(context, Activity6_EditEntry.class);
                    i.putExtra("NoteID", getAdapterPosition());
                    i.putExtra("EntryID", dataList.get(getAdapterPosition()).getEntryID());
                    i.putExtra("Picture", dataList.get(getAdapterPosition()).getPersonPicture());
                    i.putExtra("LastName", dataList.get(getAdapterPosition()).getLastname());
                    i.putExtra("FirstName", dataList.get(getAdapterPosition()).getFirstname());
                    i.putExtra("MiddleName", dataList.get(getAdapterPosition()).getMiddlename());
                    i.putExtra("Remark", dataList.get(getAdapterPosition()).getRemark());
                    i.putExtra("Birthday", dataList.get(getAdapterPosition()).getBirthday());
                    i.putExtra("Gender", dataList.get(getAdapterPosition()).getGender());
                    i.putExtra("Address", dataList.get(getAdapterPosition()).getAddress());
                    i.putExtra("Contact", dataList.get(getAdapterPosition()).getContactNumber());
                    i.putExtra("Hobby", dataList.get(getAdapterPosition()).getHobbies());
                    i.putExtra("Info", dataList.get(getAdapterPosition()).getOtherInfo());
                    context.startActivity(i);
                }
            });

            convertView.findViewById(R.id.deleteButton_rv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("WARNING")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO: DELETE ROW IN DATABASE.
                                    myDB.deleteEntry(dataList.get(getAdapterPosition()).getEntryID());
                                    dataList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(true)
                            .create();
                    builder.show();
                }
            });
        }

    }

    public interface OnConvertViewClickListener {
        void ConvertViewOnClick (int position);
    }

    public void setOnConvertViewClickListener (OnConvertViewClickListener listener) {
        listen = listener;
    }

}
