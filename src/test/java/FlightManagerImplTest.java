import dto.MaletaAFacturarVol;
import dto.VolBajarMaletas;
import dto.VolRequest;
import exceptions.AvioNotFoundException;
import exceptions.VolNotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import models.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.List;

public class FlightManagerImplTest {
    private FlightManager fm;
    @Before
    public void setUp() {
        fm = FlightManagerImpl.getInstance();
        fm.addAvio(123, "B-2", "Vueling");
    }

    @After
    public void tearDown() {
        fm = null;
    }

    @Test
    public void testAddAvio() throws Exception {
        fm.addAvio(222, "123-AAA", "AmericVuelo");
        // Acceder por reflexión al campo privado listAviones
        Field field = fm.getClass().getDeclaredField("listAviones");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, Object> listAviones = (Map<Integer, Object>) field.get(fm);

        // Comprobaciones
        Assert.assertTrue("El mapa debe contener la clave 222", listAviones.containsKey(222));
        Assert.assertEquals("El tamaño del mapa debe ser 2 tras añadir otro avión", 2, listAviones.size());
        Assert.assertNotNull("El objeto Avio añadido no debe ser null", listAviones.get(222));
    }
    @Test
    public void testAddVol_CreateAndUpdate() throws Exception {
        // --- 1. PREPARAR ---
        // El setUp() ya ha añadido un Avio con ID 123
        // Creamos una petición para un vuelo nuevo (ID 501)
        VolRequest vr = new VolRequest(501, 123, "BCN", "JFK", "10:00", "18:00");

        // --- 2. ACTUAR (Crear) ---
        fm.addVol(vr);

        // --- 3. COMPROBAR (Crear) ---
        // Usamos reflexión para acceder al mapa privado 'listVols'
        Field field = fm.getClass().getDeclaredField("listVols");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, Vol> listVols = (Map<Integer, Vol>) field.get(fm);

        // Comprobamos que el vuelo 501 se ha añadido
        Assert.assertEquals("El mapa debe tener 1 vuelo", 1, listVols.size());
        Assert.assertTrue("El mapa debe contener el vuelo 501", listVols.containsKey(501));
        Assert.assertEquals("El destino debe ser JFK", "JFK", listVols.get(501).getDesti());

        // --- 4. PREPARAR (Actualizar) ---
        // Creamos una nueva petición CON EL MISMO ID (501) pero otro destino
        VolRequest vrUpdate = new VolRequest(501, 123, "BCN", "MADRID", "11:00", "12:00");

        // --- 5. ACTUAR (Actualizar) ---
        fm.addVol(vrUpdate);

        // --- 6. COMPROBAR (Actualizar) ---
        // El tamaño no debería cambiar, solo el contenido
        Assert.assertEquals("El mapa debe seguir teniendo 1 vuelo", 1, listVols.size());
        Assert.assertEquals("El destino debe ser MADRID", "MADRID", listVols.get(501).getDesti());
        Assert.assertEquals("La hora de salida debe ser 11:00", "11:00", listVols.get(501).getHoraSortida());
    }

    /**
     * Test para el método addVol (Error Path).
     * Prueba que se lanza AvioNotFoundException si el Avio no existe.
     */
    @Test(expected = AvioNotFoundException.class)
    public void testAddVol_AvioNotFound() throws AvioNotFoundException {
        // --- 1. PREPARAR ---
        // Creamos una petición para un vuelo (ID 502) que usa un Avio
        // que NO existe (ID 999).
        VolRequest vrError = new VolRequest(502, 999, "AMS", "LAX", "15:00", "22:00");

        // --- 2. ACTUAR Y COMPROBAR ---
        // que se ha lanzado la excepción 'AvioNotFoundException'.
        // Si no se lanza, el test fallará.
        fm.addVol(vrError);
    }

    @Test
    public void testSubirMaleta() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // El setUp() solo añade un Avión (ID 123).
        // Para este test, primero necesitamos un Vuelo.
        VolRequest vr = new VolRequest(101, 123, "BCN", "JFK", "10:00", "12:00");
        fm.addVol(vr);

        // Ahora creamos la petición para facturar una maleta en el Vuelo 101
        // Asumimos que el DTO 'MaletaAFacturarVol' es (idVol, idUsuario)
        MaletaAFacturarVol mf = new MaletaAFacturarVol(101, 555); // (Vuelo 101, Usuario 555)

        // --- 2. ACT (Actuar) ---
        fm.subirMaleta(mf); // Llamamos al método que queremos probar

        // --- 3. ASSERT (Comprobar) ---
        // Usamos reflexión para acceder al mapa privado 'listVols'
        Field field = fm.getClass().getDeclaredField("listVols");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, Vol> listVols = (Map<Integer, Vol>) field.get(fm);

        // B. Comprobar que la maleta está en la bodega
        Vol volTest = listVols.get(101); // Obtenemos el Vuelo 101
        Assert.assertNotNull("El vuelo no debería ser null", volTest);

        // C. Comprobar la pila (Stack) de la bodega
        Assert.assertEquals("La bodega debe tener 1 maleta", 1, volTest.getBodegaMaletas().size());

        // D. Comprobar la maleta que está en la cima de la pila
        Maleta maletaTest = volTest.getBodegaMaletas().peek();
        Assert.assertNotNull("La maleta no debería ser null", maletaTest);

