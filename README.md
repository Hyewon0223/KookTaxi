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
#### Java
##### LoginActivity.java, JoinActivity.java, MainActivity.java, SearchActivity.java, CharActivity.java


