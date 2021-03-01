package com.example.contacts.injection.component;



import com.example.contacts.injection.PerActivity;
import com.example.contacts.injection.module.ActivityModule;
import com.example.contacts.ui.activity.HomeActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

     void inject(HomeActivity homeActivity);


}
