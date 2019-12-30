package com.example.travel_helper.Screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.travel_helper.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.StringTokenizer;

import butterknife.BindView;
import objects.ZoneName;

public class CurrencyListViewActivity  extends AppCompatActivity implements TextWatcher {

    @BindView(R.id.listView)
    RecyclerView mListview;
    @BindView(R.id.currencySearch)
    EditText mCurrencySearch;
    CurrencyConverterAdapter mAdaptorListView;
    String temp = null;

    public static ArrayList<ZoneName> currences_names;
    String s_ids_names;
    private Context mContext;

    StringTokenizer stok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list_view);
        setTitle(R.string.text_currency);

        currences_names = new ArrayList<>();
        mListview = findViewById(R.id.listView);
        mCurrencySearch = findViewById(R.id.currencySearch);

        mContext = this;
        addCurrencies();

        mCurrencySearch.addTextChangedListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void addCurrencies() {
        try {
            InputStream is = mContext.getAssets().open("currency.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            s_ids_names = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        s_ids_names = s_ids_names.replace("{", "");
        s_ids_names = s_ids_names.replace("}", "");
        s_ids_names = s_ids_names.replace("\"", "");
        stok = new StringTokenizer(s_ids_names, ",");

        while (stok.hasMoreElements()) {
            temp = stok.nextElement().toString();

            if (temp.contains("currencySymbol")) {
                temp = stok.nextElement().toString();
            }

            String[] split = temp.split(":");
            temp = stok.nextElement().toString();
            if (temp.contains("currencySymbol")) {
                temp = stok.nextElement().toString();
            }
            String[] split2 = temp.split(":");
            temp = null;
            currences_names.add(new ZoneName(split[2], split2[1]));
        }

        Collections.sort(currences_names, (n1, n2) -> n1.shortName.compareTo(n2.shortName));

        mAdaptorListView = new CurrencyConverterAdapter(CurrencyListViewActivity.this, currences_names);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext());
        mListview.setLayoutManager(mLayoutManager);
        mListview.setAdapter(mAdaptorListView);
    }

    @Override
    public void beforeTextChanged(CharSequence searchItem, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence searchItem, int start, int before, int count) {
        CurrencyListViewActivity.this.mAdaptorListView.getFilter().filter(searchItem);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
