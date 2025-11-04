package services;

import dto.VolRequest;
import exceptions.AvioNotFoundException;
import exceptions.VolNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.swagger.annotations.Api;
import manager.FlightManager;
import manager.FlightManagerImpl;
import models.Avio;

@Api(value = "/flight", description = "Endpoint to Flight Services")
@Path("/flight")
public class FlightManagerService {
    private FlightManager fm;

    public FlightManagerService(){
        this.fm = FlightManagerImpl.getInstance();
    }

    @POST // <-- JERSEY: Usamos POST para CREAR un recurso
    @Path("/avions") // <-- JERSEY: La URL es el recurso "aviones"
    @ApiOperation(value = "Añadir o modificar un avión") // <-- SWAGGER
    @ApiResponses(value = { // <-- SWAGGER
            @ApiResponse(code = 201, message = "Avión añadido/modificado", response = Avio.class)
    })
    @Consumes(MediaType.APPLICATION_JSON) // <-- JERSEY: Le dice que espere un JSON
    @Produces(MediaType.APPLICATION_JSON) // <-- JERSEY: Devuelve un JSON
    public Response addAvio(Avio avio) { // <-- ¡PARÁMETRO CLAVE!
        fm.addAvio(avio.getIdAvio(), avio.getModel(), avio.getCompañia());
        return Response.status(201).entity(avio).build();
    }

    @POST // <-- JERSEY: Usamos POST para CREAR un recurso
    @Path("/vols") // <-- JERSEY: La URL es el recurso "aviones"
    @ApiOperation(value = "Añadir o modificar un vol") // <-- SWAGGER
    @ApiResponses(value = { // <-- SWAGGER
            @ApiResponse(code = 201, message = "Vol añadido/modificado", response = VolRequest.class),
            @ApiResponse(code = 401, message = "Avion no vivo", response = String.class)

    })
    @Consumes(MediaType.APPLICATION_JSON) // <-- JERSEY: Le dice que espere un JSON
    @Produces(MediaType.APPLICATION_JSON) // <-- JERSEY: Devuelve un JSON
    public Response addVol(VolRequest vr) { // <-- ¡PARÁMETRO CLAVE!
        try{
            fm.addVol(vr);
            return Response.status(201).entity(vr).build();
        }catch (AvioNotFoundException e){
           return Response.status(404).entity("Error " + e).build();
        }
    }

}
