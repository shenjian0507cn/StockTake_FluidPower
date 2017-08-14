package com.example.stocktake;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.math.BigDecimal;
import android.icu.text.DecimalFormat;
import android.preference.PreferenceManager;
import java.text.*;

/**
 * Modified by james.shen on 07/07/2017.
 */

public class Common {
    private final  static int DecimalDigit = 1;

    public static int get_Decimaldigit(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int result = Integer.parseInt(settings.getString("Common_DecimalPercision", String.valueOf(DecimalDigit)));
        return result;
    }

    public static String DoubleToString(double d, int decimalDigit) {
        java.math.BigDecimal bigDecimal = new java.math.BigDecimal(d);
        double k = bigDecimal.setScale(decimalDigit, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();


        String FormatStr = "0.";
        for (int i=1; i <= decimalDigit; i++) {
            FormatStr = FormatStr + "0";
        }
        if (decimalDigit == 0) {
            FormatStr = "";
        }
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat(FormatStr);

        return decimalFormat.format(k);
    }

    private final static Boolean AutoAdding = true;
    public static Boolean get_AuotAdding(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean result = Boolean.valueOf(settings.getBoolean("Common_AutoAdding", AutoAdding));

        return result;
    }

    private final static  Boolean AutoSaving = true;
    public static Boolean get_AutoSaving(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean result = Boolean.valueOf(settings.getBoolean("Common_AutoSaving", AutoSaving));

        return result;
    }

    private final static String Licence = "0010010000000001";

    public final static String importingCSVPath = "/CSVFiles/Import";
    public final static String exportingCSVPath = "/CSVFiles/Export";

    public final static String[] DownloadFiles_Arr = {"ICProd","ICLocn","ICQty", "ICBar", "ICUom", "BinLocn"};

    //private static String Ftp_Server = "192.168.69.21";
    private static String Ftp_Server = "ftp.brunton.co.nz";
    private static String Ftp_Port = "21";
    //private static String Ftp_Username = "stocktake";
    private static String Ftp_Username = "BNZLFTP";
    //private static String Ftp_Password = "stocktake";
    private static String Ftp_Password = "K33pItS3cur3";

    private static String Ftp_DownloadPath = "/Download";
    private static String Ftp_UploadPath = "/Upload";

    public static String get_ApplicationLicence() {
        return Licence;
    }

    public static String get_FtpServer(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String result = settings.getString("Ftp_Server", Ftp_Server);

        return  result;
    }

    public static Integer get_FtpPort(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Integer result = Integer.parseInt(settings.getString("Ftp_Port", Ftp_Port));

        return result;
    }

    public static String get_FtpUsername(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String result = settings.getString("Ftp_Username", Ftp_Username);

        return  result;
    }

    public static String get_FtpPassword(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String result = settings.getString("Ftp_Password", Ftp_Password);

        return  result;
    }

    public static String get_FtpDownloadPath(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String result = settings.getString("Ftp_DownloadPath", Ftp_DownloadPath);

        return  result;
    }

    public static String get_FtpUploadPath(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String result = settings.getString("Ftp_UploadPath", Ftp_UploadPath);

        return  result;
    }
}
