package restfulbooker.api.dto.booking;


import com.framework.api.dto.AbstractDTO;

public class BookingID extends AbstractDTO<BookingID> {

    public int bookingid;

    public static BookingID of(int bookingID) {
        BookingID id = new BookingID();
        id.bookingid = bookingID;
        return id;
    }
}
