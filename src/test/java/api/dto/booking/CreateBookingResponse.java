package api.dto.booking;

import com.framework.api.dto.AbstractDTO;

public class CreateBookingResponse extends AbstractDTO<CreateBookingResponse> {
    public Booking booking;
    public int bookingid;
}
