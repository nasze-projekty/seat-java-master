package tfl.api.service.journeyplanner;

import io.restassured.RestAssured;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import tfl.api.dto.journeyplanner.DisambiguationResult;
import tfl.api.service.BaseTFLService;

import static io.restassured.RestAssured.get;
import static tfl.api.constant.Endpoint.JOURNEY_PLANNER;

public class DisambiguationService extends BaseTFLService {

    public DisambiguationResult getDisambiguationResult(String from, String to) {
        return get(JOURNEY_PLANNER.getUrl(from, to))
                .as(DisambiguationResult.class);
    }

    /**
     * Overrides {@link BaseTFLService#getResponseSpec()} for this service,
     * which has a different expectation.
     */
    @Override
    protected ResponseSpecification getResponseSpec() {
        return RestAssured
                .expect()
                .statusCode(HttpStatus.SC_MULTIPLE_CHOICES);
    }

}
