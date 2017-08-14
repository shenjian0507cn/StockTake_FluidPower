package com.example.stocktake;

/**
 * Created by shenj on 10/01/2017.
 */

public class StocktakeBasicLine {

    Long _id;
    String _LocationCode;
    String _BarCode;
    int _Quantity;

    public StocktakeBasicLine() {

    }

    public StocktakeBasicLine(Long id, String LocationCode, String BarCode, int Quantity) {
        this._id = id;
        this._LocationCode = LocationCode;
        this._BarCode = BarCode;
        this._Quantity = Quantity;
    }

    public StocktakeBasicLine(String LocationCode, String BarCode, int Quantity) {
        this._LocationCode = LocationCode;
        this._BarCode = BarCode;
        this._Quantity = Quantity;
    }

    public Long getID() {
        return this._id;
    }

    public void setID(Long id) {
        this._id = id;
    }

    public String getLocationCode() {
        return this._LocationCode;
    }

    public void setLocationCode(String LocationCode) {
        this._LocationCode = LocationCode;
    }

    public String getBarCode() {
        return this._BarCode;
    }

    public void setBarCode(String BarCode) {
        this._BarCode = BarCode;
    }

    public int getQuantity() {
        return this._Quantity;
    }

    public void setQuantity(int Quantity) {
        this._Quantity = Quantity;
    }
}
