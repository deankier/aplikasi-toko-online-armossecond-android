package id.nerdcreative.armossecond;

public class DataUser {
    String dataNama, dataUsername, dataEmail, dataPhone;

    public DataUser(String dataNama, String dataUsername, String dataEmail, String dataPhone) {
        this.dataNama = dataNama;
        this.dataUsername = dataUsername;
        this.dataEmail = dataEmail;
        this.dataPhone = dataPhone;
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

}
