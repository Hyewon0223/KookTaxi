/*
파일명: SearchActivity.java
개발자 이름: 최혜원
 */
package com.example.kooktaxi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    public ListView listView;
    public Button btn_create;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_roomList = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot();

    private String mail; // 사용자의 메일을 받아올 변수
    private String station; // 지하철 역의 정보를 받아올 변수
    private String str_room; // 채팅방 제목 설정

    Map<String, Object> map = new HashMap<String, Object>(); // firebase에 정보 묶을 때 사용

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 닉네임 및 역 정보 가져오기
        Intent intent = getIntent(); // 이전 화면인 MainActivity에서 정보를 받아옴
        mail = intent.getStringExtra("mail"); // "mail"인 정보를 받아와 mail에 저장
        station = intent.getStringExtra("station"); // "station"인 정보를 받아와 station에 저장

        listView = (ListView) findViewById(R.id.list); // xml에서 id가 list인 ListView를 받아옴
        btn_create = (Button) findViewById(R.id.btn_create); // xml에서 id가 btn_create인 버튼을 받아옴

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar); // xml에서 id가 toolbar인 ToolBar 위젯을 받아옴
        setSupportActionBar(tb); // 툴바 설정

        getSupportActionBar().setTitle(station); // 툴바에 station을 제목으로 함
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 시각화

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SearchActivity.this, MainActivity.class); // MainActivity로 보낼 정보를 담음
                intent.putExtra("mail", mail);
                intent.putExtra("station", station);
                startActivity(intent); // 메일과 지하철역을 담아 이전 화면으로 보냄
            }
        });

        // 채팅방 리스트를 보여준다.
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_roomList); // 실제로 문자열 데이터를 저장하는데 사용할 ArrayList객체를 생성합니다.
        listView.setAdapter(arrayAdapter); // arrayAdapter 설정

        // 다이얼로그에서 채팅방 이름을 적어서 채팅방을 생성
        btn_create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 채팅방 제목을 사람들이 지하철 역에서 만날 시간으로 나타낼 것이기 때문에 TimePicker를 이용
                final TimePicker et_inDialog = new TimePicker(SearchActivity.this); 
                final AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("채팅방 이름 입력"); // 다이얼로그의 제목 설정
                builder.setView(et_inDialog); // 화면에 표시
                // 다이얼로그의 "확인"을 눌렀을 때 
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        int time_hour = et_inDialog.getCurrentHour(); // 다이얼로그에서 선택한 시각
                        int time_minute = et_inDialog.getCurrentMinute(); // 다이얼로그에서 선택한 분
                        str_room = time_hour+"시"+time_minute+"분"; // 문자열로 만들어줌

                        Map<String, Object> values = toMap(mail, str_room); // toMap()메소드를 사용해 values라는 변수에 묶어서 저장
                        map.put(str_room,""); 
                        reference.child("ChatInfo").child(station).child(str_room).updateChildren(values); // firebase로 다음과 같은 위치로 저장
                    }
                });
                // 다이얼로그의 "취소"을 눌렀을 때
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        dialogInterface.dismiss(); // 다이얼로그 종료
                    }
                });
                builder.show();
            }
        });

        // 특정 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기함
        // onDataChange는 Database가 변경되었을때 호출되고
        // onCancelled는 취소됐을때 호출됩니다.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Set<String> set = new HashSet <String>(); // Set과 HashSet 컬렉션 사용
                // Iterator를 사용하여 firebase의 다음과 같은 위치의 정보를 계속해서 받아옴
                Iterator i = dataSnapshot.child("ChatInfo").child(station).getChildren().iterator(); 
            
                while (i.hasNext()){ // 다음 정보가 있을 때
                    set.add(((DataSnapshot) i.next()).getKey()); // set에 정보를 계속하여 추가
                }
                arr_roomList.clear(); // 채팅방 목록을 초기화
                arr_roomList.addAll(set); // 채팅방 목록에 set의 모든 값을 모두 넣음

                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

        // 리스트뷰의 채팅방을 클릭했을 때 반응
        // 채팅방의 이름과 입장하는 유저의 이름을 전달
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class); // intent에 다음과 같은 정보를 담음
                intent.putExtra("room_name", ((TextView) view).getText().toString());
                intent.putExtra("user_mail",mail);
                intent.putExtra("station", station);
                startActivity(intent); // 방 제목, 사용자 이메일, 역 정보
            }
        });
    }

    // firebase에 데이터를 묶어서 저장할때 사용 
    public Map<String, Object> toMap(String mail, String strRoom) {
        HashMap<String, Object> result = new HashMap<>();

        result.put("Email", mail);
        result.put("Room", strRoom);

        return result;
    }

    @Override
    // 툴바에서 이미지가 나타나게 만들어주는 메소드
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }
}
