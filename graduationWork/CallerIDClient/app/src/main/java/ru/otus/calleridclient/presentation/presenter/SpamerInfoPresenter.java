package ru.otus.calleridclient.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import ru.otus.calleridclient.di.App;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.presentation.view.AddSpamerView;
import ru.otus.calleridclient.repositories.CallerRepository;

@InjectViewState
public class SpamerInfoPresenter extends BasePresenter<AddSpamerView> {
    private final CompositeDisposable disposables;
    @Inject
    CallerRepository callerRepository;

    public SpamerInfoPresenter(CompositeDisposable disposables) {
        this.disposables = disposables;
        App.getComponent().injectSpamerPresenter(this);
    }

    public void createSpamer(Caller caller) {
        addDisposable(callerRepository
                .createCaller(caller)
                .all(aBoolean -> aBoolean)
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        System.out.println(aBoolean);
                        if (aBoolean) {
                            getViewState().closeActivity("Спамер успешно добавлен!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showError("Спамер уже существует!");
                    }
                }));
    }

    public void removeSpamer(Caller caller) {
        addDisposable(callerRepository
                .removeCaller(caller)
                .all(aBoolean -> aBoolean)
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        System.out.println(aBoolean);
                        if (aBoolean) {
                            getViewState().closeActivity("Абонент удален из списка спамеров!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

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
