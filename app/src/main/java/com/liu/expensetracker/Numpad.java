package com.liu.expensetracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.liu.expensetracker.utilities.Balance;
import com.liu.expensetracker.utilities.Utility;
import com.liu.expensetracker.utilities.BalanceManager;

import java.text.DecimalFormat;

public class Numpad extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView balance, temp;
    private Balance current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numpad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Utility.init(this);

        current = BalanceManager.getCurrent();

        balance = (TextView) findViewById(R.id.balance);
        temp = (TextView) findViewById(R.id.temp);
        balance.setText(Utility.formatDouble(current.getAmount()));

        Button clear = (Button) findViewById(R.id.clear);
        if (clear != null) {
            final Context context = this;
            clear.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Reset Balance");
                    alert.setMessage("Are you sure you want to reset balance: " + current.getName() + "?");

                    alert.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            current.setAmount(0);
                            balance.setText(Utility.formatDouble(current.getAmount()));
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alert.show();

                    return false;
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_balance_selector) {

            Intent intent = new Intent(this, BalanceSelector.class);
            Utility.clazz = this.getClass();
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.numpad) {

        } else if (id == R.id.custom) {

            Intent intent = new Intent(this, CustomButton.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnClicked(View v) {

        if (v instanceof Button) {

            Button b = (Button) v;
            String text = b.getText().toString();
            temp.setTextColor(Color.WHITE);
            if (text.matches("[0-9.]")) {
                append(text);
            } else if (text.equals(getString(R.string.clear))) {
                temp.setText("");
            } else if (text.equals(getString(R.string.arr))) {
                String txt = temp.getText().toString();
                if (txt.length() > 0) {
                    temp.setText(txt.substring(0, temp.getText().length() - 1));
                }
            } else if (text.equals(getString(R.string.add))) {
                try {
                    String s = temp.getText().toString();
                    if (s.equals("")) {
                        return;
                    }
                    double tmp = Double.parseDouble(s);
                    add(tmp);
                } catch (NumberFormatException e) {
                    temp.setTextColor(Color.RED);
                    e.printStackTrace();
                }
            } else if (text.equals(getString(R.string.subtract))) {
                try {
                    double tmp = Double.parseDouble(temp.getText().toString());
                    add(-tmp);
                } catch (NumberFormatException e) {
                    temp.setTextColor(Color.RED);
                    e.printStackTrace();
                }
            }

        }

    }

    private void append(String s) {
        temp.setText(temp.getText() + s);
    }

    private void add(double d) {
        current.addAmount(d);
        balance.setText(Utility.formatDouble(current.getAmount()));
        temp.setText("");
        BalanceManager.save(current);
    }


}