        // El contadorMalet empieza en 0 y usaste 'contadorMalet++'.
        // Por lo tanto, el primer ID de maleta debe ser 0.
        Assert.assertEquals("El ID de la maleta debe ser 0", Integer.valueOf(0), maletaTest.getIdMaleta());
        Assert.assertEquals("El ID del usuario debe ser 555", Integer.valueOf(555), maletaTest.getIdUsr());
    }


    /**
     * Prueba el "error path" de subirMaleta.
     * Intenta subir una maleta a un Vuelo que NO existe (ID 999).
     * Espera que el método lance 'VolNotFoundException'.
     *
     * NOTA: Este test fallará si tu método 'getVolById' lanza
     * 'AvioNotFoundException' en lugar de 'VolNotFoundException' (el bug que vimos).
     * ¡Asegúrate de haber arreglado ese bug!
     */
    @Test(expected = VolNotFoundException.class)
    public void testSubirMaletaVueloNoEncontrado() {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos una petición para un vuelo que no existe (999)
        MaletaAFacturarVol mfError = new MaletaAFacturarVol(999, 555);

        // --- 2. ACT & ASSERT (Actuar y Comprobar) ---
        // JUNIT 4 comprobará que esta línea lanza la excepción esperada.
        // Si no la lanza, el test fallará.
        fm.subirMaleta(mfError);
    }
    @Test
    public void testBajarMaletasLIFO() throws Exception {
        // --- 1. ARRANGE (Preparar) ---
        // Añadimos un Vuelo (101)
        VolRequest vr = new VolRequest(101, 123, "BCN", "JFK", "10:00", "12:00");
        fm.addVol(vr);

        // Añadimos 3 maletas al Vuelo 101
        // Maleta 0 (ID Usuario 555) -> Va al fondo
        fm.subirMaleta(new MaletaAFacturarVol(101, 555));
        // Maleta 1 (ID Usuario 666) -> Va en medio
        fm.subirMaleta(new MaletaAFacturarVol(101, 666));
        // Maleta 2 (ID Usuario 777) -> Va en la cima (LIFO)
        fm.subirMaleta(new MaletaAFacturarVol(101, 777));

        // Creamos la petición para bajar las maletas del Vuelo 101
        VolBajarMaletas vbm = new VolBajarMaletas(101);

        // --- 2. ACT (Actuar) ---
        // Llamamos al método que queremos probar
        List<Maleta> maletasDescargadas = fm.bajarMaletas(vbm);

        // --- 3. ASSERT (Comprobar) ---
        // A. Comprobar la lista devuelta (orden LIFO)
        Assert.assertNotNull("La lista no debe ser null", maletasDescargadas);
        Assert.assertEquals("La lista debe tener 3 maletas", 3, maletasDescargadas.size());

        // Comprobamos el orden LIFO (Last-In, First-Out)
        //
        Assert.assertEquals("La primera maleta en salir debe ser la ID 2", Integer.valueOf(2), maletasDescargadas.get(0).getIdMaleta());
        Assert.assertEquals("El usuario debe ser 777", Integer.valueOf(777), maletasDescargadas.get(0).getIdUsr());

        Assert.assertEquals("La segunda maleta en salir debe ser la ID 1", Integer.valueOf(1), maletasDescargadas.get(1).getIdMaleta());
        Assert.assertEquals("El usuario debe ser 666", Integer.valueOf(666), maletasDescargadas.get(1).getIdUsr());

        Assert.assertEquals("La tercera maleta en salir debe ser la ID 0", Integer.valueOf(0), maletasDescargadas.get(2).getIdMaleta());
        Assert.assertEquals("El usuario debe ser 555", Integer.valueOf(555), maletasDescargadas.get(2).getIdUsr());

        // B. Comprobar que la bodega del Vuelo (el Stack) se ha vaciado
        // (Usando reflexión como en tus otros tests)
        Field field = fm.getClass().getDeclaredField("listVols");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Integer, Vol> listVols = (Map<Integer, Vol>) field.get(fm);
        Vol volTest = listVols.get(101); // Obtenemos el Vuelo 101

        Assert.assertTrue("La bodega del vuelo debe estar vacía", volTest.getBodegaMaletas().isEmpty());
    }

    /**
     * Prueba el "happy path" de bajarMaletas de un vuelo sin maletas.
     * Espera una lista vacía.
     */
    @Test
    public void testBajarMaletasVueloVacio() {
        // --- 1. ARRANGE (Preparar) ---
        // Añadimos un Vuelo (102) pero NO añadimos maletas
        VolRequest vr = new VolRequest(102, 123, "AMS", "LAX", "10:00", "12:00");
        fm.addVol(vr);

        VolBajarMaletas vbm = new VolBajarMaletas(102);

        // --- 2. ACT (Actuar) ---
        List<Maleta> maletasDescargadas = fm.bajarMaletas(vbm);

        // --- 3. ASSERT (Comprobar) ---
        Assert.assertNotNull("La lista no debe ser null", maletasDescargadas);
        Assert.assertEquals("La lista debe estar vacía", 0, maletasDescargadas.size());
    }

    /**
     * Prueba el "error path" de bajarMaletas.
     * Intenta bajar maletas de un Vuelo que NO existe (ID 999).
     * Espera que el método lance 'VolNotFoundException'.
     */
    @Test(expected = VolNotFoundException.class)
    public void testBajarMaletasVueloNoEncontrado() {
        // --- 1. ARRANGE (Preparar) ---
        // Creamos una petición para un vuelo que no existe (999)
        VolBajarMaletas vbmError = new VolBajarMaletas(999);

        // --- 2. ACT & ASSERT (Actuar y Comprobar) ---
        // JUNIT 4 comprobará que esta línea lanza la excepción esperada.
        fm.bajarMaletas(vbmError);
    }
}