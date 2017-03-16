package id.kost.plus.setget;


/**
 * Created by iqbalhood on 29/10/16.
 */

public class Kost {

    private String id;
    private String nama;
    private String gambar;
    private String alamat;
    private String keterangan;
    private String harga;
    private String latitude;
    private String logitude;

    public Kost(){
        // TODO Auto-generated constructor stub
    }


    public Kost(String id, String nama, String gambar, String harga, String alamat, String keterangan, String latitude, String logitude) {
        super();
        this.id             = id;
        this.nama           = nama;
        this.gambar         = gambar;
        this.keterangan     = keterangan;
        this.alamat         = alamat;
        this.harga          = harga;
        this.latitude       = latitude;
        this.logitude       = logitude;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }


    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String logo) {
        this.keterangan = logo;
    }



    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }



    public String getLogitude() {
        return logitude;
    }

    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }




    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
















}