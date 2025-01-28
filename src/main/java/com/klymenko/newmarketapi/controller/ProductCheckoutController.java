package com.klymenko.newmarketapi.controller;

import com.klymenko.newmarketapi.dto.ProductRequest;
import com.klymenko.newmarketapi.dto.StripeResponse;
import com.klymenko.newmarketapi.service.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/checkout")
public class ProductCheckoutController {

    private final StripeService stripeService;

    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping()
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest) {
        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);

        return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
    }
}
