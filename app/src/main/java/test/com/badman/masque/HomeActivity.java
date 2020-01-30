package test.com.badman.masque;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity
{
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Member, SubViewHolder> mPeopleRVAdapter;
//    Dialog dialog;

    private FirebaseAuth mAuth;
    private DatabaseReference getUserDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


       // mAuth = FirebaseAuth.getInstance();

     //   Button button =findViewById(R.id.open);
     //   final TextView textView = findViewById(R.id.text_name);




//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                    getUserDataReference = FirebaseDatabase.getInstance().getReference().child("sub").child("1");
//                   // getUserData = FirebaseDatabase.getInstance().getReference().child("seatplan").child(online_user_id);
//
//
//
//                    getUserDataReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                        {
//                            String startnumber = dataSnapshot.child("name").getValue().toString();
//                           // String Room = dataSnapshot.child("room").getValue().toString();
//                           // String endnumber = dataSnapshot.child("endRoll").getValue().toString();
//                            //  String otherunit = dataSnapshot.child("other_unit").getValue().toString();
//
//
//                                textView.setText(startnumber);
//
////                            showText2.setText(year);
//                            //displayMainUnit.setText(mainunit);
//                            // displayOtherUnit.setText(otherunit);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//                }
//
//
//
//
//
//
//
//
//        });

        mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("members");
        mDatabase.keepSynced(true);

        mPeopleRV =  findViewById(R.id.home_recycleview);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("members");
        Query personsQuery = personsRef.orderByKey();

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Member>().setQuery(personsQuery, Member.class).build();

        mPeopleRVAdapter = new FirebaseRecyclerAdapter<Member, HomeActivity.SubViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(HomeActivity.SubViewHolder holder, final int position, final Member model) {
                holder.setName(model.getStrName());
              //  final String current_user_Id = mAuth.getCurrentUser().getUid();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Name = model.getStrName();
                        String Id = model.getStrID();

                        Intent intent = new Intent(HomeActivity.this,MemberProfileActivity.class);
                        intent.putExtra("name", Name);
                        intent.putExtra("id",Id);
                        startActivity(intent);

                    }
                });
             //   holder.setCode(model.getCode());
                //  holder.setDesignation(model.getCode());
//                holder.setEmail(model.getEmail());
//                holder.setMobile(model.getMobile());
//                holder.setPhone(model.getPhone());
//                holder.setImage(getBaseContext(), model.getImage());
            }


            @Override
            public HomeActivity.SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.sub_admin_rows, parent, false);

                return new HomeActivity.SubViewHolder(view);
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
            TextView sub_admin = mView.findViewById(R.id.sub_admin_name);
            sub_admin.setText(name);
        }
    }


}
