package ru.otus.calleridclient.models.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.otus.calleridclient.R;

public class SpamTypeAdapter extends RecyclerView.Adapter<SpamTypeAdapter.SpamViewHolder> {
    private List<String> spamTypes = new ArrayList<>();
    private boolean[] isCheckedTypes;

    public void setItems(Collection<String> spamTypes) {
        this.spamTypes.clear();
        this.spamTypes.addAll(spamTypes);
        isCheckedTypes = new boolean[spamTypes.size()];
        notifyDataSetChanged();
    }

    public List<String> getTypes() {
        List<String> checkedTypes = new ArrayList<>();
        for (int i = 0; i < spamTypes.size(); i++) {
            if (isCheckedTypes[i])
                checkedTypes.add(spamTypes.get(i));
        }
        return checkedTypes;
    }

    @NonNull
    @Override
    public SpamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categories, parent, false);
        return new SpamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpamViewHolder holder, int position) {
        holder.bind(spamTypes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return spamTypes.size();
    }

    class SpamViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cbSelectedCat)
        CheckBox cbSelectedCat;

        SpamViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String spamType, int position) {
            cbSelectedCat.setText(spamType);
            cbSelectedCat.setOnCheckedChangeListener((buttonView, isChecked)
                    -> isCheckedTypes[position] = isChecked);
        }
    }
}
