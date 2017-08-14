package com.example.stocktake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private EditText productcodeEdt;
    private EditText descriptionEdt;
    private DBHelper dbHelper;

    private ListView productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productcodeEdt = (EditText)findViewById(R.id.productcodeEdt);
        productcodeEdt.addTextChangedListener(productcodeWatcher);

        descriptionEdt = (EditText)findViewById(R.id.descriptionEdt);
        descriptionEdt.addTextChangedListener(descriptionWatcher);

        productList = (ListView)findViewById(R.id.productLst);

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = (Map<String, String>)productList.getItemAtPosition(position);
                String selectedProduct = map.get("Code");

                Intent intent = new Intent();
                intent.putExtra("selectedProduct", selectedProduct);
                setResult(0, intent);

                finish();
            }
        });

        dbHelper = new DBHelper(getApplicationContext());
    }

    private final TextWatcher productcodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) return;

            refreshListViewByProductCode(productcodeEdt.getText().toString());
        }
    };

    private final TextWatcher descriptionWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) return;

            refreshListViewByDescription(descriptionEdt.getText().toString());
        }
    };

    private void refreshListViewByProductCode(String ProductCode) {
        List<Map<String, String>> ProductList = dbHelper.ProdductDescriptionQueryByProduct(ProductCode);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, ProductList, android.R.layout.simple_list_item_2, new String[]{"Code","Description"}, new int[]{android.R.id.text1, android.R.id.text2});
        productList.setAdapter(simpleAdapter);
    }

    private void refreshListViewByDescription(String Description) {
        List<Map<String, String>> ProductList = dbHelper.ProdductDescriptionQueryByDescription(Description);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, ProductList, android.R.layout.simple_list_item_2, new String[]{"Code","Description"}, new int[]{android.R.id.text1, android.R.id.text2});
        productList.setAdapter(simpleAdapter);
    }
}
