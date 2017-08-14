package com.example.stocktake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ExportActivity extends AppCompatActivity {

    public static final String EXPORT_RESULT = "com.example.stocktake.intentservice.EXPORT_RESULT";
    public static final String UPLOAD_RESULT = "com.example.stocktake.intentservice.UPLOAD_RESULT";

    private LinearLayout mLyTaskContainer;

    private List<String> LocationList;

    private BroadcastReceiver exportDataReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction() == EXPORT_RESULT)
            {
                String fileName = intent.getStringExtra(ExportDataService.EXTRA_FILE_NAME);
                String ErrorMessage = intent.getStringExtra(ExportDataService.EXTRA_WRONG_MESSAGE);

                handleResult(fileName, ErrorMessage);
            }
            if (intent.getAction() == UPLOAD_RESULT)
            {
                String fileName = intent.getStringExtra(ExportDataService.EXTRA_FILE_NAME);
                String ErrorMessage = intent.getStringExtra(ExportDataService.EXTRA_WRONG_MESSAGE);

                handleUploadResult(fileName, ErrorMessage);
            }
        }
    };

    private void handleResult(String fileName, String ErrorMessage)
    {
        TextView tv = (TextView) mLyTaskContainer.findViewWithTag("E" + fileName);
        if (ErrorMessage.equals("")) {
            tv.setText(fileName + " export success ~~~ ");
        }
        else {
            tv.setText(fileName + " Wrong: " + ErrorMessage);
        }

        if (i < LocationList.size())  {
            addTask(this.mLyTaskContainer);
        }
        else {
            TextView tv_done = new TextView(this);
            mLyTaskContainer.addView(tv_done);
            tv_done.setText("Export Data Form Database to Files Done!");

            TextView tv_line = new TextView(this);
            mLyTaskContainer.addView(tv_line);
            tv_line.setText("---------------------------------------------");

            i = 0;
            addUploadTask(this.mLyTaskContainer);
        }
    }

    private void handleUploadResult(String fileName, String ErrorMessage)
    {
        TextView tv = (TextView) mLyTaskContainer.findViewWithTag("U" + fileName);
        if (ErrorMessage.equals("")) {
            tv.setText(fileName + " upload success ~~~ ");
        }
        else {
            tv.setText(fileName + " Wrong: " + ErrorMessage);
        }

        if (i < LocationList.size())  {
            addUploadTask(this.mLyTaskContainer);
        }
        else {
            TextView tv_done = new TextView(this);
            mLyTaskContainer.addView(tv_done);
            tv_done.setText("Upload Files Done!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        mLyTaskContainer = (LinearLayout) findViewById(R.id.activity_export);

        DBHelper dbHelper = new DBHelper(this);
        LocationList = dbHelper.GetLocationList();

        registerReceiver();

        addTask(this.mLyTaskContainer);
    }

    private void registerReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXPORT_RESULT);
        filter.addAction(UPLOAD_RESULT);
        registerReceiver(exportDataReceiver, filter);
    }

    int i = 0;

    public void addTask(View view)
    {
        if (i== LocationList.size()) {return;}

        String fileName = LocationList.get(i++);

        TextView tv = new TextView(this);
        mLyTaskContainer.addView(tv);
        tv.setText(fileName + " is Exporting ...");

        tv.setTag("E" + fileName);

        ExportDataService.startExport(this, fileName);
    }

    public void addUploadTask(View view)
    {
        if (i == LocationList.size()) {return;}

        String fileName = LocationList.get(i++);

        TextView tv = new TextView(this);
        mLyTaskContainer.addView(tv);
        tv.setText(fileName + " is Uploading ...");

        tv.setTag("U" + fileName);

        ExportDataService.startUpload(this, fileName);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(exportDataReceiver);
    }
}
