package com.example.stocktake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * Modified by james.shen on 07/07/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StockTake.db";

    //*********************************************************************************************
    //Define SQL string

    //Insert each stocktaking line into table StockTakeBasic, thus, it's easily to track.
    private static final String SQL_CREATE_StockTakeBasic =
            "CREATE TABLE IF NOT EXISTS StockTakeBasic" + " (" +
                    "LocationCode TEXT," +
                    "BinLocationCode TEXT," +
                    "BarCode TEXT," +
                    "ProductCode TEXT," +
                    "QuantityCounted Double," +
                    "CreatedDate Integer," +
                    "CreatedTime Integer)";

    private static final String SQL_DROP_StockTakeBasic =
            "DROP TABLE IF EXISTS StockTakeBasic";

    private static final String SQL_CREATE_ICPROD =
            "CREATE TABLE IF NOT EXISTS ICProd (" +
                    " ProductCode Text PRIMARY KEY, " +
                    " BarCode Text, " +
                    " Description Text)";

    private static final  String SQL_DROP_ICPROD = "DROP TABLE IF EXISTS ICProd";

    private static final String SQL_CREATE_ICQTY =
            "CREATE TABLE IF NOT EXISTS ICQty (" +
                    " LocationCode Text, " +
                    " BinLocationCode Text, " +
                    " ProductCode Text, " +
                    " QuantityInStock Double)";

    private static final String SQL_DROP_ICQTY = "DROP TABLE IF EXISTS ICQty";

    private static final String SQL_CREATE_ICLOCN =
            "CREATE TABLE IF NOT EXISTS ICLocn (" +
                    " LocationCode Text, " +
                    " LocationName Text, " +
                    " PRIMARY KEY(LocationCode))";

    private static final String SQL_DROP_ICLOCN = "DROP TABLE IF EXISTS ICLocn";

    private static final String SQL_CREATE_ICBAR =
            "CREATE TABLE IF NOT EXISTS ICBar (" +
                    " ProductCode Text, " +
                    " BarCode Text, " +
                    " UOMCode Text)";

    private static final String SQL_DROP_ICBAR = "DROP TABLE IF EXISTS ICBar";

    private static final String SQL_CREATE_ICUOM =
            "CREATE TABLE IF NOT EXISTS ICUom (" +
                    " ProductCode Text, " +
                    " UOMCode Text, " +
                    " UOMMultiplier Double)";

    private static final  String SQL_DROP_ICUOM = "DROP TABLE IF EXISTS ICUom";

    private static final String SQL_CREATE_BINLOCN =
            "CREATE TABLE IF NOT EXISTS BinLocn (" +
                    " LocationCode Text, " +
                    " BinLocationCode Text)";

    private static final  String SQL_DROP_BINLOCN = "DROP TABLE IF EXISTS BinLocn";

    //*********************************************************************************************

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    //*********************************************************************************************
    //Initialize table
    public void createStockTakeBasic()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL(SQL_DROP_StockTakeBasic);
        db.execSQL(SQL_CREATE_StockTakeBasic);
        db.close();
    }

    public void resetStockTakeBasic(String LocationList)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_CREATE_StockTakeBasic);
        db.execSQL("Delete from StockTakeBasic where Locationcode in (" + LocationList + ")");
        db.close();
    }

    public void createICPROD()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DROP_ICPROD);
        db.execSQL(SQL_CREATE_ICPROD);

        db.close();
    }

    public void insertICPROD(String ProductCode, String BarCode, String Description)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ProductCode", ProductCode);
        values.put("BarCode", BarCode);
        values.put("Description", Description);

        db.insert("ICPROD", null, values);

        db.close();
    }

    public void createICQTY()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DROP_ICQTY);
        db.execSQL(SQL_CREATE_ICQTY);

        db.close();
    }

    public void insertICQTY(String LocationCode, String BinLocationCode, String ProductCode, Double QuantityInStock)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("LocationCode", LocationCode);
        values.put("BinLocationCode", BinLocationCode);
        values.put("ProductCode", ProductCode);
        values.put("QuantityInStock", QuantityInStock);

        db.insert("ICQTY", null, values);

        db.close();
    }

    public void createICLOCN()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DROP_ICLOCN);
        db.execSQL(SQL_CREATE_ICLOCN);

        db.close();
    }

    public void insertICLOCN(String LocationCode, String LocationName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("LocationCode", LocationCode);
        values.put("LocationName", LocationName);

        db.insert("ICLOCN", null, values);

        db.close();
    }

    public void createICBAR()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DROP_ICBAR);
        db.execSQL(SQL_CREATE_ICBAR);

        db.close();
    }

    public void insertICBAR(String ProductCode, String BarCode, String UOMCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ProductCode", ProductCode);
        values.put("BarCode", BarCode);
        values.put("UOMCode", UOMCode);

        db.insert("ICBAR", null, values);

        db.close();
    }

    public void createICUOM()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DROP_ICUOM);
        db.execSQL(SQL_CREATE_ICUOM);

        db.close();
    }

    public void insertICUOM(String ProductCode, String UOMCode, Double UOMMultiplier)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ProductCode", ProductCode);
        values.put("UOMCode", UOMCode);
        values.put("UOMMultiplier", UOMMultiplier);

        db.insert("ICUOM", null, values);

        db.close();
    }

    public void createBINLOCN()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DROP_BINLOCN);
        db.execSQL(SQL_CREATE_BINLOCN);

        db.close();
    }

    public void insertBINLOCN(String LocationCode, String BinLocationCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("LocationCode", LocationCode);
        values.put("BinLocationCode", BinLocationCode);

        db.insert("BINLOCN", null, values);

        db.close();
    }

    //*********************************************************************************************
    public String getProductcodeByBarcode(String BarCode)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select ProductCode from ICBAR where BarCode='" + BarCode + "'";

        Cursor cursor = db.rawQuery(queryStr, null);

        String ProductCode = "";

        if (cursor.moveToNext()) {
            ProductCode = cursor.getString(0);
        }

        db.close();
        cursor.close();

        return ProductCode;
    }

    public void insertBasicLine(String LocationCode, String BinLocationCode, String Barcode, String ProductCode, Double Quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        String insertLine = "Insert into StockTakeBasic (LocationCode, BinLocationCode, BarCode, ProductCode, QuantityCounted, CreatedDate, CreatedTime) "
                + "Values ('" + LocationCode + "',"
                + "'" + BinLocationCode + "',"
                + "'" + Barcode + "',"
                + "'" + ProductCode + "',"
                + String.valueOf(Quantity)  + ", Date('now', 'localtime'), Time('now', 'localtime'))";
        db.execSQL(insertLine);
        db.close();
    }

    //This procedure is from old version in which one pair of Location and product only has one line
    //So, it need to check if one pair of Location and Product exist, then increase its quantity, otherwise insert new one
    //Current version, in order to track the whole process of stocktake, each stocktaking operation has one line in StocktakeBasic table
    public void AddOnStockTakeBasic(String LocationCode, String BinLocationCode, String BarCode, String ProductCode, Double Quantity)
    {
        this.insertBasicLine(LocationCode, BinLocationCode, BarCode, ProductCode, Quantity);
    }

    public List<String> GetLocationList()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select LocationCode from ICLOCN";

        Cursor cursor = db.rawQuery(queryStr, null);

        List<String> LocationList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String LocationCode = cursor.getString(cursor.getColumnIndexOrThrow("LocationCode"));
            LocationList.add(LocationCode);
        }

        db.close();
        cursor.close();

        return LocationList;
    }

    public List<String> GetBinLocationList(String LocationCode)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select BinLocationCode from BinLocn where LocationCode ='" + LocationCode + "'";

        Cursor cursor = db.rawQuery(queryStr, null);

        List<String> BinLocationList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String BinLocationCode = cursor.getString(cursor.getColumnIndexOrThrow("BinLocationCode"));
            BinLocationList.add(BinLocationCode);
        }

        db.close();
        cursor.close();

        return BinLocationList;
    }

    public ICProduct GetProductDetails(String LocationCode, String ProductCode)
    {
        String UOMCode = "";
        String Description = "";
        Double QuantityInStock = 0.0;
        Double UOMMultiplier = 0.0;

        ICProduct icProduct = new ICProduct();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select QuantityInStock from ICQty where LocationCode='" + LocationCode + "' and ProductCode='" + ProductCode + "'";
        Cursor cursor = db.rawQuery(queryStr, null);
        if (cursor.moveToNext()) {
            QuantityInStock = cursor.getDouble(cursor.getColumnIndexOrThrow("QuantityInStock"));
        }
        cursor.close();

        queryStr = "select Description from ICPROD where ProductCode='" + ProductCode + "'";

        cursor.close();
        cursor = db.rawQuery(queryStr, null);

        if (cursor.moveToNext()) {
            Description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
        }

        cursor.close();
        db.close();

        icProduct.set_ProductCode(ProductCode);
        icProduct.set_Description(Description);
        icProduct.set_UOMCode(UOMCode);
        icProduct.set_UOMMultiplier(UOMMultiplier);
        icProduct.set_QuantityInStock(QuantityInStock);

        icProduct.set_Counted(GetQuantityInCount(LocationCode, ProductCode));

        return icProduct;
    }

    public String GetDefaultBarcode(String ProductCode)
    {
        String Barcode = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String queryStr = "select BarCode from ICPROD where ProductCode='" + ProductCode + "'";

        Cursor cursor = db.rawQuery(queryStr, null);

        if (cursor.moveToNext()) {
            Barcode = cursor.getString(cursor.getColumnIndexOrThrow("BarCode"));
        }

        cursor.close();
        db.close();

        return Barcode;
    }

    public String GetProductDescription(String ProductCode)
    {
        String Description = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String queryStr = "select Description from ICPROD where ProductCode='" + ProductCode + "'";

        Cursor cursor = db.rawQuery(queryStr, null);

        if (cursor.moveToNext()) {
            Description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
        }

        cursor.close();
        db.close();

        return Description;
    }

    public Double GetQuantityInStock(String LocationCode, String ProductCode)
    {
        Double quantity = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select Sum(QuantityInStock) as QuantityInStock from ICQty where LocationCode='" + LocationCode + "' and ProductCode='" + ProductCode + "'";

        Cursor cursor = db.rawQuery(queryStr, null);

        if (cursor.moveToNext()) {
            quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("QuantityInStock"));
        }

        cursor.close();
        db.close();

        return quantity;
    }

    public double GetQuantityInCount(String LocationCode, String ProductCode)
    {
        double quantity = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select Sum(QuantityCounted) as QuantityCounted from StockTakeBasic where LocationCode='" + LocationCode + "' and ProductCode='" + ProductCode + "'";

        Cursor cursor = db.rawQuery(queryStr, null);

        if (cursor.moveToNext()) {
            quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("QuantityCounted"));
        }

        cursor.close();
        db.close();

        return quantity;
    }

    public List<String> StocktabkeByLocation(String LocationCode)
    {
        List<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select BinLocationCode, BarCode, ProductCode, QuantityCounted, CreatedDate, CreatedTime from StockTakeBasic where LocationCode='" + LocationCode + "'";

        Cursor cursor = db.rawQuery(queryStr, null);

        while (cursor.moveToNext()) {
            Double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("QuantityCounted"));
            String createdDate = cursor.getString(cursor.getColumnIndexOrThrow("CreatedDate"));
            String createdTime = cursor.getString(cursor.getColumnIndexOrThrow("CreatedTime"));

            String lineStr = cursor.getString(cursor.getColumnIndexOrThrow("BinLocationCode")) + "," +  cursor.getString(cursor.getColumnIndexOrThrow("BarCode")) + "," +
                    cursor.getString(cursor.getColumnIndexOrThrow("ProductCode"))  + "," +String.valueOf(quantity) + "," + createdDate + "," + createdTime;
            list.add(lineStr);
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<Map<String, String>> ProdductDescriptionQueryByProduct(String ProductCode) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select distinct ProductCode, Description from ICPROD where ProductCode like '" + ProductCode + "%'";

        if (ProductCode.equals("")) {
            queryStr =  "select distinct ProductCode, Description from ICPROD";
        }

        Cursor cursor = db.rawQuery(queryStr, null);

        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Code", cursor.getString(cursor.getColumnIndexOrThrow("ProductCode")));
            map.put("Description", cursor.getString(cursor.getColumnIndexOrThrow("Description")));
            list.add(map);
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<Map<String, String>> ProdductDescriptionQueryByDescription(String Description) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStr = "select distinct ProductCode, Description from ICPROD where Description like '%" + Description + "%'";

        if (Description.equals("")) {
            queryStr =  "select distinct ProductCode, Description from ICPROD";
        }

        Cursor cursor = db.rawQuery(queryStr, null);

        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Code", cursor.getString(cursor.getColumnIndexOrThrow("ProductCode")));
            map.put("Description", cursor.getString(cursor.getColumnIndexOrThrow("Description")));
            list.add(map);
        }

        cursor.close();
        db.close();

        return list;
    }
}
