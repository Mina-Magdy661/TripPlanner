package com.example.remindme2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme2.Database.DatabaseAdapter;
import com.example.remindme2.Database.NoteDataBaseSQL;
import com.example.remindme2.POJOS.TripJC;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerAdapter extends  RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {


    List<TripJC> data = new ArrayList<>();
    Context context;
    DatabaseAdapter db;
    //

    DatabaseReference databaseTripReference;
    NoteDataBaseSQL noteDataBaseSQL;


    public HistoryRecyclerAdapter(Context context ,  List<TripJC> data) {
        this.data = data ;
        this.context = context ;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.recycler_history_row, parent , false);
        db=new DatabaseAdapter(context);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        String DataOfName = data.get(position).getName();
        String DataOfSLocation = data.get(position).getStartPoint();
        String DataOfElocation = data.get(position).getEndPoint();
        String DataOfDate = data.get(position).getDate();
        String timeOfTheTrip = data.get(position).getTime();
        String status = data.get(position).getStatus();

        holder.txtNameTrip.setText(DataOfName);
        holder.Time.setText(timeOfTheTrip);
        holder.txtDate.setText(DataOfDate);
        holder.txtElocation.setText(DataOfElocation);
        holder.txtSlocation.setText(DataOfSLocation);
        holder.status.setText(status);


        final TripJC item = data.get(position);


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new android.app.AlertDialog.Builder(context)
                        .setTitle("Delete Trip")
                        .setMessage("Are you sure you want to delete this Trip?")


                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                TripJC deleteItem=item;

                                if (item.getTripFId() == null) {
                                    data.remove(item);

                                    if (db.deleteTrip(deleteItem.getId()) != -1) {

                                        Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_LONG).show();

                                    }


                                }


                                else {


                                    DatabaseReference tripDatabaseRef= FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("Trips").child(deleteItem.getTripFId());
                                    tripDatabaseRef.removeValue();

                                    data.remove(item);


                                    if (db.deleteTrip(deleteItem.getId()) != -1) {

                                        Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_LONG).show();

                                    }






                                }



                                ///////


                                notifyItemRemoved(position);



                            }
                        })


                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                ///////




            }
        });

        holder.ContainerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(context, "details", Toast.LENGTH_LONG).show();


                Intent toViewNotes= new Intent(context,NotesListActivity.class);
                toViewNotes.putExtra("TRIPID",data.get(position).getId());
                context.startActivity(toViewNotes);





            }
        });


    }



    @Override
    public int getItemCount() {
        return data.size() ;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSlocation;
        TextView txtElocation ;
        TextView txtNameTrip;
        TextView txtDate,Time;
        Button deleteBtn;
        TextView status;

        CardView ContainerCardView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSlocation = itemView.findViewById(R.id.startLoction);
            txtElocation = itemView.findViewById(R.id.endLoction);
            txtDate = itemView.findViewById(R.id.dateTime);
            txtNameTrip = itemView.findViewById(R.id.NameTrip);
            deleteBtn = itemView.findViewById(R.id.btnSartTrip);
            ContainerCardView = itemView.findViewById(R.id.con);
            status=itemView.findViewById(R.id.tripStatus);

            Time=itemView.findViewById(R.id.Time);
        }
    }



}
