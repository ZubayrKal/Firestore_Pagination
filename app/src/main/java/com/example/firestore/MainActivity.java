package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    EditText name, age, address, contno;

    EditText chngname, chngage, chngaddress, chngcontno;


    TextView showname, showage, showaddress, showcontno;


    private String TAG="MainActivity";

    Button btn, clear, newbtn, chngbtn, newbtn2;

    private FirebaseFirestore db;

    private DocumentReference noteref;

    Map<String, Object> note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        note = new HashMap<>();


        name= (EditText)findViewById(R.id.name);
        age= (EditText)findViewById(R.id.age);
        address= (EditText)findViewById(R.id.addr);
        contno= (EditText)findViewById(R.id.coninfo);

        chngname= (EditText)findViewById(R.id.chngname);
        chngage= (EditText)findViewById(R.id.chngage);
        chngaddress= (EditText)findViewById(R.id.chngaddr);
        chngcontno= (EditText)findViewById(R.id.chngconinfo);

        showname= (TextView) findViewById(R.id.showname);
        showage= (TextView) findViewById(R.id.showage);
        showaddress= (TextView) findViewById(R.id.showaddr);
        showcontno= (TextView) findViewById(R.id.showconinfo);


        btn= (Button)findViewById(R.id.btn);

        clear= (Button)findViewById(R.id.clearbtn);

        newbtn= (Button)findViewById(R.id.newbtn);

        chngbtn= (Button)findViewById(R.id.chngbtn);

        newbtn2 = (Button)findViewById(R.id.newbtn2);


        db= FirebaseFirestore.getInstance();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addtodb();

            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MainActivity.class);

                startActivity(intent);
                finish();

            }
        });

        newbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);

                startActivity(intent);
                finish();

            }
        });


        newbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Main3Activity.class);

                startActivity(intent);
                finish();

            }
        });

        chngbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update();

            }
        });


    }

    private void update() {

        noteref.delete();

        note.put("title", chngname.getText().toString());
        note.put("age", chngage.getText().toString());
        note.put("address", chngaddress.getText().toString());
        note.put("contactno", chngcontno.getText().toString());

        int ts = (int) (System.currentTimeMillis() / 1000);


        note.put("timeStamp", String.valueOf(ts));

        noteref.set(note);

        Toast.makeText(MainActivity.this,"Saved", Toast.LENGTH_SHORT).show();



    }

    private void addtodb() {



        Random rand = new Random();

        if(address.getText().toString().isEmpty())
        {
            address.setText(String.valueOf(rand.nextFloat()));
        }
        if(name.getText().toString().isEmpty())
        {
            name.setText(String.valueOf(rand.nextFloat()));
        }
        if(age.getText().toString().isEmpty())
        {
            age.setText(String.valueOf(rand.nextFloat()));
        }
        if(contno.getText().toString().isEmpty())
        {
            contno.setText(String.valueOf(rand.nextFloat()));
        }

        note.put("title", name.getText().toString());
        note.put("age", age.getText().toString());
        note.put("address", address.getText().toString());
        note.put("contactno", contno.getText().toString());

        int ts = (int) (System.currentTimeMillis() / 1000);


        note.put("timeStamp", String.valueOf(ts));


        db.collection("Information").document().set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(MainActivity.this,"Saved", Toast.LENGTH_SHORT).show();


                       // noteref= db.collection("Information").document();

                        final Query query = db.collection("Information").orderBy("timeStamp", Query.Direction.DESCENDING).limit(1);


                      query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                          @Override
                          public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                              noteref= db.collection("Information").document(queryDocumentSnapshots.toString());


                              for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments())
                              {
                                  showname.setText(documentSnapshot.getString("title"));
                                  showage.setText(documentSnapshot.getString("age"));
                                  showaddress.setText(documentSnapshot.getString("address"));
                                  showcontno.setText(documentSnapshot.getString("contactno"));

                                  chngname.setText(documentSnapshot.getString("title"));
                                  chngage.setText(documentSnapshot.getString("age"));
                                  chngaddress.setText(documentSnapshot.getString("address"));
                                  chngcontno.setText(documentSnapshot.getString("contactno"));


                              }


                          }
                      });




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "onFailure: "+ e.toString());

                    }
                });


    }
}
