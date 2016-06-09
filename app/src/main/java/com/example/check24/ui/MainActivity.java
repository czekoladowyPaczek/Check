package com.example.check24.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.check24.CheckApplication;
import com.example.check24.R;
import com.example.check24.calculation.Calculator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by marcingawel on 09.06.2016.
 */

public class MainActivity extends AppCompatActivity implements NumberAdapter.OnItemClicked {
    public static final int CODE_CHOICE = 1;

    private List<Double> numbers;
    private NumberAdapter adapter;

    private View root;
    private EditText numberView;
    private Button addView;

    private Dialog dialog;

    @Inject
    Calculator calculator;

    private Subscription calculationSubsription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CheckApplication) getApplication()).getComponent().inject(this);

        root = findViewById(R.id.root);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numbers = new ArrayList<>();
        adapter = new NumberAdapter(numbers, this, this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        addView = (Button) findViewById(R.id.item_add);
        numberView = (EditText) findViewById(R.id.item_entry);
        addView.setOnClickListener(v -> {
            if (addItem(numberView.getText().toString(), -1)) {
                numberView.setText("");
            }
        });

        FloatingActionButton computationButton = (FloatingActionButton) findViewById(R.id.shopComputation);
        computationButton.setOnClickListener((v) -> startActivityForResult(new Intent(this, ChoiceActivity.class), CODE_CHOICE));
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (calculationSubsription != null && !calculationSubsription.isUnsubscribed()) {
            calculationSubsription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_CHOICE && resultCode == Activity.RESULT_OK) {
            switch (data.getIntExtra(ChoiceActivity.EXTRA_CHOICE, -1)) {
                case ChoiceActivity.CHOICE_SUM:
                    calculationSubsription = calculator.getSum(numbers)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(value -> {
                                Snackbar.make(root, String.valueOf(value.doubleValue()), Snackbar.LENGTH_LONG).show();
                            }, error -> {

                            });
                    break;
            }
        }
    }

    private boolean addItem(String text, int position) {
        if (TextUtils.isEmpty(text)) {
            showError();
            return false;
        }

        try {
            double d = Double.parseDouble(text);
            if (position > -1) {
                numbers.remove(position);
                numbers.add(position, d);
                adapter.notifyItemChanged(position);
            } else {
                numbers.add(d);
                adapter.notifyItemInserted(numbers.size());
            }
            return true;
        } catch (NumberFormatException e) {
            showError();
        }
        return false;
    }

    private void showError() {
        Snackbar.make(root, R.string.wrong_format, Snackbar.LENGTH_LONG).show();
    }

    private void clearList() {
        int size = numbers.size();
        numbers.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                clearList();
                return true;
            default:
                return false;

        }
    }

    @Override
    public void onItemRemove(int position) {
        numbers.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemChange(final int position) {
        final EditText edit = new EditText(this);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edit.setText(String.valueOf(numbers.get(position)));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        edit.setLayoutParams(lp);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog1, which) -> addItem(edit.getText().toString(), position))
                .create();
        alertDialog.setView(edit);
        alertDialog.show();
        dialog = alertDialog;
    }
}
