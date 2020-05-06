package com.example.mure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final  String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            myStartActivity(SignUpActivity.class);
        }else{
            myStartActivity(SignUpActivity.class);//200506 확인작업 임시로 여기서 키움
            //회원정보를 입력하라는 activity 가  나와야함.
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                   DocumentSnapshot document = task.getResult();
                   if(document != null){
                       if(document.exists()){
                           Log.d(TAG,"Documentsnaphot data: "+document.getData());
                       }else {
                           Log.d(TAG,"no such");
                           myStartActivity(MemberInitActivity.class);
                       }
                   }

               }else{
                    Log.d("get Failed", String.valueOf(task.getException()));
               }
                }
            });

        }
        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
    }
        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.logoutButton:
                        FirebaseAuth.getInstance().signOut();
                        myStartActivity(SignUpActivity.class);
                        break;
                }

            }
        };
    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        startActivity(intent);
    }


}
