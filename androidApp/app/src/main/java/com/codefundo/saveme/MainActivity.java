package com.codefundo.saveme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codefundo.saveme.models.CampData;
import com.codefundo.saveme.models.UserData;
import com.codefundo.saveme.models.VictimData;
import com.codefundo.saveme.models.VolunteerData;
import com.codefundo.saveme.victimpanel.MapActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_END;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private MobileServiceClient mClient;
    BottomAppBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = findViewById(R.id.bar);
        setSupportActionBar(bar);
        startService(new Intent(this, MyService.class));
        mClient = SaveMe.getAzureClient(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MapActivity.class)));

        bar.setOnMenuItemClickListener(this);

        //pushVictimData();
        //pushCampData();
        //pushVolunteerData();
        //checkDataRefresh();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragments, HomeFragment.newInstance(), "HomeFragment").commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void checkDataRefresh() {
        MobileServiceTable<UserData> table = mClient.getTable(UserData.class);
        table.where().execute((result, count, exception, response) -> {
            for (UserData data : result)
                Log.d("USER", data.getId());
        });
    }

    private void pushVictimData() {
        for (int i = 0; i < 100; i++) {
            MobileServiceTable<VictimData> table = mClient.getTable(VictimData.class);
            VictimData victimData = new VictimData();

            Random random = new Random();
            victimData.setId(Long.toHexString(random.nextLong()));
            victimData.setAzureId(Long.toHexString(random.nextLong()));
            victimData.setId(Long.toHexString(random.nextLong()));
            victimData.setCurrentLat((random.nextDouble() * -180.0) + 90.0);
            victimData.setCurrentLong((random.nextDouble() * -180.0) + 90.0);
            victimData.setStatus("danger");

            table.insert(victimData);
        }
    }

    private void pushCampData() {
        for (int i = 0; i < 40; i++) {
            MobileServiceTable<CampData> table = mClient.getTable(CampData.class);
            CampData campData = new CampData();

            Random random = new Random();
            campData.setId(Long.toHexString(random.nextLong()));
            campData.setCreatorAzureId(Long.toHexString(random.nextLong()));
            campData.setName(Long.toHexString(random.nextLong()));
            campData.setLatitude((random.nextDouble() * -180.0) + 90.0);
            campData.setLongitude((random.nextDouble() * -180.0) + 90.0);
            campData.setType(getCampType(random.nextInt()));

            table.insert(campData);
        }

    }

    private void pushVolunteerData() {
        for (int i = 0; i < 50; i++) {
            MobileServiceTable<VolunteerData> table = mClient.getTable(VolunteerData.class);
            VolunteerData volunteerData = new VolunteerData();

            Random random = new Random();
            volunteerData.setId(Long.toHexString(random.nextLong()));
            volunteerData.setAzureId(Long.toHexString(random.nextLong()));
            volunteerData.setCurrentLat((random.nextDouble() * -180.0) + 90.0);
            volunteerData.setCurrentLong((random.nextDouble() * -180.0) + 90.0);
            volunteerData.setCurrentStatus("working");

            table.insert(volunteerData);
        }
    }

    private String getCampType(int i) {
        if (i % 2 == 0) {
            return "Medical Help";
        } else {
            return "Food Camp";
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragments, HomeFragment.newInstance(), "HomeFragment").commit();
                break;
            case R.id.nav_user:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragments, UserFragment.newInstance(), "UserFragment").commit();
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (bar.getFabAlignmentMode() == FAB_ALIGNMENT_MODE_END) {
            bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        } else {
            super.onBackPressed();
        }
    }
}
