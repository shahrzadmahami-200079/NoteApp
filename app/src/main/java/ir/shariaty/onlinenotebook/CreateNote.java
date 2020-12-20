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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import ir.shariaty.onlinenotebook.Model.Note;

public class CreateNote extends AppCompatActivity {
    private TextView save;
    private EditText title;
    private EditText description;

    private ProgressDialog progressDialog;


    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_note);


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
            Toast.makeText(CreateNote.this, "Empty Fields!", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Please Wait ... ");
            progressDialog.show();


            mRootRef = mRootRef.child("Notes");
            String noteId = mRootRef.push().getKey();

            HashMap<String , Object> mapItemNote = new HashMap<>();

            mapItemNote.put("title", txtTitle);
            mapItemNote.put("description", txtDescription);
            mapItemNote.put("time", time);

            mRootRef.child("Note").child(mAuth.getCurrentUser().getUid()).setValue(noteId).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(CreateNote.this, "Successful Save Note", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateNote.this, MainPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

}





