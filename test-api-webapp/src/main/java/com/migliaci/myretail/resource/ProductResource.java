package com.migliaci.myretail.resource;

import com.migliaci.myretail.request.ProductRequest;
import com.migliaci.myretail.response.ProductResponse;
import com.migliaci.myretail.service.ProductService;
import com.migliaci.myretail.util.CurrencyUtil;
import com.migliaci.myretail.util.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * Resource containing logic for GET and PUT methods for pricing information
 * from myRetail services. Request-level validation is performed first.
 *
 * @Author migliaci
 */
@RestController
@RequestMapping(path = "/myretail/v1/products")
@Api(value = "/myretail/v1/products", tags = "/myretail", description = "MyRetail API")
@Cacheable("false")
public class ProductResource {

    @Autowired
    ProductService productApiService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Returns product information for a given ID", notes = "Returns product information for a given ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message="Bad Request"),
            @ApiResponse(code = 503, message = "Service Unavailable"), @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<ProductResponse> getProductNameAndPrice(@PathVariable String id) {

        //make sure id value conforms to expected parameters, return immediately if not.
        BigInteger idValue = ValidationUtil.validateAndReturnPositiveBigInteger(id, "id");
        ProductResponse response = productApiService.getProductNameAndPrice(idValue);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(value = "Updates pricing information for a given ID", notes = "Returns product information for a given ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    public ResponseEntity<String> putProductPrice(@PathVariable String id, @RequestBody ProductRequest request) {

        //make sure id and pricing information conform to expected values, return immediately if not.
        //some additional validation could be added here at object-level to provide more feedback to user.
        ValidationUtil.validatePositiveBigInteger(id, "id");
        ValidationUtil.validateNotNull(request.getCurrentPrice(), "pricingInformation");
        ValidationUtil.validatePositive(request.getCurrentPrice().getValue(), "value");

        //if user provided a strange decimal, do any currencyCode-specific math via CurrencyUtil.
        request.getCurrentPrice().setValue(CurrencyUtil.transformPrice(request.getCurrentPrice().getValue(), request.getCurrentPrice().getCurrencyCode()));

        boolean updated = productApiService.updateProductPrice(id, request);
        if(updated) {
            return ResponseEntity.status(200).body("Price modification completed successfully.");
        } else {
            return ResponseEntity.status(400).body("Price modification did not complete successfully.");
        }
    }
}
