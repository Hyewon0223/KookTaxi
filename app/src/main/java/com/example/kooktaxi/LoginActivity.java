package com.example.kooktaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    public String mail = "";
    public String pw = "";

    String[] info = {mail, pw};
    String[] fill = {"Email ", "Password "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button joinButton = (Button) findViewById(R.id.Joinbutton);
        Button loginButton = (Button) findViewById(R.id.Loginbutton);

        EditText mailEdit = (EditText) findViewById(R.id.mailEditText);
        EditText pwEdit = (EditText) findViewById(R.id.pwEditText);

        EditText[] edits = {mailEdit, pwEdit};

        TextView alarm = (TextView) findViewById(R.id.alarmTextView);

        firebaseAuth = FirebaseAuth.getInstance();

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(it);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = mailEdit.getText().toString();
                pw = pwEdit.getText().toString();

                for (int i = 0; i < info.length; i++)
                    if (!PASSWORD_PATTERN.matcher(info[i]).matches()) edits[i].setHint("Please adjust the format.");

                for (int i = 0; i < info.length; i++)
                    if (info[i] == "") edits[i].setHint(fill[i] + "was not filled.");

                if (mail.length() != 0 && pw.length() != 0) {
                    firebaseAuth.signInWithEmailAndPassword(mail,pw)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        System.out.println("성공");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("mail", mail);
                                        startActivity(intent);
                                    }
                                    else alarm.setText("This ID does not exist.");
                                }
                            });
                }
                else alarm.setText("Fill in the blanks, please.");
            }
        });
    }
}