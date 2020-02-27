package ru.otus.calleridclient.presentation.view;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

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

        requestPermissions();

        initView();
    }

    private void requestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG};
                requestPermissions(permissions, 1);
            }
        }

        NotificationManager notificationManager =
                (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }
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
