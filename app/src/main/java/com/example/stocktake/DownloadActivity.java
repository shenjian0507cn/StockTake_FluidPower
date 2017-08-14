package com.example.stocktake;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class DownloadActivity extends AppCompatActivity {

    String TAG = "FTP_Download";
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ll = (LinearLayout)findViewById(R.id.activity_download);

        AsyncFTP task = new AsyncFTP();
        task.execute();

    }

    public void Add_TextView(String msg) {
        TextView tv = new TextView(getApplicationContext());
        tv.setText(msg);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(16);
        ll.addView(tv);
    }

    private class AsyncFTP extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result = true;

            Log.i(TAG, "doInBackground");

            String FTP_ServerIP = Common.get_FtpServer(getApplicationContext());
            Integer FTP_ServerPort = Common.get_FtpPort(getApplicationContext());
            String FTP_Username = Common.get_FtpUsername(getApplicationContext());
            String FTP_Password = Common.get_FtpPassword(getApplicationContext());
            String FTP_DownloadPath = Common.get_FtpDownloadPath(getApplicationContext());

            FTPClient ftpClient = new FTPClient();

            int reply;

            try {
                ftpClient.connect(FTP_ServerIP, FTP_ServerPort);
                ftpClient.login(FTP_Username, FTP_Password);

                reply = ftpClient.getReplyCode();

                if (!FTPReply.isPositiveCompletion((reply))) {
                    publishProgress("Connection failed!");
                    ftpClient.disconnect();
                    result = false;
                }
                else {
                    publishProgress("Connected to FTP Server.");

                    if (ftpClient.changeWorkingDirectory(FTP_DownloadPath)) {

                    }
                    else
                    {
                        publishProgress("Working Directory " + FTP_DownloadPath + " Error!");
                    }

                    FTPFile[] ftpFiles = ftpClient.listFiles();

                    for (int i = 0; i < Common.DownloadFiles_Arr.length; i++) {
                        String CSV_Filename = Common.DownloadFiles_Arr[i] + ".csv";

                        Boolean isExisted = false;
                        for (FTPFile ftpFile: ftpFiles) {
                            if (ftpFile.getName().equals(CSV_Filename)) {
                                isExisted = true;
                                break;
                            }
                        }
                        if (isExisted) {
                            File f = new File(getFilesDir(), Common.importingCSVPath);
                            File file = new File(f, CSV_Filename);
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            Boolean download_flag = ftpClient.retrieveFile(CSV_Filename, fileOutputStream);
                            fileOutputStream.close();

                            if (download_flag) {
                                publishProgress(CSV_Filename + " downloaded.");
                            }
                            else {
                                publishProgress(CSV_Filename + " download failed!");
                            }


                        }
                        else {
                            publishProgress(CSV_Filename + " not found!");
                            result = false;
                        }
                    }


                    ftpClient.logout();
                    ftpClient.disconnect();
                }

            } catch (SocketException e) {
                Log.i(TAG, e.getMessage());
                publishProgress("Error:" + e.getMessage());
                result = false;
            } catch (IOException e) {
                Log.i(TAG, e.getMessage());
                publishProgress("Error:" + e.getMessage());
                result = false;
            }

            return  result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.i(TAG, "onPostExecute");

            if (result) {
                Add_TextView("All Done!");
            }
            else {
                Add_TextView("Download Failed!");
            }

        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");

            Add_TextView("Connecting to FTP Server!");
        }

        protected void onProgressUpdate(String... values) {
            Log.i(TAG, "onProgressUpdate");

            Add_TextView(values[0]);
            }
        }
}
