package com.example.Subcept;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import java.text.DecimalFormat;
import java.util.ArrayList;

public class Subscription_Section extends Fragment { private SubscriptionsDatabase entriesDB = null;

    private LinearLayout subscriptionsContainer;
    private Typeface fontAwesome;
    private View mainView;
    private Context context;

    ArrayList<BecomesEmptyListener> listeners = new ArrayList<BecomesEmptyListener> ();
    ArrayList<OnSubscriptionClickListener> subscriptionClickListeners = new ArrayList<OnSubscriptionClickListener> ();

    public void setOnBecomesEmptyListener (BecomesEmptyListener listener) {
        this.listeners.add(listener);
    }

    public interface BecomesEmptyListener {
        void onBecomesEmpty();
    }

    public void setOnSubscriptionClickListener (OnSubscriptionClickListener listener) {
        // Store the listener object
        this.subscriptionClickListeners.add(listener);
    }

    public interface OnSubscriptionClickListener {
        void onSubscriptionClick(Subscriptions subscription, int index, View view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.subscriptions_section, container, false);
        ScrollView scrollView = mainView.findViewById(R.id.subscriptions);
        subscriptionsContainer = scrollView.findViewById(R.id.subscriptionsContainer);

        context = getContext();

        fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");

        entriesDB = new SubscriptionsDatabase(getActivity());
        entriesDB.setOnDataChanged(new SubscriptionsDatabase.DataChangeListener() {
            @Override
            public void onDataChanged(int index, int typeOfChange) {
                updateSubscriptionsFragment(index, typeOfChange);
            }
        });

        updateSubscriptionsFragment(-1, -1);

        return mainView;
    }

    public void updateSubscriptionsFragment(int index, int typeOfChange) {
        if(isAdded()) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            String payment = decimalFormat.format(entriesDB.getTotalPayment());
            String newText = getResources().getString(R.string.monthly_payment) + payment;

            TextView paymentTextView = mainView.findViewById(R.id.paymentTextView);
            paymentTextView.setText(newText);

            View[] subscriptionViews = convertSubscriptionsToViews(entriesDB.getSubscriptions());

            if (typeOfChange == -1) {
                fillSubscriptionsInActivity(subscriptionViews);
            }
            else if(typeOfChange == entriesDB.REMOVED){
                subscriptionsContainer.removeViewAt(index);
                if(subscriptionsContainer.getChildCount() == 0){
                    for(BecomesEmptyListener listener: listeners) {
                        listener.onBecomesEmpty();
                    }
                }
            }else if(typeOfChange == entriesDB.INSERTED){
                subscriptionsContainer.addView(subscriptionViews[index], 0);
            }else if(typeOfChange == entriesDB.REPLACED){
                subscriptionsContainer.removeViewAt(index);
                subscriptionsContainer.addView(subscriptionViews[index], index);
            }
        }
    }

    public View[] convertSubscriptionsToViews(final Subscriptions[] subscriptions){
        View[] results = new View[subscriptions.length];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 45);

        for(int i=0; i < results.length; ++i){
            final View newView = subscriptions[i].getView(context, fontAwesome);

            newView.setLayoutParams(layoutParams);

            newView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int index = subscriptionsContainer.indexOfChild(view);

                    Delete_Subscription deleteDialog = new Delete_Subscription(context, index);
                    deleteDialog.setOnDeleteClickedListener(new Delete_Subscription.OnDeleteClicked() {
                        @Override
                        public void onDeleteClicked(int index) {
                            entriesDB.removeRow(index);
                        }
                    });

                    deleteDialog.show();

                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(10);

                    return true;
                }
            });

            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = subscriptionsContainer.indexOfChild(view);
                    Subscriptions subscription = entriesDB.getSubscriptions()[index];

                    for(OnSubscriptionClickListener listener: subscriptionClickListeners){
                        listener.onSubscriptionClick(subscription, index, view);
                    }
                }
            });

            results[i] = newView;
        }

        return results;
    }

    public void fillSubscriptionsInActivity(View[] subscriptions) {
        subscriptionsContainer.removeAllViews();

        if(subscriptions.length != 0) {
            for (View subscription : subscriptions) {
                subscriptionsContainer.addView(subscription);//, subscription.getLayoutParams());
            }
        }

        else {
            for(BecomesEmptyListener listener: listeners) {
                listener.onBecomesEmpty();
            }
        }
    }
}
