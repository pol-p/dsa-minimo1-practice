package exceptions;

public class AvioNotFoundException extends RuntimeException{
    public AvioNotFoundException(Integer idAvio){
        super("[!] Avion no encontrado con id --> " + idAvio + " [!]");
    }
}
