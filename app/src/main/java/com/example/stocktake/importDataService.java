package com.example.stocktake;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by james.shen on 24/01/2017.
 */

public class importDataService extends IntentService {
    private static final String ACTION_IMPORT_TABLE = "com.example.stocktake.intentservice.action.IMPORT_TABLE";
    public static final String EXTRA_TABLE_NAME= "com.example.stocktake.intentservice.extra.TABLE_NAME";


    public static void startImport(Context context, String tableName)
    {
        Intent intent = new Intent(context, importDataService.class);
        intent.setAction(ACTION_IMPORT_TABLE);
        intent.putExtra(EXTRA_TABLE_NAME, tableName);
        context.startService(intent);

    }

    public importDataService()
    {
        super("importDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_IMPORT_TABLE.equals(action))
            {
                final String tableName = intent.getStringExtra(EXTRA_TABLE_NAME);
                handleImport(tableName);
            }
        }
    }

    private void handleImport(String tableName)
    {
        if (tableName.toUpperCase().contains("ICPROD")) {
            importICProd();
        }
        if (tableName.toUpperCase().contains("ICQTY")) {
            importICQty();
        }
        if (tableName.toUpperCase().contains("ICLOCN")) {
            importICLocn();
        }
        if (tableName.toUpperCase().contains("ICBAR")) {
            importICBar();
        }
        if (tableName.toUpperCase().contains("ICUOM")) {
            importICUom();
        }
        if (tableName.toUpperCase().contains("BINLOCN")) {
            importBinLocn();
        }

        Intent intent = new Intent(ImportActivity.IMPORT_RESULT);
        intent.putExtra(EXTRA_TABLE_NAME, tableName);
        sendBroadcast(intent);
    }

    private void importICProd()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createICPROD();

        File appPath = this.getFilesDir();
        String appPathStr = appPath.toString();

        File file = new File(appPathStr + Common.importingCSVPath, "ICProd.csv");
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "Not Exists",Toast.LENGTH_SHORT).show();
        }
        else {
            String inString = "";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                Integer count;
                count = 1;
                while ((inString = reader.readLine()) != null) {
                    String[] strArr;
                    strArr = inString.split(",");
                    if (strArr.length == 3) {
                        dbHelper.insertICPROD(strArr[0], strArr[1], strArr[2]);
                    }
                    else
                    {
                        dbHelper.insertICPROD("PRO" + String.valueOf(count), "Bar" + String.valueOf(count), "Desc" + String.valueOf(count));
                    }

                    count++;
                }
            } catch (FileNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "ICProd.CSV Not Exists", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "IOException Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void importICQty()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createICQTY();

        File appPath = this.getFilesDir();
        String appPathStr = appPath.toString();

        File file = new File(appPathStr + Common.importingCSVPath, "ICQty.csv");
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "ICQty.CSV Not Exists",Toast.LENGTH_SHORT).show();
        }
        else {
            String inString = "";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while ((inString = reader.readLine()) != null) {
                    String[] strArr;
                    strArr = inString.split(",");
                    dbHelper.insertICQTY(strArr[0], strArr[1], strArr[2], Double.parseDouble(strArr[3]));
                }
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Not Exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void importICLocn()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createICLOCN();

        File appPath = this.getFilesDir();
        String appPathStr = appPath.toString();

        File file = new File(appPathStr + Common.importingCSVPath, "ICLocn.csv");
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "ICLocn.CSV Not Exists",Toast.LENGTH_SHORT).show();
        }
        else {
            String inString = "";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while ((inString = reader.readLine()) != null) {
                    String[] strArr;
                    strArr = inString.split(",");
                    dbHelper.insertICLOCN(strArr[0], strArr[1]);
                }
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Not Exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void importICBar()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createICBAR();

        File appPath = this.getFilesDir();
        String appPathStr = appPath.toString();

        File file = new File(appPathStr + Common.importingCSVPath, "ICBar.csv");
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "ICBar.CSV Not Exists",Toast.LENGTH_SHORT).show();
        }
        else {
            String inString = "";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while ((inString = reader.readLine()) != null) {
                    String[] strArr;
                    strArr = inString.split(",");
                    dbHelper.insertICBAR(strArr[0], strArr[1], strArr[2]);
                }
            } catch (FileNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "Not Exists", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "IOException Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void importICUom()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createICUOM();

        File appPath = this.getFilesDir();
        String appPathStr = appPath.toString();

        File file = new File(appPathStr + Common.importingCSVPath, "ICUom.csv");
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "ICUom.CSV Not Exists",Toast.LENGTH_SHORT).show();
        }
        else {
            String inString = "";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while ((inString = reader.readLine()) != null) {
                    String[] strArr;
                    strArr = inString.split(",");
                    dbHelper.insertICUOM(strArr[0], strArr[1], Double.parseDouble(strArr[2]));
                }
            } catch (FileNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "Not Exists", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "IOException Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void importBinLocn()
    {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createBINLOCN();

        File appPath = this.getFilesDir();
        String appPathStr = appPath.toString();

        File file = new File(appPathStr + Common.importingCSVPath, "BinLocn.csv");
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "BinLocn.CSV Not Exists",Toast.LENGTH_SHORT).show();
        }
        else {
            String inString = "";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while ((inString = reader.readLine()) != null) {
                    String[] strArr;
                    strArr = inString.split(",");
                    if (strArr.length == 2) {
                        dbHelper.insertBINLOCN(strArr[0], strArr[1]);
                    }
                    else {
                        dbHelper.insertBINLOCN(strArr[0], "");
                    }
                }
            } catch (FileNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "Not Exists", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "IOException Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("TAG","onCreate");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("TAG","onDestroy");
    }
}
