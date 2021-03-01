package com.example.contacts;

import android.app.Application;
import android.content.Context;

import com.example.contacts.injection.component.AppComponent;
import com.example.contacts.injection.component.DaggerAppComponent;
import com.example.contacts.injection.module.AppModule;


public class ContactsApplication extends Application {

    private AppComponent appComponent;

    public static ContactsApplication get(Context context) {
        return (ContactsApplication) context.getApplicationContext();
    }

    public AppComponent getComponent() {
        if (appComponent == null) {

         appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
