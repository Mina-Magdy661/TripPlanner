package com.example.remindme2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.example.remindme2.POJOS.TripJC;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends  RecyclerView.Adapter<RecycleAdapter.ViewHolder> {


    List<TripJC> data = new ArrayList<>();
    Context context;
    DatabaseAdapter databaseAdapter;


    public RecycleAdapter(Context context ,  List<TripJC> data) {
        this.data = data ;
        this.context = context ;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.recycle_row, parent , false);
    databaseAdapter=new DatabaseAdapter(context);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        String DataOfName = data.get(position).getName();
        String DataOfSLocation = data.get(position).getStartPoint();
        String DataOfElocation = data.get(position).getEndPoint();
        String DataOfDate = data.get(position).getDate();
        String timeOfTheTrip=data.get(position).getTime();

        holder.txtNameTrip.setText(DataOfName);
        holder.Time.setText(timeOfTheTrip);
        holder.txtDate.setText(DataOfDate);
        holder.txtElocation.setText(DataOfElocation);
        holder.txtSlocation.setText(DataOfSLocation);

        final TripJC item = data.get(position);


        holder.Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = "http://maps.google.com/maps?" + "&daddr=" + data.get(position).getEndLatitude()
                        + "," + data.get(position).getEndLongitude();
                Intent intent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse(uri));

                context.startActivity(intent);


                if( databaseAdapter.updateTripStatus(data.get(position).getId(),"Ended")) {
                    cancelAlarm(data.get(position).getId());
                    data.remove(position);
                    Toast.makeText(context, "Your Trip Ended", Toast.LENGTH_LONG).show();
                }


                //Toast.makeText(context ,  "The Postion " + position , Toast.LENGTH_LONG).show();

            }
        });

        holder.ContainerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context , data.get(position).getName() , Toast.LENGTH_LONG).show();
            }
        });


        holder.btnPopMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context , holder.btnPopMenu) ;

                popupMenu.inflate(R.menu.popmenu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.add){


                            Intent goEdit= new Intent(context , EditTripActivity.class);
                            goEdit.putExtra("THETRIP",data.get(position));
                            context.startActivity(goEdit);

                        }
                        if (item.getItemId() == R.id.note){

                            Toast.makeText(context ,  "Note " + position , Toast.LENGTH_LONG).show();
                            Intent goNote= new Intent(context , NoteActivity.class);
                            goNote.putExtra("tripID",data.get(position).getId());
                            context.startActivity(goNote);
                        }
                        return false;
                    }
                });
                popupMenu.show();
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
        Button Start;
        Button btnPopMenu;

        CardView ContainerCardView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSlocation = itemView.findViewById(R.id.startLoction);
            txtElocation = itemView.findViewById(R.id.endLoction);
            txtDate = itemView.findViewById(R.id.dateTime);
            txtNameTrip = itemView.findViewById(R.id.NameTrip);
             Start = itemView.findViewById(R.id.btnSartTrip);
            btnPopMenu = itemView.findViewById(R.id.btnPopMenu);
           ContainerCardView = itemView.findViewById(R.id.con);
           Time=itemView.findViewById(R.id.Time);
        }
    }


    private void cancelAlarm(int tripId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tripId, intent, 0);

        alarmManager.cancel(pendingIntent);
       // Toast.makeText(context,"AlCancled",Toast.LENGTH_LONG).show();

    }
}
