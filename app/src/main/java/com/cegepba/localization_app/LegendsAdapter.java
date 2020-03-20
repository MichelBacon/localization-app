package com.cegepba.localization_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LegendsAdapter extends FirestoreRecyclerAdapter<Legends, LegendsAdapter.LegendsHolder> {

    public LegendsAdapter(@NonNull FirestoreRecyclerOptions<Legends> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LegendsHolder holder, int position, @NonNull Legends model) {
        holder.textViewColor.setText(model.getColor());
        holder.textViewName.setText(model.getName());
    }

    @NonNull
    @Override
    public LegendsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.legend_item,
                parent, false);

        return new LegendsHolder(v);
    }

    class LegendsHolder extends RecyclerView.ViewHolder {
        TextView textViewColor;
        TextView textViewName;

        public LegendsHolder(View itemView) {
            super(itemView);
            textViewColor = itemView.findViewById(R.id.text_view_color);
            textViewName = itemView.findViewById(R.id.text_view_name);
        }
    }
}
