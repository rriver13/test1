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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;

public class Custom_SubscriptionActivity extends AppCompatActivity {
    private Subscriptions newSubscription;

    private View subscription;
    private Typeface fontAwesome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_form_custom);

        final Toolbar toolbar = findViewById(R.id.edit_subscription_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        Button deleteSubscription = findViewById(R.id.deleteSubscription);
        deleteSubscription.setVisibility(View.GONE);

        newSubscription = new Subscriptions(R.drawable.wallet, getResources().getColor(R.color.black),
                "", "", BigDecimal.valueOf(0f), Subscriptions.billingCycle.MONTHLY, -1, 0,
                Subscriptions.reminders.NEVER, FireStoreDatabase.CUSTOM_TYPE);

        final EditText serviceName = findViewById(R.id.serviceName);
        serviceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newSubscription.setName(charSequence.toString());
                newSubscription.fillOutView(subscription, fontAwesome);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final EditText description = findViewById(R.id.description);
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newSubscription.setDescription(charSequence.toString());
                newSubscription.fillOutView(subscription, fontAwesome);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final EditText amount = findViewById(R.id.amount);
        amount.setText(newSubscription.getAmountString());
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0) {
                    try {
                        if (charSequence.charAt(0) != '$') {
                            String value = charSequence.toString();
                            newSubscription.setAmount(new BigDecimal(value));
                            newSubscription.fillOutView(subscription, fontAwesome);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    newSubscription.setAmount(BigDecimal.valueOf(0f));
                    newSubscription.fillOutView(subscription, fontAwesome);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    amount.setText(newSubscription.getAmountString());
                }else{
                    if(newSubscription.getAmount().floatValue() == 0f){
                        amount.setText("");
                    } else {
                        amount.setText(String.format(Locale.US, "%.2f", newSubscription.getAmount()));
                    }
                }
            }
        });

        final EditText billingCycle = findViewById(R.id.billingCycle);
        billingCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BillingCycle frag = new BillingCycle();
                frag.show(getSupportFragmentManager(), "billing_cycle");
                frag.setOnFinishedListener(new BillingCycle.OnFinishedListener() {
                    @Override
                    public void onFinishedWithResult(int index, String name) {
                        newSubscription.setBillingCycleID(index);
                        newSubscription.fillOutView(subscription, fontAwesome);
                        billingCycle.setText(name);
                    }
                });
            }
        });

        final EditText firstBillingDate = findViewById(R.id.firstBillingDate);
        firstBillingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstBillingDate frag = new FirstBillingDate();

                Bundle args = new Bundle();
                args.putLong("date_in_milliseconds", newSubscription.getFirstBillingDate());
                frag.setArguments(args);

                frag.setOnFinishedListener(new FirstBillingDate.OnFinishedListener() {
                    @Override
                    public void onFinishedWithResult(String monthName, int day, int year, long time) {
                        String date = String.format(Locale.getDefault(),
                                "%s %d, %d", monthName, day, year);
                        firstBillingDate.setText(date);
                        newSubscription.setFirstBillingDate(time);
                        newSubscription.fillOutView(subscription, fontAwesome);
                    }
                });

                frag.show(getSupportFragmentManager(), "date_selector");
            }
        });

        final EditText reminders = findViewById(R.id.reminders);
        reminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reminders frag = new Reminders();
                frag.show(getSupportFragmentManager(), "reminders");
                frag.setOnFinishedListener(new Reminders.OnFinishedListener() {
                    @Override
                    public void onFinishedWithResult(int index, String name) {
                        newSubscription.setReminderID(index);
                        newSubscription.fillOutView(subscription, fontAwesome);
                        reminders.setText(name);
                    }
                });
            }
        });

        ViewStub subscriptionStubView = findViewById(R.id.viewStub);
        subscription = subscriptionStubView.inflate();

        fontAwesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        newSubscription.fillOutView(subscription, fontAwesome);

        // To change the icon of the subscription
        ImageView icon = subscription.findViewById(R.id.icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeIcon frag = new ChangeIcon();

                Bundle args = new Bundle();

                args.putInt("icon_id", newSubscription.getIconID());

                frag.setArguments(args);

                frag.setOnFinishedListener(new ChangeIcon.OnFinishedListener() {
                    @Override
                    public void onFinishedWithResult(int iconId) {
                        newSubscription.setIconID(iconId);
                        newSubscription.fillOutView(subscription, fontAwesome);
                    }
                });

                frag.show(getSupportFragmentManager(), "change_icon");
            }
        });

        // To change the color of the subscription
        subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeColor frag = new ChangeColor();

                Bundle args = new Bundle();
                args.putInt("color", newSubscription.getColor());

                frag.setArguments(args);

                frag.setOnFinishedListener(new ChangeColor.OnFinishedListener() {
                    @Override
                    public void onFinishedWithResult(int color) {
                        newSubscription.setColor(color);
                        newSubscription.fillOutView(subscription, fontAwesome);
                    }
                });

                frag.show(getSupportFragmentManager(), "change_color");
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void setContentView(int subscription_form_custom) {
    }

    private long today(){
        return Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_subscription, menu);
        return true;
    }

    public void createSubscription(MenuItem item) {
        Intent resultIntent = new Intent();

        resultIntent.putExtra("newSubscription", newSubscription);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}


