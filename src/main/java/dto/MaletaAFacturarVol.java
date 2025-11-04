package dto;

public class MaletaAFacturarVol {
    private Integer idVol;
    private Integer idUse;

    public MaletaAFacturarVol() {
    }

    public MaletaAFacturarVol(Integer idVol, Integer idUse) {
        this.idVol = idVol;
        this.idUse = idUse;
    }

    public Integer getIdVol() {
        return idVol;
    }

    public void setIdVol(Integer idVol) {
        this.idVol = idVol;
    }

    public Integer getIdUse() {
        return idUse;
    }

    public void setIdUse(Integer idUse) {
        this.idUse = idUse;
    }
    @Override
    public String toString(){
        return "MaletaFacturada = " + this.idVol + " " + this.idUse;
    }
}
