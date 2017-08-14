package com.example.stocktake;

/**
 * Created by james.shen on 03/02/2017.
 */

public class UOMCount {
    private String _LocationCode;
    private String _BarCode;
    private String _UOMCode;
    private double _Count;
    private double _UOMMultilplier;

    public void set_LocationCode(String LocationCode) {
        _LocationCode = LocationCode;
    }

    public String get_LocationCode() {
        return _LocationCode;
    }

    public void set_BarCode(String BarCode) {
        _BarCode = BarCode;
    }

    public String get_BarCode() {
        return _BarCode;
    }

    public void set_UOMCode(String UOMCode) {
        _UOMCode = UOMCode;
    }

    public String get_UOMCode() {
        return _UOMCode;
    }

    public void set_Count(Double Count) {
        _Count = Count;
    }

    public double get_Count() {
        return  _Count;
    }

    public  void set_UOMMultilplier(double UOMMultiplier) {
        _UOMMultilplier = UOMMultiplier;
    }

    public double get_UOMMultilplier() {
        return _UOMMultilplier;
    }
}
