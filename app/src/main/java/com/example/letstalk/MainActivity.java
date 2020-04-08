package com.example.letstalk;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myApp";
    FloatingActionButton button, buttonexit;
    EditText textMess;
    FirebaseListAdapter <person> adapter;
    RelativeLayout activity;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1){
            if (resultCode==RESULT_OK){
                Snackbar.make(activity, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
                DisplayMess();
            } else{
                Snackbar.make(activity, "Вы не авторизованы", Snackbar.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textMess = findViewById(R.id.messageField);
        activity = findViewById(R.id.rel_layout);
        button = findViewById(R.id.btnSend);
        buttonexit = findViewById(R.id.btnexit);
        buttonexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().push().setValue(new person(FirebaseAuth.getInstance().getCurrentUser().getEmail(), textMess.getText().toString()));
                textMess.setText("");
            }
        });
        textMess = findViewById(R.id.messageField);
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .build(), 1);
        }
        else{
            Snackbar.make(activity, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
        }
        DisplayMess();
    }
    public void DisplayMess(){
        ListView listview = (ListView) findViewById(R.id.list_messages);
        Query query = FirebaseDatabase.getInstance().getReference();
        FirebaseListOptions<person> options = new FirebaseListOptions.Builder<person>()
                .setQuery(query, person.class)
                .setLayout(R.layout.text_design)
                .build();
        adapter = new FirebaseListAdapter<person>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull person model, int position) {
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                messageUser.setText(model.getName());
                messageText.setText(model.getMess());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
            }
        };
        listview.setAdapter(adapter);
}
}
