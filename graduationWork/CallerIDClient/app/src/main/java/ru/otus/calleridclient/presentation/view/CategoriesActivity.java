package ru.otus.calleridclient.presentation.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import ru.otus.calleridclient.R;
import ru.otus.calleridclient.models.adapter.SpamTypeAdapter;
import ru.otus.calleridclient.presentation.presenter.CategoriesPresenter;

import static ru.otus.calleridclient.presentation.view.SpamerInfoActivity.CATEGORIES_DATA;

public class CategoriesActivity extends MvpAppCompatActivity implements CategoriesView {
    @InjectPresenter
    CategoriesPresenter presenter;
    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;
    private SpamTypeAdapter spamTypeAdapter = new SpamTypeAdapter();

    @ProvidePresenter
    public CategoriesPresenter getPresenter() {
        return new CategoriesPresenter(new CompositeDisposable());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);

        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(spamTypeAdapter);
    }

    @OnClick(R.id.buttonAccept)
    public void onAcceptClicked() {
        Intent data = new Intent();
        data.putExtra(CATEGORIES_DATA, new Gson().toJson(spamTypeAdapter.getTypes()));
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void showCategories(List<String> spamTypes) {
        spamTypeAdapter.setItems(spamTypes);
    }
}
