package ru.otus.calleridclient.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface AddSpamerView extends MvpView {
    void closeActivity(String message);

    void showError(String message);
}
