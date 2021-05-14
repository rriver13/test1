package com.example.Subcept;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class ChangeColor extends DialogFragment { private final ArrayList<OnFinishedListener> onFinishedListeners = new ArrayList<OnFinishedListener>();

    public void setOnFinishedListener(OnFinishedListener listener){
        onFinishedListeners.add(listener);
    }

    public interface OnFinishedListener{
        void onFinishedWithResult(int color);
    }

    private int color;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        color = args.getInt("color");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Select a color");

        builder.setCancelable(true);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // Prepare grid view
        GridView gridView = new GridView(getContext());


        int[] intColors = getResources().getIntArray(R.array.colors);
        Integer[] colors = new Integer[intColors.length];
        for(int i = 0; i < intColors.length; ++i){
            colors[i] = intColors[i];
        }
        final ArrayAdapter<Integer> arrayAdapter = new ColorArrayAdapter(getContext(), colors);
        gridView.setAdapter(arrayAdapter);

        gridView.setNumColumns(4);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // do something here
                Integer color = arrayAdapter.getItem(position);
                for(OnFinishedListener listener: onFinishedListeners){
                    listener.onFinishedWithResult(color);
                }
                dismiss();
            }
        });

        builder.setView(gridView);

        return builder.create();
    }
}

class ColorArrayAdapter extends ArrayAdapter<Integer> {
    final Context context;

    public ColorArrayAdapter(Context context, Integer[] items){
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Integer color = getItem(position);
        ImageView view = new ImageView(context);
        view.setImageResource(R.drawable.ic_lens);
        view.setColorFilter(color);
        view.setScaleX(1.8f);
        view.setScaleY(1.8f);
        view.setPadding(20, 30, 20, 30);

        return view;
    }
}
