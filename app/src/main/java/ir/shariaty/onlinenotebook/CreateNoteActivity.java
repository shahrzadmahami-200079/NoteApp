package ir.shariaty.onlinenotebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateNoteActivity extends AppCompatActivity {
    private TextView save;
    private EditText title;
    private EditText description;

    private ProgressDialog progressDialog;


    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);


        save = findViewById(R.id.save);
        title = findViewById(R.id.etTitle);
        description = findViewById(R.id.etDescription);

        progressDialog = new ProgressDialog(this);


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        save.setOnClickListener(saveView -> saveNote());


    }

    private void saveNote() {
        String txtTitle = title.getText().toString().trim();
        String txtDescription = description.getText().toString().trim();
        Long time = System.currentTimeMillis();

        if (TextUtils.isEmpty(txtTitle) || TextUtils.isEmpty(txtDescription)) {
            Toast.makeText(CreateNoteActivity.this, "Empty Fields!", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Please Wait ... ");
            progressDialog.show();


            mRootRef = mRootRef.child("Notes");
            String noteId = mRootRef.push().getKey();

            HashMap<String , Object> mapItemNote = new HashMap<>();

            mapItemNote.put("title", txtTitle);
            mapItemNote.put("description", txtDescription);
            mapItemNote.put("time", time);

            mRootRef.child(mAuth.getCurrentUser().getUid()).child(noteId).setValue(mapItemNote).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(CreateNoteActivity.this, "Successful Save Note", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateNoteActivity.this, MainPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateNoteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}





