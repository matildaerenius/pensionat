package se.backend1.pensionat.mapper;

import org.springframework.stereotype.Component;
import se.backend1.pensionat.dto.BookingDto;
import se.backend1.pensionat.dto.DetailedBookingDto;
import se.backend1.pensionat.entity.Booking;

@Component
public class BookingMapper {

    private final CustomerMapper customerMapper;
    private final RoomMapper roomMapper;


    public BookingMapper(CustomerMapper customerMapper, RoomMapper roomMapper) {
        this.customerMapper = customerMapper;
        this.roomMapper = roomMapper;
    }

    //Bygger från Booking TILL DTO
    public BookingDto bookingToBookingDto (Booking b){
        return BookingDto.builder()
                .id(b.getId())
                .checkIn(b.getCheckIn())
                .checkOut(b.getCheckOut())
                .numberOfGuests(b.getNumberOfGuests())
                .customerId(b.getCustomer().getId())
                .roomId(b.getRoom().getId())
                .build();
    }

    public Booking bookingDtoToBooking(BookingDto dto){
     return    Booking.builder()
                .id(dto.getId())
                .checkIn(dto.getCheckIn())
                .checkOut(dto.getCheckOut())
                .numberOfGuests(dto.getNumberOfGuests())
                .build();
        //Kund ID och rum ID finns ej då det inte finns dessa variabler i booking objekt. enbart i dto objekt
    }


    //FÅR UT DETALJERAD MAP - detta kan vara bra för frontend för att få ut info om kunders bokning
    public DetailedBookingDto getDetailedBooking(Booking b) {
        return DetailedBookingDto.builder()
                .customerId(b.getCustomer().getId())
                .name(b.getCustomer().getName())
                .roomId(b.getRoom().getId())
                .roomNumber(b.getRoom().getRoomNumber())
                .roomType(b.getRoom().getRoomType())
                .numberOfGuests(b.getNumberOfGuests())
                .bookingDto(bookingToBookingDto(b))
                .build();
    }
//    public static Booking toEntity(BookingDto dto) {
//        if (dto == null) return null;
//        Booking booking = new Booking();
//        booking.setId(dto.getId());
//        booking.setCheckIn(dto.getCheckIn());
//        booking.setCheckOut(dto.getCheckOut());
//        booking.setNumberOfGuests(dto.getNumberOfGuests());
//        // customer och room sätts i service?
//        return booking;
//    }
//
//    public static BookingDto toDto(Booking booking) {
//        if (booking == null) return null;
//        BookingDto dto = new BookingDto();
//        dto.setId(booking.getId());
//        dto.setCheckIn(booking.getCheckIn());
//        dto.setCheckOut(booking.getCheckOut());
//        dto.setNumberOfGuests(booking.getNumberOfGuests());
//        dto.setCustomerId(booking.getCustomer().getId());
//        dto.setRoomId(booking.getRoom().getId());
//        return dto;
//    }
}
