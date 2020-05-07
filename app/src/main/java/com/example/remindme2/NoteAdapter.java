package com.example.remindme2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme2.POJOS.Note_Data;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends  RecyclerView.Adapter<NoteAdapter.ViewHolder> {


    List<Note_Data> note = new ArrayList<>();
    Context context;


    public NoteAdapter(Context context , List<Note_Data> note) {
        this.note = note ;
        this.context = context ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater  = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.note_row, parent , false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        String title = note.get(position).getTitle();



        holder.txtTitle.setText(title);

    }

    @Override
    public int getItemCount() {
        return note.size() ;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle  , txtDate , txtTime;


        CardView ContainerCardView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);

            ContainerCardView = itemView.findViewById(R.id.con);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext() , ViewContent.class);
                    i.putExtra("ID" , note.get(getAdapterPosition()).getId());
                    i.putExtra("tripID" , note.get(getAdapterPosition()).getTripId());

                    v.getContext().startActivity(i);
                }
            });
        }
    }



}
