//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckedTextView;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.kooktaxi.R;
//import com.example.kooktaxi.SearchActivity;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//public class ChatActivity extends AppCompatActivity {
//    public ListView lv_chating;
//    private EditText et_send;
//    public Button btn_send;
//    public Button btn_out;
//    public CheckedTextView check_text1;
//    public CheckedTextView check_text2;
//    public CheckedTextView check_text3;
//    public String socketData = "";
//
//    private ArrayAdapter<String> arrayAdapter;
//    private ArrayList<String> arr_room = new ArrayList<>();
//
//    public String str_room_name;
//    private String str_user_mail;
//    private String station;
//
//    private String key;
//    public String chat_user;
//    public String chat_message;
//
//    public int confirm_cnt = 0; // 마스터가 사용자 입금 완료 확인 하는 용도
//    public int pay_cnt = 0; // 사용자가 입금 완료 확인하는 용도
//
//    public String master_mail;
//    public String[] user_list = {"", "", "","","","","","",""};
//    public int cnt_user = 1;
//
//    public boolean matched = false;
//
//    Handler handler = new Handler();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar);
//        setSupportActionBar(tb);
//
//        et_send = (EditText) findViewById(R.id.et_send);
//        lv_chating = (ListView) findViewById(R.id.lv_chating);
//        btn_send = (Button) findViewById(R.id.btn_send);
//        btn_out = (Button) findViewById(R.id.btn_out);
//
//        check_text1 = (CheckedTextView) findViewById(R.id.checkedTextView1);
//        check_text2 = (CheckedTextView) findViewById(R.id.checkedTextView2);
//        check_text3 = (CheckedTextView) findViewById(R.id.checkedTextView3);
//
//        str_room_name = getIntent().getExtras().get("room_name").toString();
//        str_user_mail = getIntent().getExtras().get("user_mail").toString();
//        station = getIntent().getExtras().get("station").toString();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ChatInfo").child(station).child(str_room_name); // 채팅 정보 읽어옴
//
//        getSupportActionBar().setTitle(station +" "+ str_room_name + " 채팅방");
//
//        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_room);
//        lv_chating.setAdapter(arrayAdapter);
//        //리스트뷰가 갱신될 때 하단으로 자동 스크롤
//        lv_chating.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//
//        btn_send.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                //map을 사용해 name과 메시지를 가져오고, key에 값 요청
//                Map<String, Object> map = new HashMap<String, Object>();
//                key = reference.push().getKey();
//
//                DatabaseReference root = reference.child(key);
//
//                //updateChildren를 호출하여 database 최종 업데이트
//                Map<String, Object> objectMap = new HashMap<String, Object>();
//                objectMap.put("name", str_user_mail);
//                objectMap.put("message", et_send.getText().toString());
//
//                root.updateChildren(objectMap);
//
////                System.out.println(str_user_ID); // 메일 확인용
//
//                et_send.setText("");
//            }
//        });
//
//        btn_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (matched == false) {
//                    if (Arrays.asList(user_list).contains(chat_user)) {
//                        int idx = Arrays.asList(user_list).indexOf(str_user_mail);
//                        user_list[idx] = "";
//                        cnt_user--;
//                    }
//
//                    Intent intent = new Intent(ChatActivity.this, SearchActivity.class);
//                    intent.putExtra("mail", str_user_mail);
//                    intent.putExtra("station", station);
//                    startActivity(intent);
//                }
//                else {
//                    if (confirm_cnt == cnt_user & pay_cnt == cnt_user){
//                        int idx = Arrays.asList(user_list).indexOf(str_user_mail);
//                        user_list[idx] = "";
//                        cnt_user--;
//
//                        for (int j=0; j<user_list.length; j++){
//                            System.out.println(user_list[j]);
//                        }
//
//                        Intent intent = new Intent(ChatActivity.this, SearchActivity.class);
//                        intent.putExtra("mail", str_user_mail);
//                        intent.putExtra("station", station);
//                        startActivity(intent);
//                    }
//                }
//            }
//        });
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                master_mail = snapshot.child("Email").getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                chatConversation(dataSnapshot);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                chatConversation(dataSnapshot);
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//    //메뉴 자바 코드
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu1_hj, menu);
//        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
//
//        MenuItem item_master = menu.findItem(R.id.item_master);
//        MenuItem item_user = menu.findItem(R.id.item_user);
//
//        if (str_user_mail.equals(master_mail)) {
//            item_master.setVisible(true);
//            item_user.setVisible(false);
//        }
//        else {
//            item_master.setVisible(false);
//            item_user.setVisible(true);
//        }
//
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.item_matched:
//
//                //구현 못함 --> activity_search.xml에서 해당 방의 제목을 invisible하게 한다
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        startServer();
//                        send("매칭완료");
//                    }
//                }).start();
//
//                System.out.println(socketData);
//
//                if (socketData.equals("매칭완료")){
//                    btn_out.setVisibility(View.INVISIBLE);
//                }
//
//                return true;
//            case R.id.item_user1:
//                confirm_cnt++;
//                check_text1.setChecked(true);
//                return true;
//            case R.id.item_user2:
//                confirm_cnt++;
//                check_text2.setChecked(true);
//                return true;
//            case R.id.item_user3:
//                confirm_cnt++;
//                check_text3.setChecked(true);
//                return true;
//            // 강퇴하기
//            case R.id.item_user_1:
//                int user_idx = Arrays.asList(user_list).indexOf(str_user_mail);
//                if (user_idx == 1) {
//                    user_list[user_idx] = "";
//                    cnt_user--;
//
//                    Intent intent = new Intent(ChatActivity.this, SearchActivity.class);
//                    intent.putExtra("mail", str_user_mail);
//                    intent.putExtra("station", station);
//                    startActivity(intent);
//                }
//                return true;
//            case R.id.item_user_2:
//                int user_idx2 = Arrays.asList(user_list).indexOf(str_user_mail);
//                if (user_idx2 == 2) {
//                    user_list[user_idx2] = "";
//                    cnt_user--;
//
//                    Intent intent = new Intent(ChatActivity.this, SearchActivity.class);
//                    intent.putExtra("mail", str_user_mail);
//                    intent.putExtra("station", station);
//                    startActivity(intent);
//                }
//                return true;
//            case R.id.item_user_3:
//                int user_idx3 = Arrays.asList(user_list).indexOf(str_user_mail);
//                if (user_idx3 == 3) {
//                    user_list[user_idx3] = "";
//                    cnt_user--;
//
//                    Intent intent = new Intent(ChatActivity.this, SearchActivity.class);
//                    intent.putExtra("mail", str_user_mail);
//                    intent.putExtra("station", station);
//                    startActivity(intent);
//                }
//                return true;
//            case R.id.item_deposit:
//                pay_cnt++;
//                return true;
//        }
//        return true;
//    }
//
//    // addChildEventListener를 통해 실제 데이터베이스에 변경된 값이 있으면,
//    // 화면에 보여지고 있는 Listview의 값을 갱신함
//    private void chatConversation(DataSnapshot dataSnapshot) {
//        Iterator i = dataSnapshot.getChildren().iterator();
//
//        while(i.hasNext()) {
//            chat_message = (String) ((DataSnapshot) i.next()).getValue();
//            chat_user = (String) ((DataSnapshot) i.next()).getValue();
//
//            if (!Arrays.asList(user_list).contains(chat_user)) {
//                user_list[cnt_user] = chat_user;
//                cnt_user++;
//                for (int j=0; j<user_list.length; j++){
//                    System.out.println(user_list[j]);
//                }
////                if (cnt_user == 3) {
////                    //방에 들어올 수 없도록 해야함..
////                }
//            }
//
//            arrayAdapter.add(chat_user + " : " + chat_message);
//        }
//
//        arrayAdapter.notifyDataSetChanged();
//    }
//
//    public void send(String data) {
//        try {
//            int portNumber = 5001;
//            Socket sock = new Socket("localhost", portNumber);
//            printClientLog("소켓 연결함.");
//
//            ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
//            outstream.writeObject(data);
//            outstream.flush();
//            printClientLog("데이터 전송함.");
//
//            ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
//            printClientLog("서버로부터 받음 : " + instream.readObject());
//
//            socketData = data;
//            sock.close();
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void startServer() {
//        try {
//            int portNumber = 5001;
//
//            ServerSocket server = new ServerSocket(portNumber);
//            printServerLog("서버 시작함 : " + portNumber);
//
//            while(true) {
//                Socket sock = server.accept();
//                InetAddress clientHost = sock.getLocalAddress();
//                int clientPort = sock.getPort();
//                printServerLog("클라이언트 연결됨 : " + clientHost + " : " + clientPort);
//
//                ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
//                Object obj = instream.readObject();
//                printServerLog("데이터 받음 : " + obj);
//
//                ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
//                outstream.writeObject(obj + " from Server.");
//                outstream.flush();
//                printServerLog("데이터 보냄.");
//
//                sock.close();
//            }
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void printClientLog(final String data) {
//        Log.d("ChatActivity", data);
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(ChatActivity.this, data, Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
//
//    public void printServerLog(final String data) {
//        Log.d("MainActivity", data);
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(ChatActivity.this, data, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//}
