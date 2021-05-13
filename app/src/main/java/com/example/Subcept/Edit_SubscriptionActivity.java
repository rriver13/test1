package com.example.Subcept;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;
import java.util.Locale;

public class Edit_SubscriptionActivity extends AppCompatActivity { Subscriptions subscription;
    int index;
    View subscriptionView;

    private Typeface fontAwesome;

    @Override
    public void onBackPressed() {
        subscriptionView.findViewById(R.id.the_Layout).setVisibility(View.GONE);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getIntent().getExtras();
        subscription = (Subscriptions)args.getSerializable("subscription");
        index = args.getInt("index");

        fontAwesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        if(!(subscription == null)){
            if(!(subscription.getSubscriptionType() == SubscriptionsDatabase.CUSTOM_TYPE)) {
                if(subscription.getIconID() == -1){
                    setContentView(R.layout.subscription_form_text_template);
                }else{
                    setContentView(R.layout.subscription_form_image_template);
                }
            }
            else{
                setContentView(R.layout.subscription_form_custom);

                final EditText serviceName = (EditText)findViewById(R.id.serviceName);
                serviceName.setText(subscription.getName());
                serviceName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        subscription.setName(charSequence.toString());
                        subscription.fillOutView(subscriptionView, fontAwesome);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            final EditText description = (EditText)findViewById(R.id.description);
            description.setText(subscription.getDescription());
            description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    subscription.setDescription(charSequence.toString());
                    subscription.fillOutView(subscriptionView, fontAwesome);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            final EditText amount = (EditText)findViewById(R.id.amount);
            amount.setText(subscription.getAmountString());
            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(charSequence.length() != 0) {
                        try {
                            if (charSequence.charAt(0) != '$') {
                                BigDecimal value = new BigDecimal(charSequence.toString());
                                subscription.setAmount(value);
                                subscription.fillOutView(subscriptionView, fontAwesome);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        subscription.setAmount(BigDecimal.valueOf(0));
                        subscription.fillOutView(subscriptionView, fontAwesome);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean focus) {
                    if(!focus){
                        amount.setText(subscription.getAmountString());
                    }else{
                        if(subscription.getAmount().floatValue() == 0){
                            amount.setText("");
                        } else {
                            amount.setText(String.format(Locale.US, "%.2f", subscription.getAmount()));
                        }
                    }
                }
            });

            final EditText billingCycle = (EditText)findViewById(R.id.billingCycle);
            billingCycle.setText(subscription.getBillingCycleString(this));
            billingCycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BillingCycle frag = new BillingCycle();
                    frag.show(getSupportFragmentManager(), "billing_cycle");
                    frag.setOnFinishedListener(new BillingCycle.OnFinishedListener() {
                        @Override
                        public void onFinishedWithResult(int index, String name) {
                            subscription.setBillingCycleID(index);
                            subscription.fillOutView(subscriptionView, fontAwesome);
                            billingCycle.setText(name);
                        }
                    });
                }
            });

            final EditText firstBillingDate = (EditText)findViewById(R.id.firstBillingDate);
            firstBillingDate.setText(subscription.getFirstBillingDateString(this));
            firstBillingDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirstBillingDate frag = new FirstBillingDate();

                    Bundle args = new Bundle();
                    args.putLong("date_in_milliseconds", subscription.getFirstBillingDate());
                    frag.setArguments(args);

                    frag.setOnFinishedListener(new FirstBillingDate.OnFinishedListener() {
                        @Override
                        public void onFinishedWithResult(String monthName, int day, int year, long time) {
                            String date = String.format(Locale.getDefault(),
                                    "%s %d, %d", monthName, day, year);
                            firstBillingDate.setText(date);
                            subscription.setFirstBillingDate(time);
                            subscription.fillOutView(subscriptionView, fontAwesome);
                        }
                    });

                    frag.show(getSupportFragmentManager(), "date_selector");
                }
            });

            final EditText reminders = (EditText)findViewById(R.id.reminders);
            reminders.setText(subscription.getReminderString(this));
            reminders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reminders frag = new Reminders();
                    frag.show(getSupportFragmentManager(), "reminders");
                    frag.setOnFinishedListener(new Reminders.OnFinishedListener() {
                        @Override
                        public void onFinishedWithResult(int index, String name) {
                            subscription.setReminderID(index);
                            subscription.fillOutView(subscriptionView, fontAwesome);
                            reminders.setText(name);
                        }
                    });
                }
            });
        }

        final Toolbar toolbar = (Toolbar)findViewById(R.id.edit_subscription_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.the_Layout).setVisibility(View.GONE);
                supportFinishAfterTransition();
            }
        });

        subscriptionView = ((ViewStub)findViewById(R.id.viewStub)).inflate();
        subscription.fillOutView(subscriptionView, fontAwesome);

        if(subscription.getSubscriptionType() == SubscriptionsDatabase.CUSTOM_TYPE){
            ImageView icon = subscriptionView.findViewById(R.id.icon);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangeIcon frag = new ChangeIcon();

                    Bundle args = new Bundle();
                    args.putInt("icon_id", subscription.getIconID());

                    frag.setArguments(args);

                    frag.setOnFinishedListener(new ChangeIcon.OnFinishedListener() {
                        @Override
                        public void onFinishedWithResult(int iconId) {
                            subscription.setIconID(iconId);
                            subscription.fillOutView(subscriptionView, fontAwesome);
                        }
                    });

                    frag.show(getSupportFragmentManager(), "change_icon");
                }
            });

            subscriptionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangeColor frag = new ChangeColor();

                    Bundle args = new Bundle();
                    args.putInt("color", subscription.getColor());

                    frag.setArguments(args);

                    frag.setOnFinishedListener(new ChangeColor.OnFinishedListener() {
                        @Override
                        public void onFinishedWithResult(int color) {
                            subscription.setColor(color);
                            subscription.fillOutView(subscriptionView, fontAwesome);
                        }
                    });

                    frag.show(getSupportFragmentManager(), "change_color");
                }
            });
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_subscription, menu);
        return true;
    }

    public void createSubscription(MenuItem item) {
        Intent resultIntent = new Intent();

        resultIntent.putExtra("subscription", subscription);
        resultIntent.putExtra("index", index);

        setResult(Activity.RESULT_OK, resultIntent);
        findViewById(R.id.the_Layout).setVisibility(View.GONE);
        supportFinishAfterTransition();
    }

    public void deleteSubscription(View view) {
        final SubscriptionsDatabase entriesDB = new SubscriptionsDatabase(this);  // SQL DATABASE MUST CHANGE

        Delete_Subscription deleteDialog = new Delete_Subscription(this, index);
        deleteDialog.setOnDeleteClickedListener(new Delete_Subscription.OnDeleteClicked() {
            @Override
            public void onDeleteClicked(int index) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("index", index);
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        deleteDialog.show();
    }
}