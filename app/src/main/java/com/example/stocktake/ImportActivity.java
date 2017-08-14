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

public class ImportActivity extends AppCompatActivity {

    public static final String IMPORT_RESULT = "com.example.stocktake.intentservice.IMPORT_RESULT";

    private LinearLayout mLyTaskContainer;

    private BroadcastReceiver uploadImgReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction() == IMPORT_RESULT)
            {
                String tableName = intent.getStringExtra(importDataService.EXTRA_TABLE_NAME);

                handleResult(tableName);

            }
        }
    };

    private void handleResult(String tableName)
    {
        if (i < Common.DownloadFiles_Arr.length)  {
            TextView tv = (TextView) mLyTaskContainer.findViewWithTag(tableName);
            tv.setText(tableName + " import success ~~~ ");

            addTask(this.mLyTaskContainer);
        }
        else {
            TextView tv = (TextView) mLyTaskContainer.findViewWithTag(tableName);
            tv.setText(tableName + " import success ~~~ ");

            TextView tv_done = new TextView(this);
            mLyTaskContainer.addView(tv_done);
            tv_done.setText("All Done!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        mLyTaskContainer = (LinearLayout) findViewById(R.id.activity_import);

        registerReceiver();

        addTask(this.mLyTaskContainer);
    }

    private void registerReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IMPORT_RESULT);
        registerReceiver(uploadImgReceiver, filter);
    }

    int i = 0;

    public void addTask(View view)
    {
        if (i== Common.DownloadFiles_Arr.length) {return;}

        String tableName = Common.DownloadFiles_Arr[i++];
        importDataService.startImport(this, tableName);

        TextView tv = new TextView(this);
        mLyTaskContainer.addView(tv);
        tv.setText(tableName + " is Importing ...");

        tv.setTag(tableName);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(uploadImgReceiver);
    }
}
