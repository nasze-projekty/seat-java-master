package api.dto.booking.search;

import api.dto.booking.Booking;

import java.util.HashMap;
import java.util.Map;

public class SearchParamsMapper {

    private SearchParamsMapper() {
        // hide constructor of mapper
    }

    public static Map<String, String> namesOfBooking(Booking booking) {
        Map<String, String> myMap = new HashMap<String, String>() {{
            put("firstname", booking.firstname);
            put("lastname", booking.lastname);
        }};
        return myMap;
    }

    public static Map<String, String> datesOfBooking(Booking booking) {

        Map<String, String> myMap = new HashMap<String, String>() {{
            put("checkin", booking.bookingdates.checkin);
            put("checkout", booking.bookingdates.checkout);

        }};
        return myMap;
    }
}
