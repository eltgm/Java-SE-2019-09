package ru.otus.calleridclient.presentation.presenter;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ru.otus.calleridclient.di.App;
import ru.otus.calleridclient.models.SpamType;
import ru.otus.calleridclient.presentation.view.CategoriesView;
import ru.otus.calleridclient.repositories.CallerRepository;

@InjectViewState
public class CategoriesPresenter extends BasePresenter<CategoriesView> {
    private final CompositeDisposable disposables;
    @Inject
    CallerRepository callerRepository;

    public CategoriesPresenter(CompositeDisposable disposables) {
        this.disposables = disposables;
        App.getComponent().injectCategoriesPresenter(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getCategories();
    }

    private void getCategories() {
        addDisposable(callerRepository
                .getSpamTypes()
                .map(spamTypes -> {
                    List<String> spams = new ArrayList<>();
                    for (SpamType spamType : spamTypes) {
                        spams.add(spamType.getType());
                    }
                    return spams;
                })
                .subscribeWith(new DisposableObserver<List<String>>() {
                    @Override
                    public void onNext(@NonNull List<String> spamTypes) {
                        getViewState().showCategories(spamTypes);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.fillInStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    protected void disconnect() {
        if (!disposables.isDisposed())
            disposables.clear();
    }

    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}
