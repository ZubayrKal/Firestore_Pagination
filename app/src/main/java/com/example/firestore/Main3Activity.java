package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    RecyclerView recview;

    EditText searchtext;

    Button gobtn;

    LinearLayoutManager mlayoutmngr;

    DocumentSnapshot lastresult;

    boolean isscrolling= false;
    boolean islastitemReached = false;

    TextView loadingtext;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recview= (RecyclerView)findViewById(R.id.recview);

        handler = new Handler();

        searchtext= (EditText)findViewById(R.id.serchtext);

        gobtn= (Button)findViewById(R.id.gobtn);

        mlayoutmngr= new LinearLayoutManager(this);

        recview.setLayoutManager(mlayoutmngr);

        loadingtext = (TextView)findViewById(R.id.loadingtext);

        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchwork();

            }
        });



    }

    private void searchwork() {

        Query query = FirebaseFirestore.getInstance().collection("Information")
                .whereEqualTo("title", searchtext.getText().toString())
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .limit(5);

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        final List<Note> list= new ArrayList<>();
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            Note note = documentSnapshot.toObject(Note.class);

                            list.add(note);


                        }

                        final InfoAdapter infoAdapter = new InfoAdapter(list);
                        recview.setAdapter(infoAdapter);

                        lastresult = queryDocumentSnapshots
                                .getDocuments()
                                .get(queryDocumentSnapshots.size() - 1);



                        //onscrolllistener

                        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);

                                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                                {
                                    isscrolling = true;
                                }

                            }

                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);


                                int firstVisibleItem = mlayoutmngr.findFirstVisibleItemPosition();
                                int visibleItemCount = mlayoutmngr.getChildCount();
                                int totalItemCount = mlayoutmngr.getItemCount();

                                if(isscrolling && (firstVisibleItem + visibleItemCount == totalItemCount) && !islastitemReached)
                                {
                                    isscrolling = false;

                                    Query nextquery = FirebaseFirestore.getInstance().collection("Information")
                                            .whereEqualTo("title", searchtext.getText().toString())
                                            .orderBy("timeStamp", Query.Direction.ASCENDING)
                                            .startAfter(lastresult)
                                            .limit(5);

                                    nextquery.get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                                    {

                                                        loadingtext.setVisibility(View.VISIBLE);

                                                        Note note = documentSnapshot.toObject(Note.class);

                                                        list.add(note);


                                                    }

                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            loadingtext.setVisibility(View.VISIBLE);
                                                        }
                                                    }, 2000);

                                                    loadingtext.setVisibility(View.GONE);


                                                    infoAdapter.notifyDataSetChanged();

                                                    lastresult = queryDocumentSnapshots
                                                            .getDocuments()
                                                            .get(queryDocumentSnapshots.size() - 1);


                                                    if(queryDocumentSnapshots.size() < 5)
                                                    {
                                                        islastitemReached =true;
                                                    }


                                                }
                                            });
                                }

                            }
                        };


                        recview.addOnScrollListener(onScrollListener);


                    }
                });


    }

    private class  InfoAdapter extends RecyclerView.Adapter<InfoViewHolder>
    {
        List<Note> list;

        public InfoAdapter(List<Note> list) {

            this.list = list;

        }

        @NonNull
        @Override
        public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new InfoViewHolder(view);


        }

        @Override
        public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {

            String infotitle = list.get(position).getTitle();
            String infoage = list.get(position).getAge();

            String infoadd = list.get(position).getAddress();

            String infocontno = list.get(position).getContactno();


            holder.settitlework(infotitle);
            holder.setagework(infoage);
            holder.setaddwork(infoadd);
            holder.setcontctnowork(infocontno);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    private class InfoViewHolder extends RecyclerView.ViewHolder
    {

        View mview;

        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;

        }
        void settitlework(String title)
        {
            TextView titleshow = (TextView)mview.findViewById(R.id.showname);

            titleshow.setText(title);
        }
        void setagework(String age)
        {
            TextView ageshow = (TextView)mview.findViewById(R.id.showage);

            ageshow.setText(age);
        }
        void setaddwork(String addr)
        {
            TextView addshow = (TextView)mview.findViewById(R.id.showaddr);

            addshow.setText(addr);
        }
        void setcontctnowork(String contctno)
        {
            TextView contctnoshow = (TextView)mview.findViewById(R.id.showconinfo);

            contctnoshow.setText(contctno);
        }

    }

}
