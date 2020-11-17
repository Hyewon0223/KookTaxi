/*
파일명: JoinActivity.java
개발자 이름: 조나영, 최혜원
 */
package com.example.kooktaxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    public String Id = "";
    public String mail = "";
    public String pw = "";
    public String pwCheck = "";
    public String dp = "";
    public String phone = "";
    public String gender = "";

    String[] info = {Id, mail, pw, dp, phone};
    String[] alarm = {"ID ", "Email ", "Password ", "Department ", "Phone Number "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText IDEdit = (EditText) findViewById(R.id.IDjoinEdit);
        EditText mailEdit = (EditText) findViewById(R.id.emailjoinEdit);
        EditText pwEdit = (EditText) findViewById(R.id.PwjoinEdit);
        EditText dpEdit = (EditText) findViewById(R.id.dpjoinEdit);
        EditText phoneEdit = (EditText) findViewById(R.id.phonejoinEdit);
        EditText pwCheckEdit = (EditText) findViewById(R.id.pwjoinCheckEdit);

        EditText[] edits = {IDEdit,  mailEdit, pwEdit, dpEdit, phoneEdit};

        RadioGroup rg = (RadioGroup) findViewById(R.id.RadioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.maleButton) gender = "male";
                else if (checkedId == R.id.femaleButton) gender = "female";
            }
        });

        CheckBox Check = (CheckBox) findViewById(R.id.check);
        TextView alarmtext = (TextView) findViewById(R.id.alarmText);

        Button joinButton = (Button) findViewById(R.id.joinButton);

        // Firebase 설정
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        // joinButton을 클릭했을 때
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 모든 입력 값들을 String으로 각각의 변수에 저장
                Id = IDEdit.getText().toString();
                mail = mailEdit.getText().toString();
                pw = pwEdit.getText().toString();
                pwCheck = pwCheckEdit.getText().toString();
                dp = dpEdit.getText().toString();
                phone = phoneEdit.getText().toString();

                // mail = "123456@kookmin.ac.kr
                // mailCheck 배열에 '@'를 기준으로 분리해 저장
                String mailCheck[] = mail.split("@");

                //password 양식에 적합한지 검사
                for (int i = 1; i < 2; i++)
                    if (!PASSWORD_PATTERN.matcher(info[i]).matches()) edits[i].setHint("Please adjust the format.");

                //빈 칸이 있는지 확인
                for (int i = 0; i < info.length; i++)
                    if (info[i].equals("")) edits[i].setHint(alarm[i] + "was not filled.");

                // 모든 조건 충족시
                if (mailCheck[1].equals("kookmin.ac.kr") && pw.equals(pwCheck) && Check.isChecked() && gender != "") {
                    if (Id.length() != 0 && pw.length() != 0 && dp.length() != 0 && mail.length() != 0 && phone.length() != 0) {
                        firebaseAuth.createUserWithEmailAndPassword(mail,pw).addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // 아이디(학번)가 중복되지 않는다면
                                if (task.isSuccessful()) {
                                    // Map을 이용해 정보 저장
                                    Map<String, Object> values = toMap(pw, dp, mail, phone, gender);

                                    myRef.child("UserInfo").child(Id).updateChildren(values);
                                    // 회원가입 화면에서 로그인 화면으로 이동
                                    Intent it = new Intent(JoinActivity.this, LoginActivity.class);
                                    startActivity(it);
                                }
                                // 아이디(학번) 중복
                                else alarmtext.setText("ID is already existed.");
                            }
                        });
                    }

                    else { // 하나라도 조건이 충족되지 않는다면
                        for (int i = 0; i < info.length; i++)
                            if (info[i].equals("")) edits[i].setHint(alarm[i] + "was not filled.");
                    }
                }

                else if (!pw.equals(pwCheck)) alarmtext.setText("Please check the password.");
                else if (!mailCheck[1].equals("kookmin.ac.kr")) alarmtext.setText("Please check the email.");
                else if (gender.equals("")) alarmtext.setText("Please check the gender.");
                else if (!Check.isChecked()) alarmtext.setText("Please check to allow personal information.");
                else alarmtext.setText("Please check the gender.");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    public Map<String, Object> toMap(String pw, String dp, String mail, String phone, String gender) {
        HashMap<String, Object> result = new HashMap<>();

        result.put("Password", pw);
        result.put("Department", dp);
        result.put("Email", mail);
        result.put("Phone Number", phone);
        result.put("Gender", gender);

        return result;
    }

}