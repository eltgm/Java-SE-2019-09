package ru.otus.calleridclient.presentation.presenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ru.otus.calleridclient.di.App;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.presentation.view.CallView;
import ru.otus.calleridclient.repositories.CallerRepository;

public class CallPresenter extends BasePresenter<CallView> {
    private final CompositeDisposable disposables;
    private final CallView callView;
    @Inject
    CallerRepository callerRepository;

    public CallPresenter(CompositeDisposable disposables, CallView callView) {
        this.disposables = disposables;
        this.callView = callView;
        App.getComponent().injectCallPresenter(this);
    }

    public void findUser(String phoneNumber) {
        addDisposable(callerRepository.getCallerByIdFromDB(phoneNumber)
                .doOnNext(caller -> caller.setFromDB(true))
                .switchIfEmpty(callerRepository.getCallerByIdFromNetwork(phoneNumber))
                .defaultIfEmpty(Caller.builder().build())
                .subscribeWith(new DisposableObserver<Caller>() {
                    @Override
                    public void onNext(Caller caller) {
                        if (caller.getTelephoneNumber() != null) {
                            if (caller.isFromDB()) {
                                callView.muteSpamer();
                            } else {
                                callView.showSpamer(caller, callView.getContext());
                            }
                        } else {
                            callView.showNoInfo(callView.getContext(), phoneNumber);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
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
