# [모바일 프로그래밍] Project_KookTaxi
### 국민대 소프트웨어학부 
## 20191604백연선 / 20191650이한정 / 20191670조나영 / 20191686최혜원
### https://github.com/Hyewon0223/KookTaxi
---
### 설명
- 목적지가 국민대인 사람들을 모아 택시를 함께 탈 수 있도록 하는 앱입니다.
- 동승하고 싶은 사람의 성별 여부, 인원수, 출발 지점(길음역, 광화문역, 홍대입구역, 동대문역사문화공원역) 등에 따라 사람들을 매칭해준다.
---
### 개발 환경
- Window OS
- Android Studio 4.0 (AVD: Pixel 2 API 30 / 안드로이드 폰)
---
### 사용 기술
- xml
- Java
- Firebase
- Google Cloud Platform(Maps SDK for Android API)
<div>
    <img src="https://user-images.githubusercontent.com/55418359/99349262-e41eb300-28de-11eb-8bd4-0cb2368d9b30.JPG" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99349264-e54fe000-28de-11eb-8448-44150c172a0d.JPG" width="200">
    <img src="https://user-images.githubusercontent.com/55418359/99349265-e5e87680-28de-11eb-80e5-358142a3bca6.JPG" width="200">
</div>

---
### 코드 관련 설명
#### manifests
##### AndroidManifest.xml
- 사용자의 현재 위치를 실시간으로 표현할 때 사용자 개인정보를 보호하려면 위치 서비스를 사용하는 앱에서 위치 정보 액세스 권한을 요청해야 함
~~~xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
~~~
#### layout
##### activity_login.xml, activity_join.xml, activity_main.xml, activity_search.xml, activity_chat.xml
- Relative Layout(Login 화면, Join 화면, Main 화면), Frame Layout(Main 화면), Linear Layout(Search 화면, Chat 화면)
- map component 추가를 위한 Frame Layout 사용
~~~xml
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/container">

    <fragment
        android:id="@+id/map"
        android:name="com.google.android.gms.maps.SupportMapFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MapsActivity" />

</FrameLayout>
~~~
- 실시간 변경이 가능한 데이터베이스에 있는 채팅방 목록 화면에 출력
~~~xml
<ListView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginBottom="50dp" />
~~~
#### menu
##### menu1.xml, menu_toolbar.xml
- 각 페이지의 툴바 작성
#### Java
##### LoginActivity.java, JoinActivity.java, MainActivity.java, SearchActivity.java, CharActivity.java
- 국민대 이메일이 아닌 경우 회원가입할 수 없음
~~~java
String mailCheck[] = mail.split("@");
...
else if (!mailCheck[1].equals("kookmin.ac.kr")) alarmtext.setText("Please check the email.");
~~~
- 4개의 지하철역 좌표 객체 생성 후 마커 추가
~~~java
LatLng Gwanghwamun = new LatLng(37.5707456,126.973708); //(위도, 경도)
LatLng Hongdae = new LatLng(37.557527,126.9222782);
LatLng Gileum = new LatLng(37.6086541,127.0136683);
LatLng DDP = new LatLng(37.5644,127.0055713);

MarkerOptions[] markerOptions = new MarkerOptions[4];
markerOptions[0] = new MarkerOptions()
        .position(Gwanghwamun)
        .title("광화문역");
markerOptions[1] = new MarkerOptions()
        .position(Hongdae)
        .title("홍대입구역");
markerOptions[2] = new MarkerOptions()
        .position(Gileum)
        .title("길음역");
markerOptions[3] = new MarkerOptions()
        .position(DDP)
        .title("동대문역사문화공원역");

for(int i=0; i<4; i++){
    mMap.addMarker(markerOptions[i]);
}
~~~
- 마커 클릭하면 해당 지하철 역의 activity 실행
~~~java
@Override
public boolean onMarkerClick(Marker marker) {
    if(marker.getTitle().equals("길음역")){
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("mail", mail);
        startActivity(intent);
    }
    ...
    return true;
}
~~~
