package ru.otus.calleridclient.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import ru.otus.calleridclient.R;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.adapter.ChosenSpamTypeAdapter;
import ru.otus.calleridclient.presentation.presenter.SpamerInfoPresenter;

import static ru.otus.calleridclient.presentation.view.MainActivity.SPAMER_INFO;

public class SpamerInfoActivity extends MvpAppCompatActivity implements AddSpamerView {
    static final String CATEGORIES_DATA = "categories";
    private static final int CATEGORIES = 1;
    private final ChosenSpamTypeAdapter chosenSpamTypeAdapter = new ChosenSpamTypeAdapter();
    @InjectPresenter
    SpamerInfoPresenter presenter;
    @BindView(R.id.toolbarNav)
    Toolbar toolbar;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etComments)
    EditText etComments;
    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;
    @BindView(R.id.toolbarInfo)
    TextView toolbarInfo;
    @BindView(R.id.buttonDone)
    ImageButton buttonDone;
    @BindView(R.id.buttonChangeCategories)
    Button buttonChangeCategories;
    @BindView(R.id.buttonRemoveFromSpam)
    Button buttonRemoveFromSpam;

    @ProvidePresenter
    public SpamerInfoPresenter providePresenter() {
        return new SpamerInfoPresenter(new CompositeDisposable());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spamer_info);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final String spamer = getIntent().getStringExtra(SPAMER_INFO);
        if (spamer == null)
            initViewForNewSpamer();
        else
            initViewForShowInfo(new Gson().fromJson(spamer, Caller.class));


        rvCategories.setHasFixedSize(true);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 3));
        rvCategories.setAdapter(chosenSpamTypeAdapter);
    }

    private void initViewForShowInfo(Caller caller) {
        toolbarInfo.setText("Информация о спамере");

        etPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPhoneNumber.setKeyListener(DigitsKeyListener.getInstance("1234567890+-() "));
        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(
                etPhoneNumber,
                "+7 ([000]) [000]-[00]-[00]",
                (maskFilled, extractedValue, formattedValue) -> {
                    Log.d("TAG", extractedValue);
                    Log.d("TAG", String.valueOf(maskFilled));
                }
        );

        etPhoneNumber.setHint(listener.placeholder());
        etPhoneNumber.setText(caller.getTelephoneNumber());
        etComments.setText(caller.getDescription());
        chosenSpamTypeAdapter.setItems(caller.getSpamCategories());

        etPhoneNumber.setEnabled(false);
        etComments.setEnabled(false);
        buttonDone.setVisibility(View.INVISIBLE);
        buttonChangeCategories.setVisibility(View.INVISIBLE);
        buttonRemoveFromSpam.setVisibility(View.VISIBLE);
    }

    private void initViewForNewSpamer() {
        etPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPhoneNumber.setKeyListener(DigitsKeyListener.getInstance("1234567890+-() "));
        final MaskedTextChangedListener listener = MaskedTextChangedListener.Companion.installOn(
                etPhoneNumber,
                "+7 ([000]) [000]-[00]-[00]",
                (maskFilled, extractedValue, formattedValue) -> {
                    Log.d("TAG", extractedValue);
                    Log.d("TAG", String.valueOf(maskFilled));
                }
        );

        etPhoneNumber.setHint(listener.placeholder());
        etPhoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
    }

    @OnClick(R.id.buttonDone)
    public void onButtonDoneClicked() {
        String phoneNumber = etPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            etPhoneNumber.requestFocus();
            etPhoneNumber.setError("Введите номер!");

            return;
        }

        presenter.createSpamer(Caller.builder()
                .telephoneNumber(phoneNumber)
                .spamCategories(chosenSpamTypeAdapter.getSpamTypes())
                .description(etComments.getText().toString())
                .build());
    }

    @OnClick(R.id.buttonRemoveFromSpam)
    public void onRemoveFromSpamClicked() {
        presenter.removeSpamer(new Gson().fromJson(getIntent().getStringExtra(SPAMER_INFO), Caller.class));
    }

    @OnClick(R.id.buttonChangeCategories)
    public void onChangeCategoriesClicked() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivityForResult(intent, CATEGORIES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CATEGORIES) {
            if (resultCode == RESULT_OK) {
                Type itemsListType = new TypeToken<List<String>>() {
                }.getType();
                String accessMessage = data.getStringExtra(CATEGORIES_DATA);

                chosenSpamTypeAdapter.setItems(new Gson().fromJson(accessMessage, itemsListType));
            } else {
                Toast.makeText(this, "Возникла ошибка при добавлении категорий! Попробуйте снова", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.buttonBack)
    public void onButtonBackClicked() {
        closeActivity("");
    }

    @Override
    public void closeActivity(String message) {
        if (!message.isEmpty())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
