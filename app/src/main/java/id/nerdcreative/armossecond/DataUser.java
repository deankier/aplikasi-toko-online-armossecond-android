package id.nerdcreative.armossecond;

public class DataUser {
    String dataNama, dataUsername, dataEmail, dataPhone, dataAlamat, dataFoto;

    public DataUser(String dataNama, String dataUsername, String dataEmail, String dataPhone, String dataAlamat, String dataFoto) {
        this.dataNama = dataNama;
        this.dataUsername = dataUsername;
        this.dataEmail = dataEmail;
        this.dataPhone = dataPhone;
        this.dataAlamat = dataAlamat;
        this.dataFoto = dataFoto;
    }

    public String getDataNama() {
        return dataNama;
    }

    public void setDataNama(String dataNama) {
        this.dataNama = dataNama;
    }

    public String getDataUsername() {
        return dataUsername;
    }

    public void setDataUsername(String dataUsername) {
        this.dataUsername = dataUsername;
    }

    public String getDataEmail() {
        return dataEmail;
    }

    public void setDataEmail(String dataEmail) {
        this.dataEmail = dataEmail;
    }

    public String getDataPhone() {
        return dataPhone;
    }

    public void setDataPhone(String dataPhone) {
        this.dataPhone = dataPhone;
    }

    public String getDataAlamat() {
        return dataAlamat;
    }

    public void setDataAlamat(String dataAlamat) {
        this.dataAlamat = dataAlamat;
    }

    public String getDataFoto() {
        return dataFoto;
    }

    public void setDataFoto(String dataFoto) {
        this.dataFoto = dataFoto;
    }
}
