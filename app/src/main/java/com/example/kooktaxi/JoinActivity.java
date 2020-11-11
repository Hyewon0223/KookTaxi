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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

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
    public boolean IDCheck = false;

    String[] info = {Id, mail, pw, dp, phone};
    String[] alarm = {"ID ", "Email ", "Password ", "Department ", "Phone Number "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id = IDEdit.getText().toString();
                mail = mailEdit.getText().toString();
                pw = pwEdit.getText().toString();
                pwCheck = pwCheckEdit.getText().toString();
                dp = dpEdit.getText().toString();
                phone = phoneEdit.getText().toString();

                String mailCheck[] = mail.split("@");

                for (int i = 1; i < 2; i++)
                    if (!PASSWORD_PATTERN.matcher(info[i]).matches()) edits[i].setHint("Please adjust the format.");

                for (int i = 0; i < info.length; i++)
                    if (info[i] == "") edits[i].setHint(alarm[i] + "was not filled."); //공란

                if (IDCheck && mailCheck[1].equals("kookmin.ac.kr") && pw.equals(pwCheck) && Check.isChecked() && gender != "") {
                    if (Id.length() != 0 && pw.length() != 0 && dp.length() != 0 && mail.length() != 0 && phone.length() != 0) {
                        firebaseAuth.createUserWithEmailAndPassword(mail,pw).addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> updates = new HashMap<>();
                                    Map<String, Object> values = null;
                                    values = toMap(pw, dp, mail, phone, gender);

                                    updates.put(Id, values);
                                    myRef.updateChildren(updates);

                                    Intent it = new Intent(JoinActivity.this, LoginActivity.class);
                                    startActivity(it);
                                }
                                else alarmtext.setText("ID is already existed.");
                            }
                        });
                    }

                    else {
                        for (int i = 0; i < info.length; i++)
                            if (info[i] == "") edits[i].setHint(alarm[i] + "was not filled.");
                    }
                }

                else if (!IDCheck) alarmtext.setText("Click the IDCheck Button.");
                else if (!pw.equals(pwCheck)) alarmtext.setText("Please check the password.");
                else if (!mailCheck.equals("kookmin.ac.kr")) alarmtext.setText("Please check the email.");
                else if (gender == "") alarmtext.setText("Please check the gender.");
                else if (!Check.isChecked()) alarmtext.setText("Please check to allow personal information.");
                else alarmtext.setText("Please check the gender.");

            }
        });
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


//https://gist.github.com/rlarla245
//- Firebase 이메일 주소 유효 확인
//// 이메일을 체크하는 메소드입니다.
//        fun sendEmailVerification() {
//        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
//        if (task.isSuccessful) {
//        Toast("확인 메일을 전송했습니다.")
//        } else {
//        Toast(task.exception)
//        }
//        }
//        }
//
//        1. 체크 버튼에 해당 메소드를 실행시킵니다.
//        2. 이메일 내용을 수정해봅시다. 콘솔로 이동합니다.
//        3. Authentication -> 템플릿 -> 이메일 주소 인증 탭 -> 수정 이모티콘(펜) 누르기 -> 제목 및 기타 텍스트 변경
//        *이메일 메시지는 수정할 수 없습니다. 템플릿 언어는 바꿔줍시다.
//        4. 이메일 인증이 완료되면 새로운 기능을 활성화 시켜야 겠죠? 테스트로 인증 버튼을 없애겠습니다. 메소드 코드를 수정합니다.
//// 메소드 상단에 입력합니다. true - 인증됨, false - 인증 안됨
//        if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
//        Toast("인증되었습니다.")
//        buttonEmailVerification.Enabled(true) -> 맞는지 확인 필요
//        return
//        }