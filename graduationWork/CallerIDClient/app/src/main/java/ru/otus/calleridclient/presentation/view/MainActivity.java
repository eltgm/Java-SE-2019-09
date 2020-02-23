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
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.adapter.SpamerAdapter;
import ru.otus.calleridclient.presentation.presenter.MainPresenter;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    public static final String SPAMER_INFO = "spamer_info";
    @InjectPresenter
    MainPresenter mainPresenter;
    @BindView(R.id.rvSpamers)
    RecyclerView rvSpamers;

    private SpamerAdapter spamerAdapter;

    @ProvidePresenter
    public MainPresenter providePresenter() {
        return new MainPresenter(new CompositeDisposable());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.loadSpamers();
    }

    private void initView() {
        ButterKnife.bind(this);

        spamerAdapter = new SpamerAdapter(mainPresenter);
        rvSpamers.setHasFixedSize(true);
        rvSpamers.setLayoutManager(new LinearLayoutManager(this));
        rvSpamers.setAdapter(spamerAdapter);
    }

    @OnClick(R.id.fabAddSpamer)
    public void addSpamerOnClick() {
        Intent intent = new Intent(MainActivity.this, SpamerInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void showSpamers(List<Caller> callers) {
        spamerAdapter.setItems(callers);
    }

    @Override
    public void showSpamerInfo(Caller caller) {
        Intent intent = new Intent(MainActivity.this, SpamerInfoActivity.class);
        intent.putExtra(SPAMER_INFO, new Gson().toJson(caller));
        startActivity(intent);
    }
}
