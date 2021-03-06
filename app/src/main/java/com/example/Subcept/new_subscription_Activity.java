package com.example.Subcept;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.math.BigDecimal;

public class new_subscription_Activity extends AppCompatActivity {
    Typeface fontAwesome = null;

    private Subscriptions[] brandSubscriptions;

    private Subscriptions[] displayedSubscriptions;
    private LinearLayout subscriptionsContainer;

    View blankView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subscription);
        Toolbar toolbar = findViewById(R.id.new_subscription_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        fontAwesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        ScrollView scrollView = findViewById(R.id.subscriptionsScrollView);
        scrollView.setVerticalScrollBarEnabled(false);

        subscriptionsContainer = scrollView.findViewById(R.id.subscriptionsScrollViewContent);

        displayedSubscriptions = brandSubscriptions.clone();

        fillSubscriptionsInActivity(displayedSubscriptions);

        View blankView = View.inflate(this, R.layout.no_templates_found, null);
        subscriptionsContainer.addView(blankView);
        blankView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_subscription, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchBrandSubscriptions(query);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        searchBrandSubscriptions(query);

                        return false;
                    }
                }
        );

        return true;
    }

    public void searchBrandSubscriptions(String query) {
        // This variable is used to track if the query finds any results
        boolean anyResults = true;

        for (int i = 0; i < brandSubscriptions.length; i++) {
            String name = brandSubscriptions[i].getName().toLowerCase();

            // Stores "Does this element at this index match the query?"
            boolean found = name.contains(query.toLowerCase());

            int visibility = found ? View.VISIBLE : View.GONE;
            subscriptionsContainer.getChildAt(i).setVisibility(visibility);

            anyResults &= !found;
        }

        blankView.setVisibility(anyResults ? View.VISIBLE : View.GONE);
    }

    public void fillSubscriptionsInActivity(Subscriptions[] subscriptions) {
        displayedSubscriptions = subscriptions.clone();

        subscriptionsContainer.removeAllViewsInLayout();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 45);

        if (subscriptions.length != 0) {

            for (int i = 0; i < subscriptions.length; i++) {
                View newView = brandSubscriptions[i].getView(this, fontAwesome);
                newView.findViewById(R.id.nextPaymentDate).setVisibility(View.GONE);

                newView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startTemplateSubscriptionActivity(view);
                    }
                });
                subscriptionsContainer.addView(newView, layoutParams);
            }
        } else {
            View blankView = View.inflate(this, R.layout.no_templates_found, null);
            subscriptionsContainer.addView(blankView);
        }
    }

    public void startCustomSubscriptionActivity(View view) {
        Intent launchActivity = new Intent(this, Custom_SubscriptionActivity.class);
        startActivityForResult(launchActivity, 0);
    }

    public void startTemplateSubscriptionActivity(View view) {
        Intent launchActivity = new Intent(new_subscription_Activity.this,
                SubscriptionTemplateActivity.class);

        int index = subscriptionsContainer.indexOfChild(view);

        Subscriptions templateSubscription = displayedSubscriptions[index];
        templateSubscription.setAmount(BigDecimal.valueOf(0f));

        launchActivity.putExtra("subscription", templateSubscription);

        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, view, "subscriptionView");

        startActivityForResult(launchActivity, 0, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 0 && data != null) {
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }
}

