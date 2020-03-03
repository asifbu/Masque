package test.com.badman.masque;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class PaidHistoryActivity extends AppCompatActivity {

    private   String Area  ;
    private   String Id  ;
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<PaidHistory, ContactViewHolder> mPeopleRVAdapter;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_history);



        mToolbar=findViewById(R.id.contact_toolbar);
        mToolbar.setTitle("Paid History");
        setSupportActionBar(mToolbar);
        // setTitle("Contact");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //  extras = extras.getBundle("KEY_EXTRAS");
            String status = extras.getString("area");
            String id = extras.getString("id");

            Area=status;
            Id=id;

        }



        mDatabase = FirebaseDatabase.getInstance().getReference().child("paid_history").child(Area).child(Id);
        mDatabase.keepSynced(true);

        mPeopleRV =  findViewById(R.id.myRecycleView);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("paid_history").child(Area).child(Id);
        Query personsQuery = personsRef.orderByKey();

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<PaidHistory>().setQuery(personsQuery, PaidHistory.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<PaidHistory, PaidHistoryActivity.ContactViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(PaidHistoryActivity.ContactViewHolder holder, final int position, final PaidHistory model) {
                String PAID=model.getPaid()+" "+"Month";
                        String TAKA=model.getTaka()+" "+"TAKA";
                        String RECEIVED="Received By :"+" "+" "+model.getReceivedby();


                holder.setName(PAID);
                holder.setDesignation(TAKA);
                holder.setEmail(RECEIVED);
            }


            @Override
            public PaidHistoryActivity.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.paid_history, parent, false);

                return new PaidHistoryActivity.ContactViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mPeopleRVAdapter);


    }





    @Override
    public void onStart() {
        super.onStart();
        mPeopleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPeopleRVAdapter.stopListening();


    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ContactViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView teacher_name = (TextView)mView.findViewById(R.id.month_show);
            teacher_name.setText(name);
        }
        public void setDesignation(String designation){
            TextView teacher_designation = (TextView)mView.findViewById(R.id.tk_show);
            teacher_designation.setText(designation);
        }

        public void setEmail(String email){
            TextView eemail = (TextView)mView.findViewById(R.id.history_received);
            eemail.setText(email);
        }

    }


}

