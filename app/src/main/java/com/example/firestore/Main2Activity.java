package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Main2Activity extends AppCompatActivity {


    private String TAG= "Main2Activity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference noteref = db.collection("Information");

    TextView text;

    EditText namesearch;

    Button btn;


    private DocumentSnapshot lastresults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        text= (TextView)findViewById(R.id.textdb);

        namesearch=(EditText) findViewById(R.id.namesearch);
        btn= (Button)findViewById(R.id.srchbtn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showresult();

            }
        });

    }

    private void showresult() {


        Query query;

        if(lastresults==null)
        {
            query = noteref
                    .whereEqualTo("title", namesearch.getText().toString())
                    .orderBy("timeStamp", Query.Direction.ASCENDING)
                    .limit(5);
        }
        else {

            query = noteref
                    .whereEqualTo("title", namesearch.getText().toString())
                    .orderBy("timeStamp", Query.Direction.ASCENDING)
                    .startAfter(lastresults)
                    .limit(5);

        }


        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data="";

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            Note note = documentSnapshot.toObject(Note.class);

                            data+= note.getTitle() + " " + note.getAge() + " " + note.getAddress() + " " + note.getContactno() + "\n\n";

                        }


                        if(queryDocumentSnapshots.size()>0) {

                            data += "__________________\n";

                            text.append(data);

                            lastresults = queryDocumentSnapshots
                                    .getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, e.toString());

                    }
                });



    }


}
