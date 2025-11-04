package manager;

import dto.MaletaAFacturarVol;
import dto.VolBajarMaletas;
import dto.VolRequest;
import models.Maleta;

import java.util.List;

public interface FlightManager {
    public void addAvio(Integer idAvion, String modelAvion, String compañiaAvion);
    public void addVol(VolRequest vr);
    public void subirMaleta(MaletaAFacturarVol mf);
    public List<Maleta> bajarMaletas(VolBajarMaletas vbm);
}
