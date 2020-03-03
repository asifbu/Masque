package test.com.badman.masque;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class PaidCheckActivity extends AppCompatActivity
{
    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_check);




        mToolbar = findViewById(R.id.paid_toolbar);
        mToolbar.setTitle("Paid Check");
        setSupportActionBar(mToolbar);

        BottomNavigationView bottomNav = findViewById(R.id.paid_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_paid_container,
                    new PaidFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.paid_menu:
                            selectedFragment = new PaidFragment();
                            break;
                        case R.id.unpaid_menu:
                            selectedFragment = new UnpaidFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_paid_container,
                            selectedFragment).commit();

                    return true;
                }
            };





}
