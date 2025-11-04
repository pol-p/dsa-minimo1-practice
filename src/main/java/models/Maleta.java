package models;

public class Maleta {
    private Integer idMaleta;
    private Integer idUsr;

    public Maleta() {
    }

    public Maleta(Integer idMaleta, Integer idUsr) {
        this.idMaleta = idMaleta;
        this.idUsr = idUsr;
    }

    public Integer getIdMaleta() {
        return idMaleta;
    }

    public void setIdMaleta(Integer idMaleta) {
        this.idMaleta = idMaleta;
    }

    public Integer getIdUsr() {
        return idUsr;
    }

    public void setIdUsr(Integer idUsr) {
        this.idUsr = idUsr;
    }
}
