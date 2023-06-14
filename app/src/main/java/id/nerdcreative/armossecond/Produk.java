package id.nerdcreative.armossecond;

public class Produk {
    String judul, harga, stok, kategori, deskripsi, foto,idProduk, key;

    public Produk() {
    }

    public Produk(String judul, String harga, String stok, String kategori,
                  String deskripsi, String foto, String idProduk) {
        this.judul = judul;
        this.harga = harga;
        this.stok = stok;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.foto = foto;
        this.idProduk = idProduk;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(String idProduk) {
        this.idProduk = idProduk;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
