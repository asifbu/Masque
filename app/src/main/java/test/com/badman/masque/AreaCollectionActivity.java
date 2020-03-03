package test.com.badman.masque;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AreaCollectionActivity extends AppCompatActivity
{
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<AreaCollection, ContactViewHolder> mPeopleRVAdapter;
    Dialog dialog;
    String Area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_collection);


        mToolbar=findViewById(R.id.collection_toolbar);
        mToolbar.setTitle("Paid History");
        setSupportActionBar(mToolbar);
        // setTitle("Contact");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //  extras = extras.getBundle("KEY_EXTRAS");
            String status = extras.getString("area");
            Area=status;


        }



        mDatabase = FirebaseDatabase.getInstance().getReference().child("total_paid_month").child(Area);
        mDatabase.keepSynced(true);

        mPeopleRV =  findViewById(R.id.collection_myRecycleView);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("total_paid_month").child(Area);
        Query personsQuery = personsRef.orderByKey();

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<AreaCollection>().setQuery(personsQuery, AreaCollection.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<AreaCollection, AreaCollectionActivity.ContactViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(AreaCollectionActivity.ContactViewHolder holder, final int position, final AreaCollection model) {
                String PAID=model.getMonth_year()+" "+"(Month)";
                String TAKA=model.getMonthly_total()+" "+"TAKA";
               // String RECEIVED="Received By :"+" "+" "+model.getReceivedby();


                holder.setName(PAID);
                holder.setDesignation(TAKA);
                //holder.setEmail(RECEIVED);
            }


            @Override
            public AreaCollectionActivity.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.paid_history, parent, false);

                return new AreaCollectionActivity.ContactViewHolder(view);
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

