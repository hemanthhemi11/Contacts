package com.example.contacts.injection.component;



import com.example.contacts.injection.PerFragment;
import com.example.contacts.injection.module.FragmentModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */

@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
}
