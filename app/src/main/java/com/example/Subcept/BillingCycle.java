package com.example.Subcept;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class BillingCycle extends DialogFragment {
    private final ArrayList<OnFinishedListener> onFinishedListeners = new ArrayList<OnFinishedListener>();

    public void setOnFinishedListener(OnFinishedListener listener){
        onFinishedListeners.add(listener);
    }

    public interface OnFinishedListener{
        void onFinishedWithResult(int index, String name);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Billing Cycle");

        builder.setCancelable(true);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.select_dialog_item);

        arrayAdapter.addAll(getResources().getStringArray(R.array.billing_cycles));

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

                for(OnFinishedListener listener: onFinishedListeners){
                    listener.onFinishedWithResult(which, strName);
                }
            }
        });

        return builder.create();
    }
}
