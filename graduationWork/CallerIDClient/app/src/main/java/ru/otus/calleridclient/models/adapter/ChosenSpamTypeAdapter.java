package ru.otus.calleridclient.models.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.calleridclient.R;

public class ChosenSpamTypeAdapter extends RecyclerView.Adapter<ChosenSpamTypeAdapter.ChosenSpamViewHolder> {
    private List<String> spamTypes = new ArrayList<>();

    public void setItems(Collection<String> spamTypes) {
        this.spamTypes.clear();
        this.spamTypes.addAll(spamTypes);
        notifyDataSetChanged();
    }

    public List<String> getSpamTypes() {
        return spamTypes;
    }

    @NonNull
    @Override
    public ChosenSpamTypeAdapter.ChosenSpamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chosen_categories, parent, false);
        return new ChosenSpamTypeAdapter.ChosenSpamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChosenSpamTypeAdapter.ChosenSpamViewHolder holder, int position) {
        holder.bind(spamTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return spamTypes.size();
    }

    class ChosenSpamViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;

        ChosenSpamViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String spamType) {
            tvCategoryName.setText(spamType);
        }
    }
}
