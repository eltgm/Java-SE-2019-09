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
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.presentation.presenter.MainPresenter;

public class SpamerAdapter extends RecyclerView.Adapter<SpamerAdapter.SpamerViewHolder> {
    private final MainPresenter mainPresenter;
    private List<Caller> callers = new ArrayList<>();

    public SpamerAdapter(MainPresenter presenter) {
        mainPresenter = presenter;
    }

    public void setItems(Collection<Caller> callers) {
        this.callers.clear();
        this.callers.addAll(callers);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SpamerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spamer, parent, false);

        final SpamerViewHolder spamerViewHolder = new SpamerViewHolder(view);
        view.setOnClickListener(v -> mainPresenter.showSpamerInfo(callers.get(spamerViewHolder.getAdapterPosition())));
        return spamerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpamerViewHolder holder, int position) {
        holder.bind(callers.get(position));
    }

    @Override
    public int getItemCount() {
        return callers.size();
    }

    class SpamerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSpamerPhone)
        TextView tvSpamerPhone;
        @BindView(R.id.tvSpamerDescription)
        TextView tvSpamerDescription;

        SpamerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Caller caller) {
            tvSpamerPhone.setText(caller.getTelephoneNumber());
            tvSpamerDescription.setText(caller.getDescription());
        }
    }
}
