package exceptions;

public class VolNotFoundException extends RuntimeException {
    public VolNotFoundException(Integer idVol) {
        super("El Vol con id --> " + idVol + "No existe" );
    }
}
