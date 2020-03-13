package ru.otus.calleridclient.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.otus.calleridclient.models.Caller;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface MainView extends MvpView {
    void showSpamers(List<Caller> callers);

    void showSpamerInfo(Caller caller);
}
