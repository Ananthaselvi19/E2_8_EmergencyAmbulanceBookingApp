package com.example.login;

//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//public class myAccount extends AppCompatActivity {
//
//    Button update;
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_account_fragment);
//
//        update = findViewById(R.id.Update);
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(myAccount.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//    public static class MyAccount_Fragment extends Fragment {
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.my_account_fragment, container, false);
//        }
//    }
//
//}
