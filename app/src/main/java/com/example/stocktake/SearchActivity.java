package com.example.stocktake;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.generateViewId;

public class SearchActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    private EditText BarCodeEdt;
    private EditText ProductCodeEdt;

    private List<String> LocationList;

    private int MinimumHeight = 36;
    private String ProductCodeTxt_Tag = "Main_ProductCode_Txt";
    private String DescriptionTxt_Tag = "Main_Description_Txt";
    private String QuantityInStockTxt_Tag = "Main_QtyInStock_Txt";
    private String QuantityInCountTxt_Tag = "Main_Counted_Txt";

    private void AddMainLayout() {
        LinearLayout searchviewLayout = (LinearLayout)findViewById(R.id.search_view);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,5,0,0);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setLayoutParams(lp);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setTag("Main");
        mainLayout.setBackgroundColor(Color.parseColor("#99CC66"));
        mainLayout.setMinimumHeight(48);

        //Product
        LinearLayout productLayout = new LinearLayout(this);
        productLayout.setOrientation(LinearLayout.HORIZONTAL);
        productLayout.setLayoutParams(lp);
        productLayout.setMinimumHeight(MinimumHeight);

        TextView[] textViews = new TextView[2];

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,2);
        textViews[0] = new TextView(this);
        textViews[0].setLayoutParams(lp1);
        textViews[0].setGravity(Gravity.CENTER_VERTICAL);
        textViews[0].setText("ProductCode");
        textViews[0].setWidth(0);
        textViews[0].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[0].setMinHeight(MinimumHeight);
        productLayout.addView(textViews[0]);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,5);
        textViews[1] = new TextView(this);
        textViews[1].setLayoutParams(lp2);
        textViews[1].setGravity(Gravity.CENTER_VERTICAL);
        textViews[1].setWidth(0);
        textViews[1].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[1].setMinHeight(MinimumHeight);
        textViews[1].setTag(ProductCodeTxt_Tag);
        productLayout.addView(textViews[1]);

        mainLayout.addView(productLayout);

        //Description
        LinearLayout descriptionLayout = new LinearLayout(this);
        descriptionLayout.setOrientation(LinearLayout.HORIZONTAL);
        descriptionLayout.setLayoutParams(lp);
        descriptionLayout.setMinimumHeight(MinimumHeight);

        textViews = new TextView[2];

        textViews[0] = new TextView(this);
        textViews[0].setLayoutParams(lp1);
        textViews[0].setGravity(Gravity.CENTER_VERTICAL);
        textViews[0].setText("Description");
        textViews[0].setWidth(0);
        textViews[0].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[0].setMinHeight(MinimumHeight);
        descriptionLayout.addView(textViews[0]);

        textViews[1] = new TextView(this);
        textViews[1].setLayoutParams(lp2);
        textViews[1].setGravity(Gravity.CENTER_VERTICAL);
        textViews[1].setWidth(0);
        textViews[1].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[1].setMinHeight(MinimumHeight);
        textViews[1].setTag(DescriptionTxt_Tag);
        descriptionLayout.addView(textViews[1]);

        mainLayout.addView(descriptionLayout);

        //QtyInStock
        LinearLayout QtyLayout = new LinearLayout(this);
        QtyLayout.setOrientation(LinearLayout.HORIZONTAL);
        QtyLayout.setLayoutParams(lp);
        QtyLayout.setMinimumHeight(MinimumHeight);

        textViews = new TextView[2];

        textViews[0] = new TextView(this);
        textViews[0].setLayoutParams(lp1);
        textViews[0].setGravity(Gravity.CENTER_VERTICAL);
        textViews[0].setText("Total QtyInStock");
        textViews[0].setWidth(0);
        textViews[0].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[0].setMinHeight(MinimumHeight);
        QtyLayout.addView(textViews[0]);

        textViews[1] = new TextView(this);
        textViews[1].setLayoutParams(lp2);
        textViews[1].setGravity(Gravity.CENTER_VERTICAL);
        textViews[1].setWidth(0);
        textViews[1].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[1].setMinHeight(MinimumHeight);
        textViews[1].setTag(QuantityInStockTxt_Tag);
        QtyLayout.addView(textViews[1]);

        mainLayout.addView(QtyLayout);

        //Counted
        LinearLayout countedLayout = new LinearLayout(this);
        countedLayout.setOrientation(LinearLayout.HORIZONTAL);
        countedLayout.setLayoutParams(lp);
        countedLayout.setMinimumHeight(MinimumHeight);

        textViews = new TextView[2];

        textViews[0] = new TextView(this);
        textViews[0].setLayoutParams(lp1);
        textViews[0].setGravity(Gravity.CENTER_VERTICAL);
        textViews[0].setText("Total Counted");
        textViews[0].setWidth(0);
        textViews[0].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[0].setMinHeight(MinimumHeight);
        countedLayout.addView(textViews[0]);

        textViews[1] = new TextView(this);
        textViews[1].setLayoutParams(lp2);
        textViews[1].setGravity(Gravity.CENTER_VERTICAL);
        textViews[1].setWidth(0);
        textViews[1].setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        textViews[1].setMinHeight(MinimumHeight);
        textViews[1].setTag(QuantityInCountTxt_Tag);
        countedLayout.addView(textViews[1]);

        mainLayout.addView(countedLayout);

        searchviewLayout.addView(mainLayout);
    }

    private void AddLocationLayout() {
        LinearLayout searchviewLayout = (LinearLayout)findViewById(R.id.search_view);

        for (int i=0; i<LocationList.size(); i++) {
            String locationCode = LocationList.get(i);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,5,0,0);

            //Location Layout
            LinearLayout locationLayout = new LinearLayout(this);
            locationLayout.setLayoutParams(lp);
            locationLayout.setOrientation(LinearLayout.VERTICAL);
            locationLayout.setTag(locationCode);
            locationLayout.setBackgroundColor(Color.parseColor("#99CC66"));
            locationLayout.setMinimumHeight(48);

            //Location Layout - Label Layout
            LinearLayout labelLayout = new LinearLayout(this);
            labelLayout.setOrientation(LinearLayout.HORIZONTAL);
            labelLayout.setLayoutParams(lp);
            labelLayout.setMinimumHeight(48);
            TextView locationTxt = new TextView(this);
            locationTxt.setLayoutParams(lp);
            locationTxt.setText(locationCode);
            locationTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            locationTxt.setTypeface(null, Typeface.BOLD_ITALIC);
            locationLayout.addView(locationTxt);

            //Location Layout - QtyInStock Layout
            LinearLayout QtyLayout = new LinearLayout(this);
            QtyLayout.setOrientation(LinearLayout.HORIZONTAL);
            QtyLayout.setLayoutParams(lp);
            QtyLayout.setMinimumHeight(MinimumHeight);
            QtyLayout.setTag(locationCode + "_QtyInStock");

            TextView[] textViews = new TextView[2];

            //Location Layout - QtyInStock Layout - Caption
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,2);
            textViews[0] = new TextView(this);
            textViews[0].setLayoutParams(lp1);
            textViews[0].setGravity(Gravity.CENTER_VERTICAL);
            textViews[0].setText("QtyInStock");
            textViews[0].setWidth(0);
            QtyLayout.addView(textViews[0]);

            //Location Layout - QtyInStock Layout - Txt
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,5);
            textViews[1] = new TextView(this);
            textViews[1].setLayoutParams(lp2);
            textViews[1].setGravity(Gravity.CENTER_VERTICAL);
            textViews[1].setWidth(0);
            textViews[1].setTag(locationCode + "_QtyInStock_Txt");
            QtyLayout.addView(textViews[1]);

            locationLayout.addView(QtyLayout);

            //Location Layout - Counted Layout
            LinearLayout CountedLayout = new LinearLayout(this);
            CountedLayout.setOrientation(LinearLayout.HORIZONTAL);
            CountedLayout.setLayoutParams(lp);
            CountedLayout.setMinimumHeight(MinimumHeight);
            CountedLayout.setTag(locationCode + "_Counted");

            textViews = new TextView[2];

            //Location Layout - Counted Layout - Caption
            textViews[0] = new TextView(this);
            textViews[0].setLayoutParams(lp1);
            textViews[0].setGravity(Gravity.CENTER_VERTICAL);
            textViews[0].setText("Counted");
            textViews[0].setWidth(0);
            CountedLayout.addView(textViews[0]);

            //Location Layout - Counted Layout - Txt
            textViews[1] = new TextView(this);
            textViews[1].setLayoutParams(lp2);
            textViews[1].setGravity(Gravity.CENTER_VERTICAL);
            textViews[1].setWidth(0);
            textViews[1].setTag(locationCode + "_Counted_Txt");
            CountedLayout.addView(textViews[1]);

            locationLayout.addView(CountedLayout);

            searchviewLayout.addView(locationLayout);
        }
    }

    private void clearLinearLayout(LinearLayout currenLayout, List<String> TagList) {
        for (int i=0; i < TagList.size(); i++) {
            LinearLayout l = (LinearLayout)currenLayout.findViewWithTag(TagList.get(i).toString());
            if (l != null) {
                l.removeAllViewsInLayout();
                currenLayout.removeViewInLayout(l);
            }
        }
    }

    private void AddUOMLine(LinearLayout currenLayout, String UOMCode, double Counted) {
        LinearLayout l = (LinearLayout)currenLayout.findViewWithTag(UOMCode);
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
        linearLayout.setMinimumHeight(MinimumHeight);
        linearLayout.setTag(UOMCode);

        TextView[] textViews = new TextView[2];

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,2);
        textViews[0] = new TextView(this);
        textViews[0].setLayoutParams(lp1);
        textViews[0].setGravity(Gravity.CENTER_VERTICAL);
        textViews[0].setText(UOMCode);
        textViews[0].setWidth(0);
        linearLayout.addView(textViews[0]);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,5);
        textViews[1] = new TextView(this);
        textViews[1].setLayoutParams(lp2);
        textViews[1].setGravity(Gravity.CENTER_VERTICAL);
        textViews[1].setText(String.valueOf(Counted));
        textViews[1].setWidth(0);
        textViews[1].setTag(UOMCode + "TV");
        linearLayout.addView(textViews[1]);

        currenLayout.addView(linearLayout);
    }

    private void clearMainlayout() {
        LinearLayout searchView = (LinearLayout)findViewById(R.id.search_view);
        LinearLayout mainLayout = (LinearLayout)searchView.findViewWithTag("Main");
        searchView.removeViewInLayout(mainLayout);
    }

    private void clearLocationLayout() {
        LinearLayout searchView = (LinearLayout)findViewById(R.id.search_view);
        for (int i=0; i<LocationList.size(); i++) {
            LinearLayout locationLayout = (LinearLayout)searchView.findViewWithTag(LocationList.get(i));
            searchView.removeViewInLayout(locationLayout);
        }
    }

    public void search(String ProductcodeStr) {
        List<ICProduct> productLocation = new ArrayList<ICProduct>();

        List<String> UomcodeList = new ArrayList<String>();
        List<Double> UomcountedList = new ArrayList<Double>();

        Double TotalQty = 0.0;
        Double TotalCounted = 0.0;

        for (int i=0; i<LocationList.size();i++) {
            ICProduct icProduct = dbHelper.GetProductDetails(LocationList.get(i), ProductcodeStr);
            productLocation.add(icProduct);

            TotalQty = TotalQty + icProduct.get_QuantityInStock();
            TotalCounted = TotalCounted + icProduct.get_Counted();

            /*
            List<UOMCount> uomCounts = icProduct.get_uomCountList();
            for (int j=0; j<uomCounts.size();j++) {
                int index = UomcodeList.indexOf(uomCounts.get(j).get_UOMCode());
                if ( index == -1) {
                    UomcodeList.add(uomCounts.get(j).get_UOMCode());
                    UomcountedList.add(uomCounts.get(j).get_Count());
                }
                else {
                    UomcountedList.set(index, UomcountedList.get(index) + uomCounts.get(j).get_Count());
                }
            }
            */
        }

        clearMainlayout();
        clearLocationLayout();

        AddMainLayout();

        LinearLayout searchView = (LinearLayout)findViewById(R.id.search_view);
        LinearLayout mainLayout = (LinearLayout)searchView.findViewWithTag("Main");

        TextView ProductCodeTxt = (TextView)mainLayout.findViewWithTag(ProductCodeTxt_Tag );
        ProductCodeTxt.setText(productLocation.get(0).get_ProductCode());

        TextView DescriptionTxt = (TextView)mainLayout.findViewWithTag(DescriptionTxt_Tag );
        DescriptionTxt.setText(productLocation.get(0).get_Description());

        TextView QuantityInStockTxt = (TextView)mainLayout.findViewWithTag(QuantityInStockTxt_Tag );
        QuantityInStockTxt.setText(String.valueOf(TotalQty));

        TextView QuantityInCountTxt = (TextView)mainLayout.findViewWithTag(QuantityInCountTxt_Tag );
        QuantityInCountTxt.setText(String.valueOf(TotalCounted));

        /*
        for (int i=0; i<UomcodeList.size(); i++) {
            AddUOMLine(mainLayout, UomcodeList.get(i), UomcountedList.get(i));
        }
        */

        if (LocationList.size() > 1) {
            AddLocationLayout();

            for (int i = 0; i < LocationList.size(); i++) {
                ICProduct icProduct = productLocation.get(i);
                TextView qtyTxt = (TextView) searchView.findViewWithTag(LocationList.get(i) + "_QtyInStock_Txt");
                qtyTxt.setText(String.valueOf(icProduct.get_QuantityInStock()));

                TextView countedTxt = (TextView) searchView.findViewWithTag(LocationList.get(i) + "_Counted_Txt");
                countedTxt.setText(String.valueOf(icProduct.get_Counted()));

                /*
                List<UOMCount> uomCounts = icProduct.get_uomCountList();
                for (int j = 0; j < uomCounts.size(); j++) {
                    UOMCount uomCount = uomCounts.get(j);
                    LinearLayout linearLayout = (LinearLayout) searchView.findViewWithTag(LocationList.get(i));
                    AddUOMLine(linearLayout, uomCount.get_UOMCode(), uomCount.get_Count());
                }
                */
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DBHelper(getApplicationContext());

        BarCodeEdt = (EditText)findViewById(R.id.barcodeEdt);
        BarCodeEdt.addTextChangedListener(barcodeWatcher);

        ProductCodeEdt = (EditText)findViewById(R.id.productcodeEdt);
        ProductCodeEdt.setFocusable(false);
        ProductCodeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ProductActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        LocationList = dbHelper.GetLocationList();

        AddMainLayout();

        if (LocationList.size() > 1) {
            AddLocationLayout();
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
                search(dbHelper.getProductcodeByBarcode(BarcodeStr));
                BarCodeEdt.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) return;
        }
    };

    public void search_onClick_Event(View view) {
        String BarcodeStr;

        if (TextUtils.isEmpty(BarCodeEdt.getText())) {
            if (TextUtils.isEmpty(ProductCodeEdt.getText())) {
                Toast.makeText(SearchActivity.this, "Please Enter BarCode or ProductCode", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                String Productcode = ProductCodeEdt.getText().toString();
                search(Productcode);
                ProductCodeEdt.setText("");
            }
        }
        else {
            BarcodeStr = BarCodeEdt.getText().toString();
            search(dbHelper.getProductcodeByBarcode(BarcodeStr));
            BarCodeEdt.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String selectedProduct = data.getStringExtra("selectedProduct");
            if (requestCode == 1) {
                ProductCodeEdt.setText(selectedProduct);
            }
        }
    }
}
