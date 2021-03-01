package com.example.contacts.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.fragment.app.Fragment;

import com.example.contacts.ContactsApplication;
import com.example.contacts.R;
import com.example.contacts.data.PreferenceManager;
import com.example.contacts.injection.component.ConfigPersistentComponent;
import com.example.contacts.injection.component.DaggerConfigPersistentComponent;
import com.example.contacts.injection.component.FragmentComponent;
import com.example.contacts.injection.module.FragmentModule;


import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Abstract Fragment that every other Fragment in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent are kept
 * across configuration changes.
 */
public abstract class BaseFragment extends Fragment {

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final LongSparseArray<ConfigPersistentComponent> componentsArray =
            new LongSparseArray<>();
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private Unbinder unbinder;
    private long fragmentId;
    private PreferenceManager mPref;
    private Toast toast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the FragmentComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mPref = PreferenceManager.instance(getContext());
        fragmentId = savedInstanceState != null ? savedInstanceState.getLong(KEY_FRAGMENT_ID)
                        : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent;

        if (componentsArray.get(fragmentId) == null) {

            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .appComponent(ContactsApplication.get(getActivity()).getComponent())
                    .build();
            componentsArray.put(fragmentId, configPersistentComponent);
        } else {

            configPersistentComponent = componentsArray.get(fragmentId);
        }
        FragmentComponent fragmentComponent =
                configPersistentComponent.fragmentComponent(new FragmentModule(this));
        inject(fragmentComponent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        attachView();
        return view;
    }

    protected abstract int getLayout();

    protected abstract void inject(FragmentComponent fragmentComponent);

    protected abstract void attachView();

    protected abstract void detachPresenter();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_FRAGMENT_ID, fragmentId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isChangingConfigurations()) {

            componentsArray.remove(fragmentId);
        }
        detachPresenter();
        super.onDestroy();
       /* if(unbinder!=null) {
            unbinder.unbind();
        }*/
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }

    public void finishActivity(){
        getActivity().finish();
    }

    public void showToast(int msg){
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showErrorToast(){
        showToast(R.string.some_error);
    }
}
