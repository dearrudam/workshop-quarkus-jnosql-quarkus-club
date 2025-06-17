package org.acme;

import jakarta.data.page.PageRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/hotel/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HotelResource {

    @Inject
    Hotel hotel;

    @PUT
    public Room checkIn(Room room) {
        return hotel.checkIn(room);
    }

    @DELETE
    @Path("/{number}")
    public void checkOut(@PathParam("number") String number) {
        hotel.checkOut(
                hotel.findById(number)
                        .orElseThrow(() -> new NotFoundException("Room not found"))
        );
    }

    @GET
    public List<Room> getReservedRooms(@QueryParam("page")
                               @DefaultValue("1")
                               int page,
                               @QueryParam("size")
                               @DefaultValue("10")
                               int size) {

        return hotel.getReservedRooms(PageRequest.ofPage(page).size(size));
    }

    @GET
    @Path("/by-guest-document/{document}")
    public List<Room> getReservedRoomsByGuestDocument(
            @PathParam("document")
            String document,
            @QueryParam("page")
            @DefaultValue("1")
            int page,
            @QueryParam("size")
            @DefaultValue("10") int size) {
        return hotel.getReservedRoomsByGuestDocument(document, PageRequest.ofPage(page).size(size));
    }
}