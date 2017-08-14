package com.example.stocktake;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity {

    public final static String IMPORT_MESSAGE = "com.example.stocktake.IMPORTMESSAGE";

    private Button btnDownload;
    private Button btnImport;
    private Button btnReset;
    private Button btnScan;
    private Button btnUpload;
    private Button btnSearch;
    private Button btnSetup;

    ProgressDialog progressDialog = null;

    private int Download_Msg = 0;
    private int Failure_Msg = 1;

    private DBHelper dbHelper;

    private Boolean isValidated;
    private final String SERVICE_NAMESPACE  = "http://ws.apache.org/axis2";
    private final String SERVICE_URL  = "http://192.168.69.21:8080/axis2/services/WebServiceTest1";
    private final String SOAP_ACTION = "http://ws.apache.org/axis2/validLicence";
    private final String METHOD_NAME = "validLicence";
    private String TAG = "PGGURU";
    private static String Licence;
    private static String ResultOfValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        btnDownload = (Button) findViewById(R.id.Button_Download);
        btnDownload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isValidated = false;

                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setTitle("Tip");
                        progressDialog.setMessage("Validating Application Licence...");
                        progressDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(true);
                        progressDialog.setButton("Terminate", new SureButtonListener());
                        progressDialog.show();

                        new Thread() {
                            public void run() {
                                /*
                                HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
                                try {
                                    ht.debug = true;

                                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

                                    SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE, METHOD_NAME);
                                    soapObject.addProperty("Licence", Common.get_ApplicationLicence());
                                    envelope.bodyOut = soapObject;

                                    ht.call(SERVICE_NAMESPACE + METHOD_NAME, envelope);

                                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                                    downloadHandler.sendEmptyMessage(Integer.parseInt(response.toString()));
                                } catch (SoapFault e) {
                                    downloadHandler.sendEmptyMessage(0);
                                } catch (IOException e) {
                                    downloadHandler.sendEmptyMessage(0);
                                } catch (XmlPullParserException e) {
                                    downloadHandler.sendEmptyMessage(0);
                                }
                                */
                                downloadHandler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                }
        );

        btnImport = (Button) findViewById(R.id.Button_Import);
        btnImport.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ImportActivity.class);
                        startActivity(intent);
                    }
                }
        );

        btnReset = (Button) findViewById(R.id.Button_Reset);
        btnReset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setIcon(R.drawable.ic_notifications_black_24dp);
                        builder.setTitle("Reset Stocktake");

                        List<String> locationList = dbHelper.GetLocationList();
                        final String[] items = locationList.toArray(new String[locationList.size()]);

                        final boolean[] checked = new boolean[items.length];
                        for (int i = 0; i < checked.length; i++) {
                            checked[i] = true;
                        }
                        builder.setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        checked[which] = isChecked;
                                    }
                                }
                        );
                        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = "";
                                for (int i = 0; i < items.length; i++) {
                                    if (checked[i]) {
                                        s = s + "'" + items[i] + "'" + ",";
                                    }
                                }
                                if (s.length() > 0) {
                                    s = s.substring(0, s.length() - 1);
                                }
                                dbHelper.resetStockTakeBasic(s);
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.create().show();
                    }
                }
        );

        btnScan = (Button) findViewById(R.id.Button_Scan);
        btnScan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> locationList = dbHelper.GetLocationList();
                        if (locationList.size() > 1) {
                            final String[] items = locationList.toArray(new String[locationList.size()]);

                            AlertDialog.Builder scanDialog = new AlertDialog.Builder(MainActivity.this);
                            scanDialog.setTitle("Select Location")
                                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Toast.makeText(MainActivity.this, "You clicked " + items[which], Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setPositiveButton("New Scan", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            int itemIndex = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                            Intent intent = new Intent(MainActivity.this, ScanUOM.class);
                                            intent.putExtra("LocationCode", items[itemIndex]);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            scanDialog.show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, ScanUOM.class);
                            intent.putExtra("LocationCode", locationList.get(0));
                            startActivity(intent);
                        }
                    }
                }
        );

        btnUpload = (Button) findViewById(R.id.Button_Upload);
        btnUpload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isValidated = false;

                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setTitle("Tip");
                        progressDialog.setMessage("Validating Application Licence...");
                        progressDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(true);
                        progressDialog.setButton("Terminate", new SureButtonListener());
                        progressDialog.show();

                        new Thread() {
                            public void run() {
                                //Codes below for licence validation
                                /*
                                HttpTransportSE ht = new HttpTransportSE(SERVICE_URL);
                                try {
                                    ht.debug = true;

                                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

                                    SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE, METHOD_NAME);
                                    soapObject.addProperty("Licence", Common.get_ApplicationLicence());
                                    envelope.bodyOut = soapObject;

                                    ht.call(SERVICE_NAMESPACE + METHOD_NAME, envelope);

                                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                                    uploadHandler.sendEmptyMessage(Integer.parseInt(response.toString()));
                                } catch (SoapFault e) {
                                    uploadHandler.sendEmptyMessage(0);
                                } catch (IOException e) {
                                    uploadHandler.sendEmptyMessage(0);
                                } catch (XmlPullParserException e) {
                                    uploadHandler.sendEmptyMessage(0);
                                }
                                */
                                uploadHandler.sendEmptyMessage(1);
                            }
                        }.start();
                    }
                }
        );

        btnSearch = (Button) findViewById(R.id.Button_Search);
        btnSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                    }
                }

        );

        btnSetup = (Button) findViewById(R.id.Button_Setup);
        btnSetup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);

                        //ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        //toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                    }
                }
        );
    }

    private Boolean CheckDir(String dirStr) {
        if (dirStr.charAt(0) != '/') return false;

        File f = new File(getFilesDir(), dirStr);
        if (!f.exists()) {
            f.mkdirs();
        }
        return true;
    }

    private class SureButtonListener implements android.content.DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    Handler downloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String tips;
            switch (msg.what) {
                case 1:
                    tips = "Validatation Succeed!";
                    isValidated = true;
                    break;
                case 2:
                    tips = "Licence expried!";
                    isValidated = false;
                    break;
                case 3:
                    tips = "Licence doesn't exist!";
                    isValidated = false;
                    break;
                default:
                    tips = "System Error!";
                    isValidated = false;
                    break;
            }
            progressDialog.cancel();
            Toast.makeText(getApplicationContext(), tips, Toast.LENGTH_SHORT).show();

            if (isValidated) {
                if (!CheckDir(Common.importingCSVPath)) {
                    Toast.makeText(MainActivity.this, "Importing CSV Path is Wrong!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                    startActivity(intent);
                }
            }
        }
    };

    Handler uploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String tips;
            switch (msg.what) {
                case 1:
                    tips = "Validatation Succeed!";
                    isValidated = true;
                    break;
                case 2:
                    tips = "Licence expried!";
                    isValidated = false;
                    break;
                case 3:
                    tips = "Licence doesn't exist!";
                    isValidated = false;
                    break;
                default:
                    tips = "System Error!";
                    isValidated = false;
                    break;
            }
            progressDialog.cancel();
            Toast.makeText(getApplicationContext(), tips, Toast.LENGTH_SHORT).show();

            if (isValidated) {
                if (!CheckDir(Common.exportingCSVPath)) {
                    Toast.makeText(MainActivity.this, "Exporting CSV Path is Wrong!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, ExportActivity.class);
                    startActivity(intent);
                }
            }
        }
    };
}
