package com.liu.expensetracker.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.expensetracker.BalanceSelector;
import com.liu.expensetracker.R;
import com.liu.expensetracker.utilities.Balance;
import com.liu.expensetracker.utilities.BalanceManager;
import com.liu.expensetracker.utilities.Utility;

import java.text.DecimalFormat;
import java.util.List;

public class BalanceAdapter extends ArrayAdapter<Balance> {

    private LayoutInflater inflater;

    public BalanceAdapter(Context c, List<Balance> items) {
        super(c, 0, items);
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.balance_item_view, null);
        TextView name, amount;

        name = (TextView) view.findViewById(R.id.bitem_name);
        amount = (TextView) view.findViewById(R.id.bitem_amount);

        Balance bal = getItem(position);

        DecimalFormat format = new DecimalFormat();
        format.setMaximumIntegerDigits(5);
        format.setMaximumFractionDigits(2);

        name.setText(bal.getName());
        amount.setText(format.format(bal.getAmount()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nv = (TextView) v.findViewById(R.id.bitem_name);
                String name = nv.getText().toString();

                BalanceManager.setCurrent(BalanceManager.getBalance(name));

                Intent intent = new Intent(v.getContext(), Utility.clazz);
                v.getContext().startActivity(intent);

                Toast toast = Toast.makeText(v.getContext(), "Switched to balance: " + name + "!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                Context context = v.getContext();

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                TextView vName = (TextView) v.findViewById(R.id.bitem_name);

                alert.setTitle("Delete Balance");
                alert.setMessage("Are you sure you want to delete balance: " + vName.getText() + "?");

                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TextView vName = (TextView) v.findViewById(R.id.bitem_name);
                        String name = vName.getText().toString();

                        boolean success = BalanceManager.delete(name);

                        String message = "Unable to delete balance: " + name + ".";
                        if (success) {
                            message = "Successfully deleted balance: " + name + "!";
                        }
                        Toast toast = Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(v.getContext(), BalanceSelector.class);
                        v.getContext().startActivity(intent);
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
        return view;
    }


}
