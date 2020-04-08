package com.example.letstalk;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.text.format.DateFormat;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myApp";
    FloatingActionButton button;
    EditText textMess;
    private ListView listview;
    FirebaseListAdapter <person> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textMess = findViewById(R.id.messageField);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btnSend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().push().setValue(new person(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), textMess.getText().toString()));
                textMess.setText("");
            }
        });
        textMess = findViewById(R.id.messageField);
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), 1);
            Toast.makeText(getApplicationContext(), "Вы авторизованы", Toast.LENGTH_LONG);
        }
        else
            Toast.makeText(getApplicationContext(), "Вы авторизованы", Toast.LENGTH_LONG);
        DisplayMess();
    }
    public void DisplayMess(){
        listview = (ListView) findViewById(R.id.list_messages);
        Query query = FirebaseDatabase.getInstance().getReference().child("talkprog-9a0a5");
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
