package ir.shariaty.onlinenotebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends Activity {
    private EditText email;
    private EditText password;
    private EditText username;
    private Button register;
    private TextView loginUser;

    /* ProgressDialog pd;*/

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        register = findViewById(R.id.register);
        loginUser = findViewById(R.id.login_user);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        /* pd = new ProgressDialog(this);*/

        loginUser.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, Login.class)));

        register.setOnClickListener(v -> {
            String txtEmail = email.getText().toString();
            String txtPassword = password.getText().toString();
            String txtUsername = username.getText().toString();

            if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword) || TextUtils.isEmpty(txtUsername)) {
                Toast.makeText(RegisterActivity.this, "Empty Fields!", Toast.LENGTH_SHORT).show();
            } else if (txtPassword.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txtEmail, txtPassword, txtUsername);
            }
        });
    }

    private void registerUser(final String email, final String password, final String username) {
        /* pd.setMessage("Please Wait");
        pd.show();*/


        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            HashMap<String, Object> map = new HashMap<>();

            map.put("email", email);
            map.put("username", username);
            map.put("id", mAuth.getCurrentUser().getUid());

            mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        /* pd.dismiss();*/
                        Toast.makeText(RegisterActivity.this, "Register is Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }).addOnFailureListener(e -> {
            /* pd.dismiss();*/
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}