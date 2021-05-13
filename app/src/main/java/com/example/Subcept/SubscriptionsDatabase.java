package com.example.Subcept;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.Subcept.AppsModel;

public class SubscriptionsDatabase extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestorelist;
    private FirestoreRecyclerAdapter adapter;

    private static final String SUBSCRIPTIONS_TABLE_NAME = "subscriptions";
    private static final String COLUMN_ID                = "id";
    private static final String COLUMN_COLOR             = "color";
    private static final String COLUMN_ICON_TEXT         = "icon_text";
    private static final String COLUMN_ICON_IMAGE        = "icon_image";
    private static final String COLUMN_NAME              = "name";
    private static final String COLUMN_DESCRIPTION       = "description";
    private static final String COLUMN_AMOUNT            = "amount";
    private static final String COLUMN_BILLING_CYCLE     = "billing_cycle";
    private static final String COLUMN_BILLING_DATE      = "billing_date";
    private static final String COLUMN_NEXT_BILLING_DATE = "next_billing_date";
    private static final String COLUMN_REMINDER          = "reminder";
    private static final String COLUMN_TYPE              = "type";

    public static final int CUSTOM_TYPE = 0, TEMPLATE_TYPE = 1;

    private static final String SUBSCRIPTIONS_TABLE_CREATE = "CREATE TABLE " +
            SUBSCRIPTIONS_TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COLOR              + " INTEGER, " +
            COLUMN_ICON_TEXT          + " TEXT, "    +
            COLUMN_ICON_IMAGE         + " INTEGER, " +
            COLUMN_NAME               + " TEXT, "    +
            COLUMN_DESCRIPTION        + " TEXT, "    +
            COLUMN_AMOUNT             + " TEXT, " +
            COLUMN_BILLING_CYCLE      + " INTEGER, " +
            COLUMN_BILLING_DATE       + " INTEGER, " +
            COLUMN_NEXT_BILLING_DATE  + " INTEGER, " +
            COLUMN_REMINDER           + " INTEGER, " +
            COLUMN_TYPE               + " INTEGER " +
            ");";


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepg);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestorelist = findViewById(R.id.firestore_list);

        Query query = firebaseFirestore.collection("apps");
        FirestoreRecyclerOptions<AppsModel> options = new FirestoreRecyclerOptions.Builder<AppsModel>().setQuery(query,AppsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<AppsModel, AppsViewHolder>(options) {
            @NonNull
            @Override
            public AppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
                return new AppsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AppsViewHolder holder, int position, @NonNull AppsModel model) {
                holder.name.setText(model.getName());
                holder.charge.setText(model.getCharge() + "");
                holder.category.setText(model.getCategory());
            }
        };
        mFirestorelist.setHasFixedSize(true);
        mFirestorelist.setLayoutManager(new LinearLayoutManager(this));
        mFirestorelist.setAdapter(adapter);
    }

    private class AppsViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private TextView category;
        private TextView charge;

        public AppsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            charge = itemView.findViewById(R.id.charge);
        }
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.startListening();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
}
}
