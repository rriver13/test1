//package com.example.Subcept;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class user_Profile extends AppCompatActivity {
//
//    private FirebaseUser user;
//    private String userID;
//    private DatabaseReference reference;
//    private Button logout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_page);
//
//        logout = findViewById(R.id.signo);
//
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(user_Profile.this, MainActivity.class));
//
//            }
//        });
//
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Users");
//        userID = user.getUid();
//
//        final TextView messageTextView = findViewById(R.id.message);
//        final TextView fullNameTextView = findViewById(R.id.full_name);
//        final TextView emailTextView = findViewById(R.id.email_addy);
//        final TextView phoneTextView = findViewById(R.id.user_phone);
//
//        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                sUsers userProfile = snapshot.getValue(sUsers.class);
//
//                if(userProfile != null){
//                    String fullName = userProfile.fullName;
//                    String email = userProfile.email;
//                    String phone = userProfile.phone;
//
//                    messageTextView.setText("Welcome, " + fullName + "!");
//                    fullNameTextView.setText(fullName);
//                    emailTextView.setText(email);
//                    phoneTextView.setText(phone);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(user_Profile.this, "Something Wrong Has Happened!", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//}