package com.liu.expensetracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.liu.expensetracker.utilities.Balance;
import com.liu.expensetracker.utilities.BalanceManager;
import com.liu.expensetracker.utilities.Utility;
import com.liu.expensetracker.views.BalanceAdapter;

import java.util.List;

public class BalanceSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Select Balance");
        bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.vector_banner));

        List<Balance> items = BalanceManager.getBalanceList();
        BalanceAdapter adapter = new BalanceAdapter(this, items);

        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newBalance);
        if (fab != null) {
            final Context context = this;
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("New Balance");

                    final View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.balance_creator_view, null);
                    alert.setView(v);

                    alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            TextView iName = (TextView) v.findViewById(R.id.inputName);
                            TextView iAmount = (TextView) v.findViewById(R.id.inputAmount);

                            String name = iName.getText().toString();
                            double amount = Double.parseDouble(iAmount.getText().toString());

                            Balance balance = new Balance(name, amount);
                            BalanceManager.setCurrent(balance);

                            Intent intent = new Intent(context, context.getClass());
                            startActivity(intent);

                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alert.show();

                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(this, Utility.clazz);
            startActivity(intent);
        }

        return true;
    }

}
