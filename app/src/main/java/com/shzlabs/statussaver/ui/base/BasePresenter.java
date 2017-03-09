package com.shzlabs.statussaver.ui.base;

public class BasePresenter<T extends MvpView> {

    private T mvpView;

    public void attachView(T mvpView) {
        this.mvpView = mvpView;
    }

    public void detachView() {
        mvpView = null;
    }

    public T getMvpView() {
        return mvpView;
    }
}