package api.tests;

import api.dto.booking.Booking;
import api.dto.booking.BookingID;
import api.dto.booking.search.SearchParamsMapper;
import api.service.booking.BookingService;
import api.service.ping.PingService;
import com.framework.api.tests.BaseAPITest;
import com.framework.common.retry.RetryFlakyTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class SearchBookerTest extends BaseAPITest {

    @BeforeClass
    public void ensure_site_is_up_by_using_ping_service() {
        assertThat(new PingService().ping())
                .isEqualTo("Created");
    }

    // app reset every 10m, so could happen in the middle of this test
    @Test(retryAnalyzer = RetryFlakyTest.class)
    public void search_for_existing_records_by_name() {
        BookingService service = new BookingService();
        BookingID existingID = service.listBookings().get(1);
        Booking booking = service.getBooking(existingID.bookingid);

        List<BookingID> bookingIDs = service.search(
                SearchParamsMapper.namesOfBooking(booking));

        assertThat(bookingIDs).contains(existingID);
    }

}
