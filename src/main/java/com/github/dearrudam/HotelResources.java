package com.github.dearrudam;

import jakarta.data.page.PageRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.jnosql.mapping.Database;
import org.eclipse.jnosql.mapping.DatabaseType;

@Path("/hotels")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HotelResources {

    private final Hotel hotel;

    public HotelResources(
            @Database(DatabaseType.DOCUMENT)
            Hotel hotel
    ) {
        this.hotel = hotel;
    }

    @GET
    public Response list(@QueryParam("page")
                  @DefaultValue("1") int page,
                  @QueryParam("size")
                  @DefaultValue("10")
                  int size) {
        return Response.ok(
                hotel.getRooms(PageRequest.ofPage(page).size(size)))
                .build();
    }

    @PUT
    public Response checkIn(Room room) {
        return Response.ok(hotel.checkIn(room)).build();
    }

    @Path("/{number}")
    @DELETE
    public void checkOut(@PathParam("number") String number) {
        hotel.checkOut(
                hotel.findById(number)
                        .orElseThrow(()->new WebApplicationException(Response.Status.NOT_FOUND))
        );
    }

}
