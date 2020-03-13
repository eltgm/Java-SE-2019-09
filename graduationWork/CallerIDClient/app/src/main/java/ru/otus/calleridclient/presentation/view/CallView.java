package ru.otus.calleridclient.presentation.view;

import android.content.Context;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.otus.calleridclient.models.Caller;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface CallView extends MvpView {
    void showSpamer(Caller caller, Context context);

    void muteSpamer();

    void showNoInfo(Context context, String phoneNumber);

    Context getContext();
}
