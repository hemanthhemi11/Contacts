package com.example.contacts.ui.activity;


import com.example.contacts.base.BaseView;

public interface HomeActivityView extends BaseView {
    void loadList();
    void requestPermission();
    void showToast(String s);
}
