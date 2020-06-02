package tfl.api.service;

import com.framework.api.services.BaseService;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import tfl.api.constant.Endpoint;

/**
 * Base Service for TFL specific services.
 */
public class BaseTFLService extends BaseService {

    /**
     * @return a Rest Assured {@link RequestSpecification} with the baseUri
     * (and anything else required by most TFL services).
     */
    @Override
    protected RequestSpecification getRequestSpec() {
        return RestAssured.given().baseUri(Endpoint.BASE_URI.getUrl());
    }

    /**
     * @return a Rest Assured {@link ResponseSpecification} with basic checks
     * (and anything else required by most TFL services).
     */
    @Override
    protected ResponseSpecification getResponseSpec() {
        return RestAssured.expect().response().statusCode(HttpStatus.SC_OK);
    }

}
