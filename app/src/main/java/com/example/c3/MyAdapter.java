package com.example.c3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private Context context;
    private ArrayList nazivListe;

    public MyAdapter(Context context, ArrayList nazivListe) {
        this.context = context;
        this.nazivListe = nazivListe;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nazivListe.setText(String.valueOf(nazivListe.get(position)));
    }

    @Override
    public int getItemCount() {
        return nazivListe.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nazivListe;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nazivListe = itemView.findViewById(R.id.textView);
        }
    }
}
