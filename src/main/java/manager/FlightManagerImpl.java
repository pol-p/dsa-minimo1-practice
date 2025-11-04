package manager;

import dto.MaletaAFacturarVol;
import dto.VolBajarMaletas;
import dto.VolRequest;
import exceptions.AvioNotFoundException;
import exceptions.VolNotFoundException;
import models.Avio;
import models.Maleta;
import models.Vol;

import java.util.*;

import org.apache.log4j.Logger;

public class FlightManagerImpl implements FlightManager {
    private static FlightManager fm;
    private static final Logger LOGGER = Logger.getLogger(FlightManagerImpl.class);
    //BASES DE DATOS
    private Map<Integer, Avio> listAviones;
    private Map<Integer, Vol> listVols;
    private Integer contadorMalet;

    private FlightManagerImpl(){
        this.listAviones = new HashMap<>();
        this.listVols = new HashMap<>();
        this.contadorMalet = 0;
        LOGGER.info("manager.FlightManagerImpl (Singleton) creado e inicializado.");
    }

    public static FlightManager getInstance(){
        if(fm == null){
            LOGGER.info("Creando nueva instancia de manager.FlightManagerImpl");
            fm = new FlightManagerImpl();
        }
        return fm;
    }

    @Override
    public void addAvio(Integer idAvion, String modelAvion, String compañiaAvion) {
        // Log de inicio con parámetros
        LOGGER.info("INICIO addAvio: id=" + idAvion + ", model=" + modelAvion + ", compañia=" + compañiaAvion);

        if(listAviones.containsKey(idAvion)){
            listAviones.replace(idAvion, new Avio(idAvion, modelAvion, compañiaAvion));
            LOGGER.info("Actualizando informacion en el avion " + idAvion);
        } else {
            listAviones.put(idAvion, new Avio(idAvion, modelAvion, compañiaAvion));
            LOGGER.info("Avion " + idAvion + " añadido con exito");
        }

        // Log de fin
        LOGGER.info("FIN addAvio");
    }

    @Override
    public void addVol(VolRequest vr) {
        // Log de inicio con parámetros (toString() del DTO)
        LOGGER.info("INICIO addVol: VolRequest=" + vr.toString()); // (Asegúrate que tu DTO tiene un buen .toString())

        Avio avio = getAvioById(vr.getIdAvio()); // Esto puede lanzar excepción

        Vol vol = listVols.get(vr.getIdVol());

        if (vol == null) {
            vol = new Vol(vr.getIdVol(), avio, vr.getOrigen(), vr.getDesti(), vr.getHoraSortida(), vr.getHoraArribada());
            listVols.put(vr.getIdVol(), vol);
            LOGGER.info("Vol " + vr.getIdVol() + " añadido con exito");
        } else {
            vol.setIdVol(vr.getIdVol());
            vol.setAvio(avio);
            vol.setDesti(vr.getDesti());
            vol.setOrigen(vr.getOrigen());
            vol.setHoraArribada(vr.getHoraArribada());
            vol.setHoraSortida(vr.getHoraSortida());
            LOGGER.info("Cambiado con exito los valores del vol " + vr.getIdVol());
        }

        // Log de fin
        LOGGER.info("FIN addVol");
    }

    @Override
    public void subirMaleta(MaletaAFacturarVol mf) {
        // Log de inicio con parámetros
        LOGGER.info("INICIO subirMaleta: MaletaAFacturarVol=" + mf.toString());

        Vol v = getVolById(mf.getIdVol()); // Esto puede lanzar excepción

        Maleta maletaCrear = new Maleta(this.contadorMalet ++, mf.getIdUse());
        v.subirMaletaALaBodega(maletaCrear);
        LOGGER.info("Maleta " + maletaCrear.getIdMaleta() + " cargada en el Vuelo " + v.getIdVol());

        // Log de fin
        LOGGER.info("FIN subirMaleta");
    }

    @Override
    public List<Maleta> bajarMaletas(VolBajarMaletas vbm) {
        // Log de inicio con parámetros
        LOGGER.info("INICIO bajarMaletas: idVol=" + vbm.getIdVol());

        Vol vol = getVolById(vbm.getIdVol()); // Esto puede lanzar excepción

        List<Maleta> maletasDescargadas = vol.bajarMaletasEnOrdenLIFO();

        LOGGER.info("FIN bajarMaletas: Se han descargado " + maletasDescargadas.size() + " maletas.");
        return maletasDescargadas;
    }

    // (Helpers)

    private Avio getAvioById(Integer idAvio) throws AvioNotFoundException {
        LOGGER.info("INICIO getAvioById: id=" + idAvio);

        Avio a = this.listAviones.get(idAvio);
        if(a == null){
            // Log de error antes de lanzar la excepción
            LOGGER.error("ERROR en getAvioById: Avión no encontrado con ID: " + idAvio);
            throw new AvioNotFoundException(idAvio);
        }

        LOGGER.info("FIN getAvioById: Avión encontrado.");
        return a;
    }

    private Vol getVolById(Integer idVol) throws VolNotFoundException {
        LOGGER.info("INICIO getVolById: id=" + idVol);

        Vol v = this.listVols.get(idVol);
        if (v == null){
            // Log de error antes de lanzar la excepción
            LOGGER.error("ERROR en getVolById: Vuelo no encontrado con ID: " + idVol);
            throw new VolNotFoundException(idVol);
        }

        LOGGER.info("FIN getVolById: Vuelo encontrado.");
        return v;
    }
}