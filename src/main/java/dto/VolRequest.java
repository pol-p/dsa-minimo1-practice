package dto;

import models.Avio;

public class VolRequest {
    private Integer idVol;
    private Integer idAvio;
    private String origen;
    private String desti;
    private String horaSortida;
    private String horaArribada;

    public VolRequest() {
    }

    public VolRequest(Integer idVol, Integer idAvio, String origen, String desti, String horaSortida, String horaArribada) {
        this.idVol = idVol;
        this.idAvio = idAvio;
        this.origen = origen;
        this.desti = desti;
        this.horaSortida = horaSortida;
        this.horaArribada = horaArribada;
    }

    public Integer getIdVol() {
        return idVol;
    }

    public void setIdVol(Integer idVol) {
        this.idVol = idVol;
    }

    public Integer getIdAvio() {
        return idAvio;
    }

    public void setIdAvio(Integer idAvio) {
        this.idAvio = idAvio;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDesti() {
        return desti;
    }

    public void setDesti(String desti) {
        this.desti = desti;
    }

    public String getHoraSortida() {
        return horaSortida;
    }

    public void setHoraSortida(String horaSortida) {
        this.horaSortida = horaSortida;
    }

    public String getHoraArribada() {
        return horaArribada;
    }

    public void setHoraArribada(String horaArribada) {
        this.horaArribada = horaArribada;
    }
}
