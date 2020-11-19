package com.example.kooktaxi;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class networkclientsocket extends AppCompatActivity {
    Socket socket;     //클라이언트의 소켓

    DataInputStream is;
    DataOutputStream os;

    String ip;
    String port;

    String msg="";
    boolean isConnected=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    //클라이언트 소켓 열고 서버 소켓에 접속
    public void ClientSocketOpen(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    ip= "192.168.123.156";//IP 주소가 작성되어 있는 EditText에서 서버 IP 얻어오기
                    port = "5001";
                    if(ip.isEmpty() || port.isEmpty()){
                        networkclientsocket.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(networkclientsocket.this, "ip주소와 포트번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        //서버와 연결하는 소켓 생성..
                        socket = new Socket(InetAddress.getByName(ip), Integer.parseInt(port));
                        //여기까지 왔다는 것을 예외가 발생하지 않았다는 것이므로 소켓 연결 성공..

                        //서버와 메세지를 주고받을 통로 구축
                        is = new DataInputStream(socket.getInputStream());
                        os = new DataOutputStream(socket.getOutputStream());

                        networkclientsocket.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(networkclientsocket.this, "Connected With Server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //서버와 접속이 끊길 때까지 무한반복하면서 서버의 메세지 수신
                while(isConnected){
                    try {
                        msg= is.readUTF(); //서버 부터 메세지가 전송되면 이를 UTF형식으로 읽어서 String 으로 리턴
                        //runOnUiThread()는 별도의 Thread가 main Thread에게 UI 작업을 요청하는 메소드이다
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Toast.makeText(networkclientsocket.this, msg, Toast.LENGTH_SHORT);
                            }
                        });
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }//while
            }//run method...
        }).start();//Thread 실행..

    }

    public void SendMessage(View view, String data) {
        if(os==null) return;   //서버와 연결되어 있지 않다면 전송불가..

        //네트워크 작업이므로 Thread 생성
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //서버로 보낼 메세지 EditText로 부터 얻어오기
                String msg= data;
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String msg= data;
                            // TODO Auto-generated method stub
                            Toast.makeText(networkclientsocket.this, data, Toast.LENGTH_SHORT);
                        }
                    });
                    os.writeUTF(msg);  //서버로 메세지 보내기.UTF 방식으로(한글 전송가능...)
                    os.flush();        //다음 메세지 전송을 위해 연결통로의 버퍼를 지워주는 메소드..

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }//run method..

        }).start(); //Thread 실행..
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}