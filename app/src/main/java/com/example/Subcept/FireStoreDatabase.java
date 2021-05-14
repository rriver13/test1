package com.example.Subcept;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FireStoreDatabase {
    static final ArrayList<DataChangeListener> listeners = new ArrayList<>();
    final public int REMOVED = 0, REPLACED = 1, INSERTED = 2;
    private static final String TAG = "Subscription";
    private FirebaseFirestore mFirestore;
    private String sID;
    private String sCOLOR;
    private String sICON_TEXT;
    private String sICON_IMAGE;
    private String sNAME;
    private String sDESCRIPTION;
    private String sAMOUNT;
    private String sBILLING_CYCLE;
    private String sBILLING_DATE;
    private String sNEXT_BILLING_DATE;
    private String sREMINDER;
    private String sTYPE;

    public static final int CUSTOM_TYPE = 0, TEMPLATE_TYPE = 1;

    public FireStoreDatabase(Edit_SubscriptionActivity edit_subscriptionActivity) {

    }


    public void addData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("Amount", "9.99");
        user.put("Billing Cycle", "");
        user.put("Billing Data", "");
        user.put("Color", " ");
        user.put("Description", " ");
        user.put("ID", "");
        user.put("Icon Image", " ");
        user.put("Icon Text", " ");
        user.put("Name", " ");
        user.put(" Next Billing Date", " ");
        user.put("Type", " ");

        db.collection("AppT")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with iD " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error adding document", e);
                    }
                });

    }
    public void getSubscriptions () {
        mFirestore.collection("App2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());
                            }
                        } else {
                            Log.w(TAG, "ERROR GETTING DOCUMENT", task.getException());
                        }
                    }
                });
    }


    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsCOLOR() {
        return sCOLOR;
    }

    public void setsCOLOR(String sCOLOR) {
        this.sCOLOR = sCOLOR;
    }

    public String getsICON_TEXT() {
        return sICON_TEXT;
    }

    public void setsICON_TEXT(String sICON_TEXT) {
        this.sICON_TEXT = sICON_TEXT;
    }

    public String getsICON_IMAGE() {
        return sICON_IMAGE;
    }

    public void setsICON_IMAGE(String sICON_IMAGE) {
        this.sICON_IMAGE = sICON_IMAGE;
    }

    public String getsNAME() {
        return sNAME;
    }

    public void setsNAME(String sNAME) {
        this.sNAME = sNAME;
    }

    public String getsDESCRIPTION() {
        return sDESCRIPTION;
    }

    public void setsDESCRIPTION(String sDESCRIPTION) {
        this.sDESCRIPTION = sDESCRIPTION;
    }

    public String getsAMOUNT() {
        return sAMOUNT;
    }

    public void setsAMOUNT(String sAMOUNT) {
        this.sAMOUNT = sAMOUNT;
    }

    public String getsBILLING_CYCLE() {
        return sBILLING_CYCLE;
    }

    public void setsBILLING_CYCLE(String sBILLING_CYCLE) {
        this.sBILLING_CYCLE = sBILLING_CYCLE;
    }

    public String getsBILLING_DATE() {
        return sBILLING_DATE;
    }

    public void setsBILLING_DATE(String sBILLING_DATE) {
        this.sBILLING_DATE = sBILLING_DATE;
    }

    public String getsNEXT_BILLING_DATE() {
        return sNEXT_BILLING_DATE;
    }

    public void setsNEXT_BILLING_DATE(String sNEXT_BILLING_DATE) {
        this.sNEXT_BILLING_DATE = sNEXT_BILLING_DATE;
    }

    public String getsREMINDER() {
        return sREMINDER;
    }

    public void setsREMINDER(String sREMINDER) {
        this.sREMINDER = sREMINDER;
    }

    public String getsTYPE() {
        return sTYPE;
    }

    public void setsTYPE(String sTYPE) {
        this.sTYPE = sTYPE;
    }

    public FireStoreDatabase(String sID, String sCOLOR, String sICON_TEXT, String sICON_IMAGE, String sNAME,
                             String sDESCRIPTION, String sAMOUNT, String sBILLING_CYCLE,
                             String sBILLING_DATE, String sNEXT_BILLING_DATE, String sREMINDER,
                             String sTYPE) {

        this.sAMOUNT = sAMOUNT;
        this.sBILLING_CYCLE = sBILLING_CYCLE;
        this.sBILLING_DATE = sBILLING_DATE;
        this.sICON_IMAGE = sICON_IMAGE;
        this.sDESCRIPTION = sDESCRIPTION;
        this.sICON_TEXT = sICON_TEXT;
        this.sID = sID;
        this.sCOLOR = sCOLOR;
        this.sBILLING_DATE = sBILLING_DATE;
        this.sNEXT_BILLING_DATE = sNEXT_BILLING_DATE;
        this.sTYPE = sTYPE;
        this.sREMINDER = sREMINDER;
    }

    public int getTotalPayment(int x) {
        return x ;
    }

    public void insertSubscription(Subscriptions entry) {

    }

    public void removeRow(int index) {
    }
    public int getDatabaseID(int index) {
        return index;
    }

    public void setOnDataChanged(DataChangeListener listener) {
        // Store the listener object
        listeners.add(listener);
    }

    public interface DataChangeListener {
        void onDataChanged(int index, int type);
    }

    public void replaceSubscription(Subscriptions entry, int index) {
        setAlarmForNotification(index, true);
        notifyDataChange(new int[]{index}, REPLACED);
    }

    public void setAlarmForNotification(int index, boolean displayNotification) {
        Subscriptions setAlarm = null;

        Context context = null;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        long thisTime = setAlarm.getNextBillingDate();

        int id = getDatabaseID(index);
        intent.putExtra("id", id);
        intent.putExtra("subscription", setAlarm);
        intent.putExtra("index", index);
        intent.putExtra("time", thisTime);

        Subscriptions.reminders reminder =
                Subscriptions.reminders.values()[setAlarm.getReminderID()];

        if (displayNotification && reminder != Subscriptions.reminders.NEVER) {
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(thisTime);

            if (reminder == Subscriptions.reminders.ONE_DAY) {
                c.add(Calendar.DATE, -1);
            } else if (reminder == Subscriptions.reminders.TWO_DAYS) {
                c.add(Calendar.DATE, -2);
            } else if (reminder == Subscriptions.reminders.THREE_DAYS) {
                c.add(Calendar.DATE, -3);
            } else if (reminder == Subscriptions.reminders.ONE_WEEK) {
                c.add(Calendar.WEEK_OF_YEAR, -1);
            } else if (reminder == Subscriptions.reminders.TWO_WEEKS) {
                c.add(Calendar.WEEK_OF_YEAR, -2);
            } else if (reminder == Subscriptions.reminders.ONE_MONTH) {
                c.add(Calendar.MONTH, -1);
            }

            thisTime = c.getTimeInMillis();

            Log.e("alarm", String.format(Locale.US, "%d", thisTime));
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, thisTime, alarmIntent);
        } else {
            PendingIntent displayIntent = PendingIntent.getBroadcast(
                    context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            if (displayIntent != null) {
                alarmManager.cancel(displayIntent);
                displayIntent.cancel();
            }
        }

        // Set an alarm to go off at mNextBillingDate, repeating, displays a notification;
        // If display notification is true, change the alarm for this subscription.
        // If display notification is false, remove the alarm for this subscription.
    }
    public void getSubscription() {


    }



                public void notifyDataChange(int[] indexes, int type) {
                    for (DataChangeListener listener : listeners) {
                        for (int index : indexes) {
                            listener.onDataChanged(index, type);
                        }
            }
        }
    }



