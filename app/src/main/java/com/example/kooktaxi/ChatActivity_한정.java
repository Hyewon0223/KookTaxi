/*
파일명: ChatActivity.java
개발자 이름: 백연선, 이한정
 */
package com.example.kooktaxi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    public static int cnt = 0; //입금 확인한 사용자 수 세는 용도

    public ListView lv_chating;
    private EditText et_send;
    public Button btn_send;
    public Button btn_out;
    public CheckedTextView check_text1;
    public CheckedTextView check_text2;
    public CheckedTextView check_text3;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_room = new ArrayList<>();

    public String str_room_name;
    private String str_user_mail;
    private String station;

    private String key;
    public String chat_user;
    public String chat_message;

    public String master_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_send = (EditText) findViewById(R.id.et_send);
        lv_chating = (ListView) findViewById(R.id.lv_chating);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_out = (Button) findViewById(R.id.btn_out);
        check_text1 = (CheckedTextView) findViewById(R.id.checkedTextView1);
        check_text2 = (CheckedTextView) findViewById(R.id.checkedTextView2);
        check_text3 = (CheckedTextView) findViewById(R.id.checkedTextView3);

        str_room_name = getIntent().getExtras().get("room_name").toString();
        str_user_mail = getIntent().getExtras().get("user_mail").toString();
        station = getIntent().getExtras().get("station").toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ChatInfo").child(station).child(str_room_name); // 채팅 정보 읽어옴

        getSupportActionBar().setTitle(station +" "+ str_room_name + " 채팅방");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_room);
        lv_chating.setAdapter(arrayAdapter);
        //리스트뷰가 갱신될 때 하단으로 자동 스크롤
        lv_chating.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //map을 사용해 name과 메시지를 가져오고, key에 값 요청
                Map<String, Object> map = new HashMap<String, Object>();
                key = reference.push().getKey();

                DatabaseReference root = reference.child(key);

                //updateChildren를 호출하여 database 최종 업데이트
                Map<String, Object> objectMap = new HashMap<String, Object>();
                objectMap.put("name", str_user_mail);
                objectMap.put("message", et_send.getText().toString());

                root.updateChildren(objectMap);

//                System.out.println(str_user_ID); // 메일 확인용

                et_send.setText("");
            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ChatActivity.this, SearchActivity.class); // 나가면서 데베 기록도 삭제하면 좋겠음
                startActivity(it);
            }
        });

        if(cnt == 3) {
            btn_out.setVisibility(View.VISIBLE);
        }



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                master_mail = snapshot.child("Email").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                chatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                chatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //메뉴 자바 코드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu1, menu);
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem item_master = menu.findItem(R.id.item_master);
        MenuItem item_user = menu.findItem(R.id.item_user);

        if (str_user_mail.equals(master_mail)) {
            item_master.setVisible(true);
            item_user.setVisible(false);
        }
        else {
            item_master.setVisible(false);
            item_user.setVisible(true);
        }

//        MenuItem item_out = menu.findItem(R.id.item_out);
//
//        if(cnt == 3) {
//            item_out.setVisible(true);
//        }


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_matched: //매칭완료 아이템을 누르면
                //툴바 있어? getSupportActionBar().setDisplayHomeAsUpEnabled(false); //툴바의 뒤로가기 버튼을 안보이게 하고
                btn_out.setVisibility(View.INVISIBLE); //나가기 버튼을 안보이게 만듦
                //구현 못함 --> activity_search.xml에서 해당 방의 제목을 invisible하게 한다
                return true;
            case R.id.item_user1:
                cnt++;
                check_text1.setChecked(true);
                return true;
            case R.id.item_user2:
                cnt++;
                check_text2.setChecked(true);
                return true;
            case R.id.item_user3:
                cnt++;
                check_text3.setChecked(true);
                return true;
        }
        return false;
    }

//    public void addDepositChecked(MenuItem item) { //각 사용자의 입금이 확인되면 리스트에 넣어주는 함수
//        if(item.isChecked()) {
//            deposit_user_list.add(item.getTitle().toString());
//        }
//    }

    // addChildEventListener를 통해 실제 데이터베이스에 변경된 값이 있으면,
    // 화면에 보여지고 있는 Listview의 값을 갱신함
    private void chatConversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()) {
            chat_message = (String) ((DataSnapshot) i.next()).getValue();
            chat_user = (String) ((DataSnapshot) i.next()).getValue();

            arrayAdapter.add(chat_user + " : " + chat_message);
        }

        arrayAdapter.notifyDataSetChanged();
    }
}
