package tfl.api.test;

import com.framework.api.tests.BaseAPITest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tfl.api.dto.bikepoints.BikePoints;
import tfl.api.dto.common.Place;
import tfl.api.service.bikepoints.BikePointService;
import tfl.api.service.bikepoints.BikePointsParamsBuilder;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

@Test
public class BikePointsTest extends BaseAPITest {

    private BikePoints bikePoints;

    @BeforeClass
    public void cache_get_bike_points_results() {
        bikePoints = new BikePointService().getBikePoints();
    }

    public void there_are_a_lot_of_bike_points() {

        assertThat(bikePoints.getAllNames().size())
                .isAtLeast(700);
    }

    public void known_name_exists_in_all_bike_points() {

        assertThat(bikePoints.getAllNames())
                .contains("Evesham Street, Avondale");
    }

    public void given_lat_long_of_point_point_appears_in_lat_long_search() {

        // Get random bike point
        Place randomBP = bikePoints.getRandomBikePoint();

        // Search for lat long of said bike point with 200m radius
        Map<String, String> params = new BikePointsParamsBuilder()
                .latitude(randomBP.lat)
                .longditude(randomBP.lon)
                .radiusInMeters(200)
                .build();

        BikePoints searchResults = new BikePointService().searchBikePoints(params);

        // Then said bike point is part of result set
        assertThat(searchResults.getAllNames())
                .contains(randomBP.commonName);
    }
}
