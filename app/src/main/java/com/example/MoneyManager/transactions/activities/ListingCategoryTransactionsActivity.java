package com.example.MoneyManager.transactions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.MoneyManager.R;
import com.example.MoneyManager.budget_categories.BudgetCategoryManager;
import com.example.MoneyManager.transactions.Transaction;
import com.example.MoneyManager.transactions.TransactionManager;
import com.example.MoneyManager.transactions.ui.TransactionListAdapter;

import java.util.ArrayList;

public class ListingCategoryTransactionsActivity extends AppCompatActivity
{
    TextView selectedCategoryName;
    ListView transactionListView;

    String categoryName;
    ArrayList<Transaction> transactions;
    TransactionListAdapter transactionListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_transaction);
        try {
            selectedCategoryName = findViewById(R.id.selected_category_name);
            transactionListView = findViewById(R.id.transaction_lv);

            Bundle bundle = getIntent().getExtras();
            categoryName = bundle.getString("category", "none");
            selectedCategoryName.setText(categoryName);

            int id= BudgetCategoryManager.getCategoryIDFromName(categoryName);
            transactions = TransactionManager.getTransactionsByCategoryID(id);
            transactionListAdapter = new TransactionListAdapter(getApplicationContext(), transactions);
            transactionListView.setAdapter(transactionListAdapter);
            transactionListAdapter.notifyDataSetChanged();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(transactionListAdapter!=null)
            transactionListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(transactionListAdapter!=null)
            transactionListAdapter.notifyDataSetChanged();
    }
}