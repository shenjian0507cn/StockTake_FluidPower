package com.example.stocktake;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;


/**
 * Created by james.shen on 26/01/2017.
 */

public class ExportDataService extends IntentService {
    private static final String ACTION_EXPORT_TABLE = "com.example.stocktake.intentservice.action.EXPORT_TABLE";
    private static final String ACTION_UPLOAD_FILE = "com.example.stocktake.intentservice.action.UPLOAD_FILE";
    public static final String EXTRA_FILE_NAME= "com.example.stocktake.intentservice.extra.FILE_NAME";
    public static final String EXTRA_WRONG_MESSAGE = "com.example.stocktake.intentservice.extra.WRONG_MESSAGE";

    private String ErrorMessage = "";

    String TAG = "Export_Upload";

    public static void startExport(Context context, String fileName)
    {
        Intent intent = new Intent(context, ExportDataService.class);
        intent.setAction(ACTION_EXPORT_TABLE);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        context.startService(intent);
    }

    public static void startUpload(Context context, String fileName)
    {
        Intent intent = new Intent(context, ExportDataService.class);
        intent.setAction(ACTION_UPLOAD_FILE);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        context.startService(intent);
    }

    public ExportDataService()
    {
        super("ExportDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_EXPORT_TABLE.equals(action))
            {
                final String fileName = intent.getStringExtra(EXTRA_FILE_NAME);
                handleExport(fileName);
            }
            if (ACTION_UPLOAD_FILE.equals(action))
            {
                final String fileName = intent.getStringExtra(EXTRA_FILE_NAME);
                handleUpload(fileName);
            }
        }
    }

    private void handleExport(String fileName)
    {
        exportFile(fileName);

        Intent intent = new Intent(ExportActivity.EXPORT_RESULT);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        intent.putExtra(EXTRA_WRONG_MESSAGE, ErrorMessage);
        sendBroadcast(intent);
    }

    private void handleUpload(String fileName)
    {
        uploadFile(fileName);

        Intent intent = new Intent(ExportActivity.UPLOAD_RESULT);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        intent.putExtra(EXTRA_WRONG_MESSAGE, ErrorMessage);
        sendBroadcast(intent);

    }

    private void exportFile(String LocationCode)
    {
        File appPath = new File(getFilesDir(), Common.exportingCSVPath);
        File file = new File(appPath, "Stocktake_" + LocationCode + ".csv");

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        List<String> list = dbHelper.StocktabkeByLocation(LocationCode);

        ErrorMessage = "";

        try {
            FileOutputStream fos = new FileOutputStream(file);
            for (int i =0; i < list.size(); i++)
            {
                fos.write(list.get(i).getBytes());
                fos.write("\r\n".getBytes());
            }
            fos.close();
        } catch (Exception e) {
            ErrorMessage = e.getMessage();
        }
    }

    private void  uploadFile(String fileName)
    {
        String Server_IP = Common.get_FtpServer(getApplicationContext());
        int Server_Port = Common.get_FtpPort(getApplicationContext());
        String UserName = Common.get_FtpUsername(getApplicationContext());
        String Password = Common.get_FtpPassword(getApplicationContext());
        String Upload_Path = Common.get_FtpUploadPath(getApplicationContext());

        ErrorMessage = "";

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(Server_IP, Server_Port);
            if (ftpClient.isConnected()) {
                ftpClient.login(UserName, Password);

                ftpClient.changeWorkingDirectory(Upload_Path);

                File appPath = new File(getFilesDir(), Common.exportingCSVPath);
                File file = new File(appPath, "Stocktake_" + fileName + ".csv");
                FileInputStream fileInputStream = new FileInputStream(file);
                ftpClient.storeFile("Stocktake_" + fileName + ".csv", fileInputStream);

                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
        catch (Exception e) {
            ErrorMessage = e.getMessage();
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG,"onCreate");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }
}
