package com.example.stocktake;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.generateViewId;

public class ScanUOM extends AppCompatActivity {

    private DBHelper dbHelper;

    private Spinner binSpin;
    private EditText BarCode;
    private EditText countEdt;
    private CheckBox isNegative;

    private TextView BarCodeTxt, Pre_BarCodeTxt;
    private TextView ProductCode, Pre_ProductCode;
    private TextView Description, Pre_Description;
    private TextView QuantityInStock, Pre_QuantityInStock;
    private TextView QuantityInCount, Pre_QuantityInCount;

    private String Location;
    private String BinLocation;

    private LinearLayout detailLayout;

    private List<String> TagList = new ArrayList<String>();

    private Double currentCounted = 0.00;

    private static MediaPlayer mp = new MediaPlayer();

    private EditText productcodeEdt;

    private String previousBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_uom);

        Location = getIntent().getStringExtra("LocationCode");

        TextView locationTxt = (TextView)findViewById(R.id.LocationTxt);
        locationTxt.setText(Location);

        dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createStockTakeBasic();

        //BinLocation DropDown List
        final List<String> BinLocationList = dbHelper.GetBinLocationList(Location);
        if (BinLocationList.size() == 0) {
            BinLocationList.add("");
        }
        final String binArr[] = BinLocationList.toArray(new String[BinLocationList.size()]);
        BinLocation = binArr[0];

        binSpin = (Spinner)findViewById(R.id.binSpin);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, binArr);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binSpin.setAdapter(arrayAdapter);

        binSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                BinLocation = BinLocationList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //EditText for scanning barcode
        BarCode = (EditText)findViewById(R.id.barcodeEdt);
        BarCode.addTextChangedListener(barcodeWatcher);

        //Create mediaPlayer for playing alarm video
        try {
            AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.quantityalarm);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.setAudioStreamType(AudioManager.STREAM_RING);
            mp.setLooping(true);
            afd.close();
            mp.prepare();
        }
        catch (Exception e) {

        }

        countEdt = (EditText)findViewById(R.id.countEdt);
        countEdt.setText("0");
        countEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    countEdt.setText(String.valueOf(currentCounted));

                    try {
                        if (mp.isPlaying()) mp.pause();
                        mp.seekTo(0);
                        mp.setVolume(1000,1000);
                        mp.start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanUOM.this);
                    builder.setTitle("Alarm");
                    builder.setMessage("Quantity Error!");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mp.pause();
                            dialog.cancel();
                        }
                    });
                    builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                return  true;
                            }
                            else {
                                return false;
                            }
                        }
                    });
                    builder.show();

                    BarCode.requestFocus();
                }

                return false;
            }
        });
        countEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currentCounted = Double.parseDouble(countEdt.getText().toString());
                }
            }
        });

        isNegative = (CheckBox)findViewById(R.id.negative);

        BarCodeTxt = (TextView)findViewById(R.id.barcodeTxt);
        ProductCode = (TextView)findViewById(R.id.productTxt);
        Description = (TextView)findViewById(R.id.discriptionTxt);
        QuantityInStock = (TextView)findViewById(R.id.quantityTxt);
        QuantityInCount = (TextView)findViewById(R.id.countTxt);

        Pre_BarCodeTxt = (TextView)findViewById(R.id.pre_barcodeTxt);
        Pre_ProductCode = (TextView)findViewById(R.id.pre_productTxt);
        Pre_Description = (TextView)findViewById(R.id.pre_discriptionTxt);
        Pre_QuantityInStock = (TextView)findViewById(R.id.pre_quantityTxt);
        Pre_QuantityInCount = (TextView)findViewById(R.id.pre_countTxt);

        productcodeEdt = (EditText)findViewById(R.id.productcodeEdt);
        productcodeEdt.setFocusable(false);
        productcodeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanUOM.this, ProductActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void clearLinearLayout() {
        detailLayout = (LinearLayout)findViewById(R.id.detail_linearlayout);

        for (int i=0; i < TagList.size(); i++) {
            LinearLayout l = (LinearLayout)detailLayout.findViewWithTag(TagList.get(i));
            if (l != null) {
                l.removeAllViewsInLayout();
                detailLayout.removeViewInLayout(l);
            }
        }

        TagList.clear();
    }

    private void AddUOMLine(String UOMCode, double Counted) {
        detailLayout = (LinearLayout)findViewById(R.id.detail_linearlayout);

        LinearLayout l = (LinearLayout)detailLayout.findViewWithTag(UOMCode);
        if (l != null) {
            TextView t = (TextView)l.findViewWithTag(UOMCode + "TV");
            t.setText(String.valueOf(Counted));
            return;
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        int linearLayoutID = generateViewId();
        linearLayout.setId(linearLayoutID);
        linearLayout.setLayoutParams(lp);
        linearLayout.setMinimumHeight(48);
        linearLayout.setTag(UOMCode);

        TextView[] textViews = new TextView[2];

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,2);
        textViews[0] = new TextView(this);
        textViews[0].setLayoutParams(lp1);
        textViews[0].setGravity(Gravity.CENTER_VERTICAL);
        textViews[0].setText(UOMCode);
        textViews[0].setWidth(0);
        textViews[0].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[0].setMinHeight(45);
        textViews[0].setTextSize(16);
        linearLayout.addView(textViews[0]);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,5);
        textViews[1] = new TextView(this);
        textViews[1].setLayoutParams(lp2);
        textViews[1].setGravity(Gravity.CENTER_VERTICAL);
        textViews[1].setText(String.valueOf(Counted));
        textViews[1].setWidth(0);
        textViews[1].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[1].setMinHeight(45);
        textViews[1].setTextSize(16);
        textViews[1].setTag(UOMCode + "TV");
        linearLayout.addView(textViews[1]);

        detailLayout.addView(linearLayout);
    }

    public void search(String ProductcodeStr) {
        clearLinearLayout();

        ICProduct productDetails = dbHelper.GetProductDetails(Location, ProductcodeStr);

        ProductCode.setText(productDetails.get_ProductCode());
        Description.setText(productDetails.get_Description());

        double quantity = dbHelper.GetQuantityInStock(Location, productDetails.get_ProductCode());
        QuantityInStock.setText(String.valueOf(quantity));

        double counted = 0.0;
        counted = productDetails.get_Counted();

        QuantityInCount.setText(String.valueOf(counted));
    }

    public void scan(String BarcodeStr) {
        String productcodeStr = dbHelper.getProductcodeByBarcode(BarcodeStr);

        productcodeEdt.setText("");
        BarCode.setText("");

        if ((Common.get_AuotAdding(getApplicationContext())) && (productcodeStr.equals(Pre_ProductCode.getText().toString()))){
            Double d = Double.parseDouble(countEdt.getText().toString());
            d = d + 1;
            countEdt.setText(Common.DoubleToString(d, Common.get_Decimaldigit(getApplicationContext())));
        }
        else {
            if ((Common.get_AutoSaving(getApplicationContext())) && (!productcodeStr.equals(Pre_ProductCode.getText().toString()))){
                if (!Pre_ProductCode.getText().toString().equals("")) {
                    saveScan();
                }
            }

            countEdt.setText(Common.DoubleToString(1, Common.get_Decimaldigit(getApplicationContext())));

            Pre_BarCodeTxt.setText(BarcodeStr);
            Pre_ProductCode.setText(productcodeStr);

            String Description = dbHelper.GetProductDescription(productcodeStr);
            Pre_Description.setText(Description);

            Double QuantityInStock = dbHelper.GetQuantityInStock(Location, productcodeStr);
            Pre_QuantityInStock.setText(String.valueOf(QuantityInStock));

            Double QuantityCounted = dbHelper.GetQuantityInCount(Location, productcodeStr);
            Pre_QuantityInCount.setText(String.valueOf(QuantityCounted));
        }
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

    public void minus_onClick_Event(View view) {
        double i = Double.parseDouble(countEdt.getText().toString());
        if (isNegative.isChecked()) {
            i++;
        }
        else {
            if (i == 0) {
                isNegative.setChecked(true);
                i = 1.00;
            }
            else {
                i--;
            }
        }
        countEdt.setText(Common.DoubleToString(i, Common.get_Decimaldigit(getApplicationContext())));
        currentCounted = i;
    }

    public void plus_onClick_Event(View view) {
        Double i = Double.parseDouble(countEdt.getText().toString());
        if (isNegative.isChecked()) {
            if (i == 0) {
                isNegative.setChecked(false);
                i++;
            }
            else {
                i--;
            }
        }
        else {
            i++;
        }
        countEdt.setText(Common.DoubleToString(i, Common.get_Decimaldigit(getApplicationContext())));
        currentCounted = i;
    }

    public void Multi_onClick_Event(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.entrynumber, null);
        final TextView dialog_ProductCode = (TextView)v.findViewById(R.id.dialog_productcodeTxt);
        final TextView dialog_BarCode = (TextView)v.findViewById(R.id.dialog_barcodeTxt);
        final EditText dialog_input = (EditText)v.findViewById(R.id.dialog_quantityEdt);

        dialog_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > Common.get_Decimaldigit(getApplicationContext())) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + Common.get_Decimaldigit(getApplicationContext()) + 1);
                        dialog_input.setText(s);
                        dialog_input.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    dialog_input.setText(s);
                    dialog_input.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        dialog_input.setText(s.subSequence(0, 1));
                        dialog_input.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(ScanUOM.this);
        alert.setTitle("Enter Quantity of Counted");

        alert.setView(v);

        String BarcodeStr = BarCodeTxt.getText().toString();
        dialog_BarCode.setText(BarcodeStr);
        dialog_ProductCode.setText(dbHelper.getProductcodeByBarcode(BarcodeStr));
        Double k;
        try {
            k = Double.parseDouble(countEdt.getText().toString());
        } catch (Exception e) {
            k = 0.00;
        }
        dialog_input.setText(Common.DoubleToString(k, Common.get_Decimaldigit(getApplicationContext())));
        dialog_input.setSelection(Common.DoubleToString(k, Common.get_Decimaldigit(getApplicationContext())).length());

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Double i;
                try {
                    i = Double.parseDouble(dialog_input.getText().toString());
                } catch (Exception e) {
                    i = 0.00;
                }
                countEdt.setText(Common.DoubleToString(i, Common.get_Decimaldigit(getApplicationContext())));
            }
        });

        alert.show();
    }

    private void saveScan() {
        String BarcodeStr = Pre_BarCodeTxt.getText().toString();
        String ProductcodeStr = Pre_ProductCode.getText().toString();

        Double i = Double.parseDouble(countEdt.getText().toString());
        if (isNegative.isChecked()) {
            i = i * -1;
        }
        dbHelper.AddOnStockTakeBasic(Location, BinLocation, BarcodeStr, ProductcodeStr, i);

        BarCodeTxt.setText(BarcodeStr);
        search(ProductcodeStr);
        BarCode.setText("");
        productcodeEdt.setText("");

        countEdt.setText("0");
        currentCounted = 0.00;
        isNegative.setChecked(false);
    }

    public void scan_onClick_Event(View view) {
        saveScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String selectedProduct = data.getStringExtra("selectedProduct");
            if (requestCode == 1) {
                //productcodeEdt.setText(selectedProduct);

                productcodeEdt.setText("");
                BarCode.setText("");
                countEdt.setText("1");

                String BarcodeStr = dbHelper.GetDefaultBarcode(selectedProduct);
                Pre_BarCodeTxt.setText(BarcodeStr);

                Pre_ProductCode.setText(selectedProduct);

                String Description = dbHelper.GetProductDescription(selectedProduct);
                Pre_Description.setText(Description);

                Double QuantityInStock = dbHelper.GetQuantityInStock(Location, selectedProduct);
                Pre_QuantityInStock.setText(String.valueOf(QuantityInStock));

                Double QuantityCounted = dbHelper.GetQuantityInCount(Location, selectedProduct);
                Pre_QuantityInCount.setText(String.valueOf(QuantityCounted));
            }
        }
    }
}
