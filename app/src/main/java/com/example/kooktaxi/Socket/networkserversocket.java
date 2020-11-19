package com.example.kooktaxi;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class networkserversocket extends AppCompatActivity {
//    //xml
//    TextView ipText;
//    TextView portText;
//    TextView text_msg; //클라이언트로부터 받을 메세지를 표시하는 TextView
//    EditText edit_msg; //클라이언트로 전송할 메세지를 작성하는 EditText

    ServerSocket serversocket;
    Socket socket;
    DataInputStream is;
    DataOutputStream os;

    String msg="";
    boolean isConnected=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //내 아이피 확인 및 세팅
        try {
            Toast.makeText(networkserversocket.this, getLocalIpAddress(), Toast.LENGTH_LONG);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    //Open Server버튼을 누르면 동작하는 온클릭 메소드
    public void ServerSocketOpen(View view) throws IOException {
        final String port = "5001";
        if(port.isEmpty()){
            Toast.makeText(this, "포트번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Socket Open", Toast.LENGTH_SHORT).show();
            //Android API14버전이상 부터 네트워크 작업은 무조건 별도의 Thread에서 실행 해야함.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        //서버소켓 생성.
                        serversocket = new ServerSocket(Integer.parseInt(port));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        //서버에 접속하는 클라이언트 소켓 얻어오기(클라이언트가 접속하면 클라이언트 소켓 리턴)
                        socket = serversocket.accept(); //서버는 클라이언트가 접속할 때까지 여기서 대기 접속하면 다음으로 코드로 넘어감
                        //클라이언트와 데이터를 주고 받기 위한 통로구축
                        is = new DataInputStream(socket.getInputStream()); //클라이언트로 부터 메세지를 받기 위한 통로
                        os = new DataOutputStream(socket.getOutputStream()); //클라이언트로 메세지를 보내기 위한 통로

                        networkserversocket.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(networkserversocket.this, "Connected With Client Socket", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //클라이언트가 접속을 끊을 때까지 무한반복하면서 클라이언트의 메세지 수신
                    while (isConnected) {
                        try {
                            msg = is.readUTF();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //클라이언트로부터 읽어들인 메시지msg를 TextView에 출력한다. 안드로이드는 메인스레드가 아니면 UI변경 불가하므로 다음과같이 해줌.(토스트메세지도 마찬가지)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Toast.makeText(networkserversocket.this,msg,Toast.LENGTH_SHORT);
                            }
                        });
                    }//while..
                }//run method...
            }).start(); //Thread 실행..
        }
    }


    //메세지전송
    public void SendMessage(View view, String data) {
        if(os==null) return; //클라이언트와 연결되어 있지 않다면 전송불가
        final String msg= data;
        //네트워크 작업이므로 Thread 생성
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //클라이언트로 보낼 메세지 EditText로 부터 얻어오기

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // TODO Auto-generated method stub
                            Toast.makeText(networkserversocket.this, msg, Toast.LENGTH_SHORT);
                        }
                    });

                    os.writeUTF(msg); //클라이언트로 메세지 보내기.UTF 방식으로 한글 전송 가능하게함
                    os.flush();   //다음 메세지 전송을 위해 연결통로의 버퍼를 지워주는 메소드
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start(); //Thread 실행..
    }

    //내 ip 얻기
    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }
}
