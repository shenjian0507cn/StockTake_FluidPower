package com.example.stocktake;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by james.shen on 03/02/2017.
 */

public class ICProduct {
    private String _BarCode;
    private String _ProductCode;
    private String _Description;
    private String _UOMCode;
    private Double _UOMMultiplier;
    private Double _QuantityInStock;
    private Double _Counted;

    private List<UOMCount> _uomCountList;

    public void set_BarCode(String BarCode) {
        _BarCode = BarCode;
    }

    public String get_BarCode() {
        return  _BarCode;
    }

    public void set_ProductCode(String ProductCode) {
        _ProductCode = ProductCode;
    }

    public String get_ProductCode() {
        return _ProductCode;
    }

    public void set_Description(String Description) {
        _Description = Description;
    }

    public String get_Description() {
        return _Description;
    }

    public void set_UOMCode(String UOMCode) {
        _UOMCode = UOMCode;
    }

    public String get_UOMCode() {
        return _UOMCode;
    }

    public void set_UOMMultiplier(Double UOMMultiplier) {
        _UOMMultiplier = UOMMultiplier;
    }

    public Double get_UOMMultiplier() {
        return _UOMMultiplier;
    }

    public void set_QuantityInStock(Double QuantityInStock) {
        _QuantityInStock = QuantityInStock;
    }

    public Double get_QuantityInStock() {
        return _QuantityInStock;
    }

    public void set_Counted(Double counted) {_Counted = counted;}

    public Double get_Counted() {return _Counted;};

    public void set_UomCountList(List<UOMCount> uomCountList) {
        _uomCountList = uomCountList;
    }

    public List<UOMCount> get_uomCountList() {
        return _uomCountList;
    }
}
