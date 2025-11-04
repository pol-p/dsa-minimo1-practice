package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Vol {
    private Integer idVol;
    private Avio avio;
    private String origen;
    private String desti;
    private String horaSortida;
    private String horaArribada;
    private Stack<Maleta> bodegaMaletas;

    public Vol() {
    }

    public Vol(Integer idVol, Avio avio, String origen, String desti, String horaSortida, String horaArribada) {
        this.idVol = idVol;
        this.avio = avio;
        this.origen = origen;
        this.desti = desti;
        this.horaSortida = horaSortida;
        this.horaArribada = horaArribada;
        this.bodegaMaletas = new Stack<>();
    }

    public Integer getIdVol() {
        return idVol;
    }

    public void setIdVol(Integer idVol) {
        this.idVol = idVol;
    }

    public Avio getAvio() {
        return avio;
    }

    public void setAvio(Avio avio) {
        this.avio = avio;
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

    public Stack<Maleta> getBodegaMaletas() {
        return bodegaMaletas;
    }

    public void setBodegaMaletas(Stack<Maleta> bodegaMaletas) {
        this.bodegaMaletas = bodegaMaletas;
    }

    public void subirMaletaALaBodega(Maleta m){
        this.bodegaMaletas.push(m);
    }
    public List<Maleta> bajarMaletasEnOrdenLIFO(){
        List<Maleta> lista = new ArrayList<Maleta>();
        while(!this.bodegaMaletas.isEmpty()){
            lista.add(bodegaMaletas.pop());
        }

        return  lista;
    }
}
