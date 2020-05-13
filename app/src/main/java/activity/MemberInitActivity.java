package activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mure.MemberInfoActivity;
import com.example.mure.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import activity.CameraActivity;

public class MemberInitActivity extends AppCompatActivity {

    private static final String TAG = "MemberInitActivity";
    private ImageView profileImageView;
    private String profilePath;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);
        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(onClickListener);
        //버튼에 리스너
        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
        findViewById(R.id.takePicture).setOnClickListener(onClickListener);
        findViewById(R.id.gallary).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //페이스북 보면 버튼누르면 갤러리 , or 사진찍을지를 선택하니까 따라만들어봄
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    //  String returnValue = data.getStringExtra("some_key");
                    profilePath = data.getStringExtra("profilePath");
                    //Log.e("로그","profilePath");
                    Bitmap bmp = BitmapFactory.decodeFile(profilePath);
                    profileImageView.setImageBitmap(bmp);
                }//카메라 엑티비티가 끝나면 여기서 멘트를 보여주면서 값을 확인할 수 있다.
                break;
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkButton:
                    profileUpdate();
                    break;
                case R.id.profileImageView:
                    Log.e("로그","merberinit 로그");
                    CardView cardView = findViewById(R.id.buttondsCardView);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.takePicture:
                    myStartActivity(CameraActivity.class);
                    break;
                case R.id.gallary:
                    //갤러리들어가기 전에 권한 체크
                    if (ContextCompat.checkSelfPermission(MemberInitActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MemberInitActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MemberInitActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        } else {
                            startToast("권한을 허용해 주세요");
                        }
                    }else{

                        myStartActivity(GalleryActivity.class);//권한 허용 시 실행
                    }
                    break;
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStartActivity(GalleryActivity.class);//권한 허용 시 실행
                } else {
                    startToast("권한을 허용해 주세요");
                }

            }
        }
    }



    private void profileUpdate() {
        final String name = ((EditText) findViewById((R.id.nameEditText))).getText().toString();//스트링값이 아니기에 스트링값으로 변환 함.,입력한 텍스트를 받아옴
        final String phoneNumber = ((EditText) findViewById((R.id.phoneNumberEditText))).getText().toString();
        final String birtyDay = ((EditText) findViewById((R.id.nameEditText))).getText().toString();
        final String address = ((EditText) findViewById((R.id.nameEditText))).getText().toString();


        if(name.length()>0 && phoneNumber.length()>9 && birtyDay.length()>5  && address.length()>0) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            user = FirebaseAuth.getInstance().getCurrentUser();//파이어 베이스에 저장된사진을 불러와서 사용하기 위함..
            final StorageReference mountainImagesRef = storageRef.child("images/" + user.getUid() + "/profileImage.jpg");

            if(profilePath==null){
                //null인경우 데이터만올려줌
                MemberInfoActivity memberInfo = new MemberInfoActivity(name,phoneNumber,birtyDay,address);
                uploder(memberInfo);

            }else{
                try {
                    InputStream stream = new FileInputStream(new File("profilePath"));
                    UploadTask uploadTask =mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Log.e("성공", "성공"+downloadUri);
                                MemberInfoActivity memberInfo = new MemberInfoActivity(name,phoneNumber,birtyDay,address,downloadUri.toString());
                                uploder(memberInfo);
                                //데이터베이스에서 유저를 구분하려면 고유키가 필요해서 기억하기 편하고 구분하기위해서
                                //인증해서아이디를 만들면 ui들을 만들어주는데 데이터베이스에 저장하기위함. 그키로 사용자를 찾고 할수 있음.
                            } else {
                                Log.e("로그", "실패");
                                startToast("회원정보를  보내는데 실패 하였습니다.");
                            }
                        }
                    });
                }catch (FileNotFoundException e){
                    Log.e("로그","에러"+e.toString());
                }
            }
        }else {
            startToast("회원정보를 입력해주세요.");
        }
    }


    private void uploder(MemberInfoActivity memberInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startToast("회원정보 등록을 성공하였습니다.");
                //등록에 성공하면화면이꺼져야하므로 finish 사용
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                startToast("회원정보 등록에 실패하였습니다.");

            }
        });
    }
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void myStartActivity(Class c){
        Intent intent=new Intent(this, c);
        startActivityForResult(intent,0);
    }
}
