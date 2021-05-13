package com.example.Subcept;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class homepg extends AppCompatActivity {

    SubscriptionsDatabase entriesDB = null;
    Toolbar toolbar;
    private Object FloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepg);
        // Set the toolbar as the actionbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.home_page_title);
        setSupportActionBar(toolbar);

        entriesDB = new SubscriptionsDatabase(this);

        if(entriesDB.length() == 0) {
            setFragmentBlankDatabase();

        } else {
            setFragmentSubscriptions();
        }

        FloatingActionButton = findViewById(R.id.FAB);

        View FAB = null;
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newSubscription = new Intent(homepg.this, new_subscription_Activity.class);
                startActivityForResult(newSubscription, 0);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(entriesDB.length() == 0) {
            setFragmentBlankDatabase();
        } else {
            setFragmentSubscriptions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // This is for when the new subscription activity returns with a new subscription
        if (resultCode == Activity.RESULT_OK && requestCode == 0 && data != null) {
            Subscriptions newSubscription = (Subscriptions)
                    data.getSerializableExtra("newSubscription");

            if(newSubscription.getFirstBillingDate() == -1){
                newSubscription.setFirstBillingDate(Subscriptions.today());
            }

            entriesDB.insertSubscription(newSubscription);
            setFragmentSubscriptions();
        }

        // This is for when the edit subscription activity returns with a updated subscription
        if (requestCode == 1 && data != null) {
            int index = data.getIntExtra("index", -1);

            if(resultCode == Activity.RESULT_OK) {
                Subscriptions newSubscription = (Subscriptions)
                        data.getSerializableExtra("subscription");
                entriesDB.replaceSubscription(newSubscription, index);
            }

            else if(resultCode == Activity.RESULT_CANCELED){
                entriesDB.removeRow(index);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homepg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_info_outline) {
            AlertDialog alertDialog = new AlertDialog.Builder(homepg.this).create();
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setTitle("Subcept");
            alertDialog.setMessage(getString(R.string.info_string));
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setFragmentBlankDatabase(){
        BlankDatabase frag = new BlankDatabase();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, frag).commit();
    }

    public void setFragmentSubscriptions(){
        Subscription_Section frag = new Subscription_Section();

        frag.setOnBecomesEmptyListener(new Subscription_Section().BecomesEmptyListener() {
            @Override
            public void onBecomesEmpty() {
                setFragmentBlankDatabase();
            }
        });

        frag.setOnSubscriptionClickListener(new SubscriptionsFragment.OnSubscriptionClickListener() {
            @Override
            public void onSubscriptionClick(Subscriptions subscription, int index, View view) {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(homepg.this, view, "subscriptionView");

                Intent launchActivity = new Intent(homepg.this, Edit_SubscriptionActivity.class);
                launchActivity.putExtra("subscription", subscription);
                launchActivity.putExtra("index", index);
                startActivityForResult(launchActivity, 1, options.toBundle());
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, frag).commit();
    }
}