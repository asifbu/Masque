package test.com.badman.masque;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private String Area;
    EditText search_edit_text;
    ImageButton mSearchBtn;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    Context context;
    ArrayList<String> name;
    ArrayList<String> address;
    ArrayList<String> mobile;
    ArrayList<String> id;
    ArrayList<String> paid;
 //   ArrayList<String> amount;
    ArrayList<String> image;



    SearchAdapter searchAdapter;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private android.support.v7.widget.Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //  extras = extras.getBundle("KEY_EXTRAS");
            String status = extras.getString("area");
           // String code = extras.getString("Code");
            Area=status;
          //  AreaCode=code;
        }


        mToolbar=findViewById(R.id.search_profile_toolbar);
        mToolbar.setTitle("Profile Search");
        setSupportActionBar(mToolbar);

        search_edit_text = (EditText) findViewById(R.id.search_field);
     //   mSearchBtn = (ImageButton) findViewById(R.id.search_button);
        recyclerView =  findViewById(R.id.search_list);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        name = new ArrayList<>();
        address = new ArrayList<>();
       id = new ArrayList<>();
        paid = new ArrayList<>();
        mobile = new ArrayList<>();
       image = new ArrayList<>();
//       amount = new ArrayList<>();




        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!s.toString().isEmpty())
                {
                    setAdapter(s.toString());
                } else
                    {
                    /*
                     * Clear the list when editText is empty
                     * */
                    name.clear();
                    address.clear();
                    id.clear();
                    mobile.clear();
                    paid.clear();
                    image.clear();
                  //  amount.clear();
                    recyclerView.removeAllViews();
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search_edit_text.setText(result.get(0));
                    String voice = result.get(0);
                }
                break;
            }

        }
    }



    private void setAdapter(final String searchedString)
    {
        databaseReference.child("area_member").child(Area).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                /*
                 * Clear the list for every new search
                 * */
                name.clear();
                address.clear();
                id.clear();
                mobile.clear();
                paid.clear();
                image.clear();
              //  amount.clear();
                recyclerView.removeAllViews();

                int counter = 0;

                /*
                 * Search all users for matching searched string
                 * */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   // String uid = snapshot.getKey();
                    String full_name = snapshot.child("name").getValue(String.class);
                    String address1 = snapshot.child("address").getValue(String.class);
                    String id1 = snapshot.child("id").getValue(String.class);
                    String mobile1 = snapshot.child("mobile").getValue(String.class);
                    String paid1 = snapshot.child("paid").getValue(String.class);
                    String image1 = snapshot.child("image").getValue(String.class);
//                    String amount1 = snapshot.child("amount").getValue(String.class);

                    if (full_name.toLowerCase().contains(searchedString.toLowerCase()))
                    {
                        name.add(full_name);
                        id.add(id1);
                        address.add(address1);
                        mobile.add(mobile1);
                        paid.add(paid1);
                        image.add(image1);
                     //   amount.add(amount1);
                        counter++;
                    }
                    else if (paid1.toLowerCase().contains(searchedString.toLowerCase()))
                    {
                        name.add(full_name);
                        id.add(id1);
                        address.add(address1);
                        mobile.add(mobile1);
                        paid.add(paid1);
                        image.add(image1);
                     //   amount.add(amount1);
                        counter++;
                    }
                    else if (mobile1.toLowerCase().contains(searchedString.toLowerCase())) {
                        mobile.add(mobile1);
                        id.add(id1);
                        address.add(address1);
                        name.add(full_name);
                        paid.add(paid1);
                        image.add(image1);
                        //   amount.add(amount1);
                        counter++;

                    }
                    /*
                     * Get maximum of 15 searched results only
                     * */
                    if (counter == 15)
                        break;
                }

                searchAdapter = new SearchAdapter(SearchActivity.this, name,address,mobile ,id ,paid,image);
                //amount,image
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
