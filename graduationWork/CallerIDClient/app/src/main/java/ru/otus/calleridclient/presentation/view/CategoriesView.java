package ru.otus.calleridclient.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(OneExecutionStateStrategy.class)
public interface CategoriesView extends MvpView {
    void showCategories(List<String> spamTypes);
}
