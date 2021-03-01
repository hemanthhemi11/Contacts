package com.example.contacts.ui.activity;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.R;
import com.example.contacts.adapters.ContactListAdapter;
import com.example.contacts.base.BaseActivity;
import com.example.contacts.injection.component.ActivityComponent;
import com.example.contacts.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class HomeActivity extends BaseActivity implements HomeActivityView {

    @BindView(R.id.list)
    RecyclerView recyclerView;
    List<ContactModel> contactList;
    ContactListAdapter contactListAdapter;
    public static final int MY_PERMISSION_REQUEST_CODE = 100;
    Toast toast;

    @Inject
    HomeActivityPresenter homeActivityPresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        homeActivityPresenter.attachView(this);
    }

    @Override
    public void setUp() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            loadList();
        } else {
            requestPermission();
        }
    }

    @Override
    public void loadList() {
        contactList = new ArrayList<>();
        contactList = getAllContacts();
        contactListAdapter = new ContactListAdapter(getApplicationContext(), contactList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contactListAdapter);
    }

    private List<ContactModel> getAllContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if ((cur!= null ? cur.getCount() : 0) > 0) {
            while (cur!= null && cur.moveToNext()) {
                ContactModel contactModel = new ContactModel();
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                contactModel.name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        contactModel.mobileNumber = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    pCur.close();
                }
                contactList.add(contactModel);
            }
        }
        if (cur!= null) {
            cur.close();
        }
        return contactList;
    }

    @Override
    protected void detachPresenter() {
        homeActivityPresenter.detachView();
    }

    @Override
    public void requestPermission() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                    // show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setMessage("Read Contacts permission is required.");
                    builder.setTitle("Please grant permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    HomeActivity.this,
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    MY_PERMISSION_REQUEST_CODE
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            HomeActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSION_REQUEST_CODE
                    );
                }
            }else {
                loadList();
            }
        }
    }

    @Override
    public void showToast(String message) {
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_menu:
                addContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addContact() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.add_contact, null);
        final EditText editName = alertLayout.findViewById(R.id.contactName);
        final EditText editNumber = alertLayout.findViewById(R.id.contactNumber);
        final Button add = alertLayout.findViewById(R.id.addButton);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editName.getText().toString().isEmpty() && !editNumber.getText().toString().isEmpty()){
                    dialog.dismiss();
                    Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION) ;
                    contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE ) ;
                    contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, editName.getText().toString());
                    contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE,editNumber.getText().toString()) ;
                    startActivityForResult(contactIntent , 1 ) ;
                }else {
                    showToast("Enter all details");
                }
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 ) {
            if (resultCode == Activity. RESULT_OK ) {
                showToast("Added Contact");
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(data.getData(),
                        null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

                if ((cur!= null ? cur.getCount() : 0) > 0) {
                    while (cur!= null && cur.moveToNext()) {
                        ContactModel contactModel = new ContactModel();
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                        contactModel.name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                contactModel.mobileNumber = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            pCur.close();
                        }
                        contactList.add(contactModel);
                        contactListAdapter.notifyDataSetChanged();
                    }
                }
                if (cur!= null) {
                    cur.close();
                }
            }
            if (resultCode == Activity. RESULT_CANCELED ) {
                showToast("Cancelled Added Contact");
            }
        }
    }
}
