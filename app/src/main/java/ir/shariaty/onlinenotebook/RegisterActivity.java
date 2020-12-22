package ir.shariaty.onlinenotebook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    ProgressDialog pd;

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
        pd = new ProgressDialog(this);

        loginUser.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, Login.class)));

        register.setOnClickListener(v -> {
            String txtUsername = username.getText().toString();
            String txtEmail = email.getText().toString();
            String txtPassword = password.getText().toString();

            if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(RegisterActivity.this, "Empty Fields!" , Toast.LENGTH_SHORT).show();
            } else if (txtPassword.length() < 6 ) {
                Toast.makeText(RegisterActivity.this, "Password too short" , Toast.LENGTH_SHORT).show();
            } else {
                registerUser (txtUsername , txtEmail , txtPassword);
            }
        });
    }

    private void registerUser(final String username, final String email, final String password) {

        pd.setMessage("Please Wait");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(authResult -> {

            HashMap<String, Object> map = new HashMap<>();
            map.put("email" , email);
            map.put("username" , username);
            map.put("id" , mAuth.getCurrentUser().getUid());


            mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Register is Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}