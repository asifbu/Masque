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

public class AdminAreaActivity extends AppCompatActivity
{
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Sub_admin, MainActivity.SubViewHolder> mPeopleRVAdapter;

    private String collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_area);


        mToolbar=findViewById(R.id.admin_toolbar);
        mToolbar.setTitle("Admin");
        setSupportActionBar(mToolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //  extras = extras.getBundle("KEY_EXTRAS");
            String status = extras.getString("collection");

            collection=status;

        }


        mDatabase = FirebaseDatabase.getInstance().getReference().child("area");
        mDatabase.keepSynced(true);

        mPeopleRV =  findViewById(R.id.admin_area);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("area");
        Query personsQuery = personsRef.orderByKey();

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Sub_admin>().setQuery(personsQuery, Sub_admin.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Sub_admin, MainActivity.SubViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(MainActivity.SubViewHolder holder, final int position, final Sub_admin model) {
                holder.setName(model.getName());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String Code = model.getCode();
                        final String Name = model.getName();
                        final String Area = model.getArea();

                        if (collection.equals("collection"))
                        {
                            Intent intent = new Intent(AdminAreaActivity.this,AreaCollectionActivity.class);
                            intent.putExtra("area", Area);
                            startActivity(intent);
                        }
                        if (collection.equals("not_collection"))
                        {
                            Intent intent = new Intent(AdminAreaActivity.this,HomeActivity.class);
                            intent.putExtra("area", Area);
                            intent.putExtra("id", Code);
                            intent.putExtra("admin", "admin");
                            intent.putExtra("sub_admin_name", "admin");
                            startActivity(intent);
                        }

                    }
                });
                //  holder.setDesignation(model.getCode());
//                holder.setEmail(model.getEmail());
//                holder.setMobile(model.getMobile());
//                holder.setPhone(model.getPhone());
//                holder.setImage(getBaseContext(), model.getImage());
            }


            @Override
            public MainActivity.SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.sub_admin_rows, parent, false);

                return new MainActivity.SubViewHolder(view);
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
