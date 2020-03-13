package ru.otus.calleridclient.presentation.presenter;

import androidx.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ru.otus.calleridclient.di.App;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.presentation.view.MainView;
import ru.otus.calleridclient.repositories.CallerRepository;

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {
    private final CompositeDisposable disposables;
    @Inject
    CallerRepository callerRepository;

    public MainPresenter(CompositeDisposable disposables) {
        this.disposables = disposables;
        App.getComponent().injectMainPresenter(this);
    }

    public void loadSpamers() {
        addDisposable(callerRepository.getCallers()
                .subscribeWith(new DisposableObserver<List<Caller>>() {
                    @Override
                    public void onNext(@NonNull List<Caller> callers) {
                        getViewState().showSpamers(callers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void showSpamerInfo(Caller caller) {
        getViewState().showSpamerInfo(caller);
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
