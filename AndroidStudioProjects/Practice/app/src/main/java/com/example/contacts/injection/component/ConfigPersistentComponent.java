package com.example.contacts.injection.component;



import com.example.contacts.injection.ConfigPersistent;
import com.example.contacts.injection.module.ActivityModule;
import com.example.contacts.injection.module.FragmentModule;

import dagger.Component;

/**
 * A dagger component that will live during the lifecycle of an Activity or Fragment but it won't be
 * destroy during configuration changes. Check {@link BaseActivity } and {@link BaseFragment} to see
 * how this components survives configuration changes. Use the {@link ConfigPersistent} scope to
 * annotate dependencies that need to survive configuration changes (for example Presenters).
 */

@ConfigPersistent
@Component(dependencies = AppComponent.class)
public interface ConfigPersistentComponent {
    ActivityComponent activityComponent (ActivityModule activityModule);
    FragmentComponent fragmentComponent (FragmentModule fragmentModule);
}
