package com.example.Subcept;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

    public class Subscriptions implements Serializable {
        private String sIconText;
        private int    sIconID;
        private int    sColor;
        private String sName;
        private String sDescription;
        private BigDecimal sAmount;
        private int    sBillingCycleID;
        private long   sFirstBillingDate;
        private long   sNextBillingDate;
        private int    sReminderID;
        private final int    sSubscriptionType;

        private String mAmountString;

        public enum billingCycle{
            WEEKLY(0), MONTHLY(1), QUARTERLY(2), YEARLY(3);

            public final int value;
            billingCycle(int value){
                this.value = value;
            }
        }

        public enum reminders{
            NEVER(0), ONE_DAY(1), TWO_DAYS(2), THREE_DAYS(3), ONE_WEEK(4), TWO_WEEKS(5), ONE_MONTH(6);

            public final int value;
            reminders(int value){
                this.value = value;
            }
        }

        public Subscriptions(int IconID, int color, String name, String description, BigDecimal amount,
                             billingCycle billingCycle, long firstBillingDate, long nextBillingDate,
                             reminders reminder, int subscriptionType) {

            sIconID           = IconID;
            sIconText         = "";
            sColor            = color;
            sName             = name;
            sDescription      = description;
            sBillingCycleID   = billingCycle.value;
            sFirstBillingDate = firstBillingDate;
            sNextBillingDate  = nextBillingDate;
            sReminderID       = reminder.value;
            setAmount(amount);
            sSubscriptionType = subscriptionType;
        }

        public Subscriptions(String IconText, int color, String name, String description, BigDecimal amount,
                             billingCycle billingCycle, long firstBillingDate, long nextBillingDate,
                             reminders reminder, int subscriptionType) {
            sIconID           = -1;
            sIconText         = IconText;
            sColor            = color;
            sName             = name;
            sDescription      = description;
            sBillingCycleID   = billingCycle.value;
            sFirstBillingDate = firstBillingDate;
            sNextBillingDate  = nextBillingDate;
            sReminderID       = reminder.value;
            setAmount(amount);
            sSubscriptionType = subscriptionType;
        }

        public View getView(new_subscription_Activity context, Typeface font){
            View view = null;

            if(sIconID == -1) {
                view = View.inflate(context, R.layout.subscription_layout_text_icon, null);
            }else{
                view = View.inflate(context, R.layout.subscription_layout_icon_image, null);
            }

            view = fillOutView(view, font);

            return view;
        }

        public View fillOutView(View view, Typeface font){

            if(sIconID == -1) {
                TextView icon = view.findViewById(R.id.icon);
                icon.setText(sIconText);
                icon.setTypeface(font);
            }else{
                ImageView imageView = view.findViewById(R.id.icon);
                imageView.setImageResource(sIconID);

                PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.WHITE,
                        PorterDuff.Mode.SRC_ATOP);

                imageView.setColorFilter(porterDuffColorFilter);
            }

            view.setBackgroundColor(sColor);

            TextView serviceName = view.findViewById(R.id.serviceName);
            serviceName.setText(sName);
            serviceName.setTypeface(font);

            TextView description = view.findViewById(R.id.description);
            description.setText(sDescription);
            description.setTypeface(font);

            TextView amount = view.findViewById(R.id.amount);
            amount.setText(mAmountString);
            amount.setTypeface(font);

            TextView nextPayment = view.findViewById(R.id.nextPaymentDate);
            if(!(sFirstBillingDate < 0)){
                nextPayment.setText(getNextPaymentString());
                nextPayment.setTypeface(font);
            }else{
                nextPayment.setText("");
            }

            if((sReminderID == reminders.NEVER.value))
            { // If the reminder is set to never, make the alarm icon go away.
                view.findViewById(R.id.alarmIcon).setVisibility(View.INVISIBLE);
            }else{
                view.findViewById(R.id.alarmIcon).setVisibility(View.VISIBLE);
            }

            return view;
        }

        public boolean equals(Subscriptions subscription){
            boolean equal;
            equal  = (this.sIconID == subscription.getIconID());
            equal &= this.sIconText.equals(subscription.getIconText());
            equal &= (this.sColor == subscription.getColor());
            equal &= this.sName.equals(subscription.getName());
            equal &= this.sDescription.equals(subscription.getDescription());
            equal &= (this.sBillingCycleID  == subscription.getBillingCycleID());
            equal &= (this.sFirstBillingDate == subscription.getFirstBillingDate());
            equal &= (getNextBillingDate()  == subscription.getNextBillingDate());
            equal &= (this.sReminderID  == subscription.getReminderID());
            equal &= (this.sSubscriptionType == subscription.getSubscriptionType());
            return equal;
        }

        public String getNextPaymentString() {
            String nextPayment = "";

            long startDate = today();
            long endDate   = sFirstBillingDate;

            if(startDate > sFirstBillingDate) {
                if(today() > sNextBillingDate) {
                    long newTime = generateNextBillingDate();
                    setNextBillingDate(newTime);
                }
                endDate = sNextBillingDate;
            }

            nextPayment = getDateDistance(startDate, endDate);

            return nextPayment;
        }

        public long generateNextBillingDate(){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(getNextBillingDate());

            billingCycle billCycle = billingCycle.values()[sBillingCycleID];

            if (billCycle == billingCycle.WEEKLY) {
                c.add(Calendar.WEEK_OF_YEAR, 1);
            } else if (billCycle == billingCycle.MONTHLY) {
                c.add(Calendar.MONTH, 1);
            } else if (billCycle == billingCycle.QUARTERLY) {
                c.add(Calendar.MONTH, 3);
            } else if (billCycle == billingCycle.YEARLY) {
                c.add(Calendar.YEAR, 1);
            }

            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            return c.getTimeInMillis();
        }

        private String getDateDistance(long startDate, long endDate){
            String dateString = "";

            long deltaDate = endDate - startDate;
            int days = (int) (deltaDate / (1000*60*60*24));

            if(startDate > endDate){
                setNextBillingDate(endDate);
                return getNextPaymentString();
            }

            switch (days){
                case(0):
                    dateString = "TODAY";
                    break;
                case(1):
                    dateString = "TOMORROW";
                    break;
                default:
                    int months = days / 30;
                    if(months == 1){
                        dateString = String.format(Locale.US, "IN %d MONTH", months);
                    }
                    else if(months > 1){
                        dateString = String.format(Locale.US, "IN %d MONTHS", months);
                    }
                    else{
                        dateString = String.format(Locale.US, "IN %d DAYS", days);
                    }
                    break;
            }

            return dateString;
        }

        static public long today(){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis(); // Today's date in milliseconds
        }


        public String getIconText() {
            return sIconText;
        }

        public int getIconID() {
            return sIconID;
        }

        public int getColor(){
            return sColor;
        }

        public String getName()
        {
            return sName;
        }

        public String getDescription()
        {
            return sDescription;
        }

        public BigDecimal getAmount() {
            return sAmount;
        }

        public String getAmountString() {
            return this.mAmountString;
        }

        public String getBillingCycleString(Context context){
            String[] billingCycles = context.getResources().getStringArray(R.array.billing_cycles);
            return billingCycles[getBillingCycleID()];
        }

        public int getBillingCycleID() {
            return sBillingCycleID;
        }

        public String getFirstBillingDateString(Context context){
            return convertMillisToString(context, getFirstBillingDate());
        }

        public static String convertMillisToString(Context context, long millis){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis);

            int month = c.get(Calendar.MONTH);
            int day   = c.get(Calendar.DAY_OF_MONTH);
            int year  = c.get(Calendar.YEAR);

            String monthString = context.getResources().getStringArray(R.array.months)[month];
            return String.format(Locale.US, "%s %d, %d", monthString, day, year);
        }

        public long getFirstBillingDate() {
            return sFirstBillingDate;
        }

        public long getNextBillingDate(){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(sNextBillingDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis(); // Today's date in milliseconds
        }

        public String getReminderString(Context context){
            String[] reminders = context.getResources().getStringArray(R.array.reminders);
            return reminders[getReminderID()];
        }

        public int getReminderID()
        {

            return sReminderID;
        }

        public int getSubscriptionType(){

            return sSubscriptionType;
        }


        public void setIconText(String iconText) {

            this.sIconText = iconText;
        }

        public void setIconID(int iconID) {

            this.sIconID = iconID;
        }

        public void setColor(int color) {

            this.sColor = color;
        }

        public void setName(String name) {

            this.sName = name;
        }

        public void setDescription(String description) {

            this.sDescription = description;
        }

        public void setAmount(BigDecimal amount) {
            this.sAmount = amount;

            this.mAmountString = "";
            if(!(this.sAmount.floatValue() < 0)){
                DecimalFormat decimalFormat = new DecimalFormat("$#,###.##");
                this.mAmountString = decimalFormat.format(amount);
            }
        }

        public void setBillingCycleID(int billingCycleID) {
            this.sBillingCycleID = billingCycleID;
            this.sNextBillingDate = sFirstBillingDate;
        }

        public void setFirstBillingDate(long billingDate) {
            this.sFirstBillingDate = billingDate;
            this.sNextBillingDate  = sFirstBillingDate;
        }

        public void setNextBillingDate(long billingDate){

            this.sNextBillingDate = billingDate;
        }

        public void setReminderID(int reminderID) {

            this.sReminderID = reminderID;
        }
    }

