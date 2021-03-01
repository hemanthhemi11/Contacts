package com.example.contacts.ui.activity;

import com.example.contacts.base.BasePresenter;

import javax.inject.Inject;


public class HomeActivityPresenter extends BasePresenter<HomeActivityView> {

    @Inject
    public HomeActivityPresenter(){

    }

    @Override
    public void attachView(HomeActivityView mvpView) {
        super.attachView(mvpView);
        if(getView()!=null){
            getView().setUp();
        }
    }
}
