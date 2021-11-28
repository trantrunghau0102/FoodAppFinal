package com.example.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MenuItem implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

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
    @SerializedName("favorite")
    @Expose
    private String favorite;
    @SerializedName("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MenuItem() {
    }

    public MenuItem(int id, String tenMon, String linkAnh, String moTa, String nguyenLieu, String soChe, String cachNau) {
        this.id = id;
        this.tenMon = tenMon;
        this.linkAnh = linkAnh;
        this.moTa = moTa;
        this.nguyenLieu = nguyenLieu;
        this.soChe = soChe;
        this.cachNau = cachNau;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
