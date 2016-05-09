package com.liu.expensetracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liu.expensetracker.utilities.Balance;
import com.liu.expensetracker.utilities.BalanceManager;
import com.liu.expensetracker.utilities.ButtonPreferenceManager;
import com.liu.expensetracker.utilities.Utility;

import java.text.DecimalFormat;
import java.util.Map;

public class CustomButton extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView balance;
    Balance current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        current = BalanceManager.getCurrent();
        balance = (TextView) findViewById(R.id.balance);
        balance.setText(Utility.formatDouble(current.getAmount()));

        Map<String, String> buttonMap = ButtonPreferenceManager.read();

        int[] ids = {R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8, R.id.b9};
        for (int i = 0; i < ids.length; i++) {
            final Button button = (Button) findViewById(ids[i]);
            if (button != null) {
                button.setText(buttonMap.get("b" + i));
                final Context context = this;
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Custom Button");
                        alert.setMessage("Change the value of this button");

                        final EditText input = new EditText(context);
                        alert.setView(input);
                        input.setText(button.getText());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

                        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                double val = 0;
                                try {
                                    val = Double.parseDouble(input.getText().toString());
                                    DecimalFormat format = new DecimalFormat();
                                    format.setMaximumFractionDigits(2);
                                    String formatted = format.format(val);
                                    if (formatted.length() > 8) {
                                        format = new DecimalFormat("0.00E0");
                                        formatted = format.format(val);
                                    }
                                    button.setText(formatted);

                                    String idName = getResources().getResourceName(button.getId());
                                    ButtonPreferenceManager.save(Integer.parseInt(String.valueOf(idName.charAt(idName.length() - 1))) - 1, val);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

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

            Intent intent = new Intent(this, Numpad.class);
            startActivity(intent);

        } else if (id == R.id.custom) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnClicked(View v) {

        if (v instanceof Button) {

            Button button = (Button) v;
            double amount = Double.parseDouble(button.getText().toString());
            add(amount);

        }

    }

    private void add(double d) {
        current.addAmount(d);
        balance.setText(Utility.formatDouble(current.getAmount()));
        BalanceManager.save(current);
    }

}
