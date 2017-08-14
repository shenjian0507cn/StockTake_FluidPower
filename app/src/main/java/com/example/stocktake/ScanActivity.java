package com.example.stocktake;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ScanActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private EditText BarCode;

    private TextView BarCodeTxt;
    private TextView ProductCode;
    private TextView Description;
    private TextView QuantityInStock;
    private TextView QuantityInCount;

    private String Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createStockTakeBasic();

        BarCode = (EditText)findViewById(R.id.barcodeEdt);
        BarCode.addTextChangedListener(barcodeWatcher);

        BarCodeTxt = (TextView)findViewById(R.id.barcodeTxt);
        ProductCode = (TextView)findViewById(R.id.productTxt);
        Description = (TextView)findViewById(R.id.discriptionTxt);
        QuantityInStock = (TextView)findViewById(R.id.quantityTxt);
        QuantityInCount = (TextView)findViewById(R.id.countTxt);

        TextView locationTxt = (TextView)findViewById(R.id.LocationTxt);

        Location = getIntent().getStringExtra("LocationCode");
        locationTxt.setText(Location);
    }

    private final TextWatcher barcodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) return;

            if (((byte)s.charAt(s.length() - 1) == 13) || ((byte)s.charAt(s.length() - 1) == 10)) {
                String BarcodeStr = s.toString().substring(0,s.length()-1);
                scan(BarcodeStr);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) return;
        }
    };

    public void scan_onClick_Event(View view) {
        scan(BarCode.getText().toString());
    }

    public void scan(String BarcodeStr) {
        //String BarcodeStr = BarCode.getText().toString();
        /*
        BarCodeTxt.setText(BarcodeStr);

        dbHelper.AddOnStockTakeBasic(Location, BarcodeStr);

        List<String> productDetails = dbHelper.GetProductDetails(BarcodeStr);

        ProductCode.setText(productDetails.get(0));
        Description.setText(productDetails.get(1));

        int quantity = dbHelper.GetQuantityInStock(Location, productDetails.get(0));
        QuantityInStock.setText(String.valueOf(quantity));

        int counted = dbHelper.GetQuantityInCount(Location, BarcodeStr);
        QuantityInCount.setText(String.valueOf(counted));

        BarCode.setText("");
        */
    }

    public void search_onClick_Event(View view) {
        /*
        String BarcodeStr = BarCode.getText().toString();

        List<String> productDetails = dbHelper.GetProductDetails(BarcodeStr);

        ProductCode.setText(productDetails.get(0));
        Description.setText(productDetails.get(1));

        int quantity = dbHelper.GetQuantityInStock(Location, productDetails.get(0));
        QuantityInStock.setText(String.valueOf(quantity));

        int counted = dbHelper.GetQuantityInCount(Location, BarcodeStr);
        QuantityInCount.setText(String.valueOf(counted));
        */
    }
}
