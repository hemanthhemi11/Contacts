package com.example.contacts.injection.component;

import android.app.Application;
import android.content.Context;

import com.example.contacts.ContactsApplication;
import com.example.contacts.data.PreferenceManager;
import com.example.contacts.injection.module.AppModule;
import com.example.contacts.services.ApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    void inject(ContactsApplication app);

    Context context();

    Application application();

    PreferenceManager getPreferenceManager();

    ApiService apiService();

}
