package test.com.badman.masque;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SubAdminHistoryActivity extends AppCompatActivity
{
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Sub_admin, SubViewHolder> mPeopleRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_admin_history);


        mToolbar = findViewById(R.id.subadmin_history_toolbar);
        mToolbar.setTitle("Sub Admin Collection");
        setSupportActionBar(mToolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("subadmin");
        mDatabase.keepSynced(true);

        mPeopleRV = findViewById(R.id.subadmin_history_recycleview);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("subadmin");
        Query personsQuery = personsRef.orderByKey();

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Sub_admin>().setQuery(personsQuery, Sub_admin.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Sub_admin, SubAdminHistoryActivity.SubViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(SubAdminHistoryActivity.SubViewHolder holder, final int position, final Sub_admin model) {
                holder.setName(model.getName());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String Id = model.getId();
                        final String Area = model.getArea();
                        final String sub_admin_name = model.getName();



                        Intent intent = new Intent(SubAdminHistoryActivity.this,SubAdminHistoryShowActivity.class);
                        intent.putExtra("sub_admin_id", Id);
                        startActivity(intent);


                    }
                });
                //  holder.setDesignation(model.getCode());
//                holder.setEmail(model.getEmail());
//                holder.setMobile(model.getMobile());
//                holder.setPhone(model.getPhone());
//                holder.setImage(getBaseContext(), model.getImage());
            }


            @Override
            public SubAdminHistoryActivity.SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.sub_admin_rows, parent, false);

                return new SubAdminHistoryActivity.SubViewHolder(view);
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

    public static class SubViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public SubViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView sub_admin = (TextView)mView.findViewById(R.id.sub_admin_name);
            sub_admin.setText(name);
        }
    }
}
