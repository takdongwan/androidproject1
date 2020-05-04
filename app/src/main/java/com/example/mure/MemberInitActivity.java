package com.example.mure;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberInitActivity extends AppCompatActivity {

    private static final  String TAG = "MemberInitActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        //버튼누를 때 예외처리리
        findViewById(R.id.checkButton).setOnClickListener(onClickListener);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkButton:
                    profileUpdate();
                    break;

            }
        }
    };

    private void profileUpdate() {
        String name = ((EditText) findViewById((R.id.nameEditText))).getText().toString();//스트링값이 아니기에 스트링값으로 변환 함.,입력한 텍스트를 받아옴
        String phoneNumber = ((EditText) findViewById((R.id.phoneNumberEditText))).getText().toString();
        String birtyDay = ((EditText) findViewById((R.id.nameEditText))).getText().toString();
        String address = ((EditText) findViewById((R.id.nameEditText))).getText().toString();

        if(name.length()>0 && phoneNumber.length()>9 && birtyDay.length()>5  && address.length()>0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//데이터베이스에서 유저를 구분하려면 고유키가 필요해서 기억하기 편하고 구분하기위해서
            //인증해서아이디를 만들면 ui들을 만들어주는데 데이터베이스에 저장하기위함. 그키로 사용자를 찾고 할수 있음.
            // Access a Cloud Firestore instance from your Activity
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(name,phoneNumber,birtyDay,address);
            if(user != null){
                db.collection("users").document(user.getUid()).set(memberInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("회원정보 등록을 성공하였습니다.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("회원정보 등록에 실패하였습니다.");
                    }
                });
            }
        }else {
            startToast("회원정보를 입력해주세요.");
        }
    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
