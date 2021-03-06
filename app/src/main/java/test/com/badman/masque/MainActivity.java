package test.com.badman.masque;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Sub_admin, SubViewHolder> mPeopleRVAdapter;
    Dialog dialog;
    private DatabaseReference getUserDataReference;
    private String AdminCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mosqus");
        setSupportActionBar(toolbar);






        mDatabase = FirebaseDatabase.getInstance().getReference().child("subadmin");
        mDatabase.keepSynced(true);

        mPeopleRV =  findViewById(R.id.myRecycleView);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("subadmin");
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
                        final String Area =model.getArea();
                        final String sub_admin_name =model.getName();
                        final String sub_admin_id =model.getId();

                       // Toast.makeText(MainActivity.this, sub_admin_id, Toast.LENGTH_SHORT).show();

                            dialog = new Dialog(MainActivity.this);
                            dialog.setContentView(R.layout.dialog_code);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            final EditText write_code =      dialog.findViewById(R.id.write_code);
                            Button send_code =     dialog.findViewById(R.id.send_code);

                            dialog.show();


                            send_code.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final String User_code = write_code.getText().toString();
                                    if (Code.equals(User_code))
                                    {
                                        // Toast.makeText(MainActivity.this, "code match", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                                        intent.putExtra("area", Area);
                                        intent.putExtra("admin", "sub_admin");
                                        intent.putExtra("sub_admin_name", sub_admin_name);
                                        intent.putExtra("sub_admin_id", sub_admin_id);
                                        startActivity(intent);
                                    }
                                    else
                                        Toast.makeText(MainActivity.this, "Code Not Match \n Enter With Right Code", Toast.LENGTH_SHORT).show();

                                }
                            });

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




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.admin)
        {



            getUserDataReference = FirebaseDatabase.getInstance().getReference().child("admin").child("1");
          //  storeProfileImage = FirebaseStorage.getInstance().getReference().child("profile_images");

            getUserDataReference.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if (dataSnapshot.hasChild("code")) {
                                   // String name = dataSnapshot.child("applicant_name").getValue().toString();
                                   String code = dataSnapshot.child("code").getValue().toString();

                                   //   displayName.setText(name);
                                   //  Picasso.get().load(image).into(settingDisplayProfileImage);
                                   //   Picasso.get().load(imageUri).into(settingDisplayProfileImage);

                                   // Do stuff
                                   AdminCode =code;
                               } else {
                                   Toast.makeText(MainActivity.this, "Please Insert Your Image", Toast.LENGTH_SHORT).show();
                               }
                           }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_code);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final EditText write_code =      dialog.findViewById(R.id.write_code);
            Button send_code =     dialog.findViewById(R.id.send_code);

            dialog.show();


            send_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String User_code = write_code.getText().toString();
                    if (AdminCode.equals(User_code))
                    {
                        // Toast.makeText(MainActivity.this, "code match", Toast.LENGTH_SHORT).show();
                       // Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                       // startActivity(intent);
                       // startActivity(new Intent(MainActivity.this,AdminAreaActivity.class));
                        Intent intent = new Intent(MainActivity.this,AdminAreaActivity.class);
                        intent.putExtra("collection","not_collection");
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(MainActivity.this, "Code Not Match \n Enter With Right Code", Toast.LENGTH_SHORT).show();

                }
            });



        }      //  startActivity(new Intent(MainActivity.this,AdminAreaActivity.class));
            // Handle the camera action
         else if (id == R.id.nav_gallery)
         {

            Intent intent = new Intent(MainActivity.this,AdminAreaActivity.class);
            intent.putExtra("collection","collection");
            startActivity(intent);

        }
        else if (id == R.id.nav_subadmin)
        {

            Intent intent = new Intent(MainActivity.this,SubAdminHistoryActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
