package com.example.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YeuThichItem {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("id_menu")
    @Expose
    private int idMenu;
    @SerializedName("TenMon")
    @Expose
    private String tenMon;
    @SerializedName("LinkAnh")
    @Expose
    private String linkAnh;
    @SerializedName("MoTa")
    @Expose
    private String moTa;
    @SerializedName("NguyenLieu")
    @Expose
    private String nguyenLieu;
    @SerializedName("SoChe")
    @Expose
    private String soChe;
    @SerializedName("CachNau")
    @Expose
    private String cachNau;
    @SerializedName("id_user")
    @Expose
    private int idUser;
    @SerializedName("faverite")
    @Expose
    private boolean faverite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNguyenLieu() {
        return nguyenLieu;
    }

    public void setNguyenLieu(String nguyenLieu) {
        this.nguyenLieu = nguyenLieu;
    }

    public String getSoChe() {
        return soChe;
    }

    public void setSoChe(String soChe) {
        this.soChe = soChe;
    }

    public String getCachNau() {
        return cachNau;
    }

    public void setCachNau(String cachNau) {
        this.cachNau = cachNau;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public boolean isFaverite() {
        return faverite;
    }

    public void setFaverite(boolean faverite) {
        this.faverite = faverite;
    }
}
