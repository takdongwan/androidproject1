package com.example.mure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //현재로그인 여부
        // Initialize Firebase Auth
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignUpActivity();
        }

        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
    }

        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.logoutButton:
                        FirebaseAuth.getInstance().signOut();
                        startSignUpActivity();//로그아웃 되면서 signupActivity 로 돌아가기 위함.
                        break;
                }

            }
        };
    private void startSignUpActivity(){
//        gotoLoginButton 버튼을 눌렀을시
        Intent intent=new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


}
