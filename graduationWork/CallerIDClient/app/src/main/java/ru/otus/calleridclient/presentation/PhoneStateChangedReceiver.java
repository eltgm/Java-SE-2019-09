package ru.otus.calleridclient.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.disposables.CompositeDisposable;
import ru.otus.calleridclient.R;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.RingTypeSingleton;
import ru.otus.calleridclient.models.adapter.ChosenSpamTypeAdapter;
import ru.otus.calleridclient.presentation.presenter.CallPresenter;
import ru.otus.calleridclient.presentation.view.CallView;
import ru.otus.calleridclient.presentation.view.SpamerInfoActivity;

public class PhoneStateChangedReceiver extends BroadcastReceiver implements CallView {
    private static WindowManager windowManager;
    private static ViewGroup windowLayout;
    private final CallPresenter callPresenter = new CallPresenter(new CompositeDisposable(), this);
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (incomingNumber != null) {
                if (!incomingNumber.isEmpty()) {
                    callPresenter.findUser(incomingNumber);
                }
            }
        } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            closeWindow(context);
        }
    }

    private void showWindow(Context context, Caller caller, String phoneNumber) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;

        windowLayout = (ViewGroup) layoutInflater.inflate(R.layout.activity_incoming_call, null);
        Button buttonSpam = windowLayout.findViewById(R.id.buttonSpam);
        TextView textView = windowLayout.findViewById(R.id.tvDescription);
        buttonSpam.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpamerInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("phoneNumber", phoneNumber);
            closeWindow(context);
            muteSpamer();
            context.startActivity(intent);
        });
        Button buttonClose = windowLayout.findViewById(R.id.buttonNotSpam);
        buttonClose.setOnClickListener(v -> closeWindow(context));

        if (caller != null) {
            textView.setText(caller.getDescription().isEmpty() ? caller.getTelephoneNumber() : caller.getDescription());

            RecyclerView rvCategories = windowLayout.findViewById(R.id.rvCategories);
            rvCategories.setHasFixedSize(true);
            rvCategories.setLayoutManager(new GridLayoutManager(context, 3));
            final ChosenSpamTypeAdapter chosenSpamTypeAdapter = new ChosenSpamTypeAdapter();
            rvCategories.setAdapter(chosenSpamTypeAdapter);
            if (caller.getSpamCategories().size() > 0)
                chosenSpamTypeAdapter.setItems(caller.getSpamCategories());
        } else {
            textView.setText("Нет информации!");
        }

        windowManager.addView(windowLayout, params);
    }

    private void closeWindow(Context context) {
        if (windowLayout != null) {

            windowManager.removeView(windowLayout);
            windowLayout = null;

        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(RingTypeSingleton.getInstance().ringerMode);
    }

    @Override
    public void showSpamer(Caller caller, Context context) {
        muteSpamer();
        showWindow(context, caller, caller.getTelephoneNumber());
    }

    @Override
    public void muteSpamer() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        RingTypeSingleton.getInstance().ringerMode = audioManager.getRingerMode();

        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    @Override
    public void showNoInfo(Context context, String phoneNumber) {
        showWindow(context, null, phoneNumber);
    }

    @Override
    public Context getContext() {
        return context;
    }
}