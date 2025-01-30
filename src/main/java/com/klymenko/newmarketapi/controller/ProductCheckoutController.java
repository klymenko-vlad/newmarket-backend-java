package com.klymenko.newmarketapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klymenko.newmarketapi.dto.stripe.ProductRequest;
import com.klymenko.newmarketapi.dto.stripe.StripeResponse;
import com.klymenko.newmarketapi.service.StripeService;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping()
public class ProductCheckoutController {

    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String webhookSecret;

    private final StripeService stripeService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/product/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest) {
        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);

        return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

            if (deserializer.getObject().isPresent()) {
                Session session = (Session) deserializer.getObject().get();
                processSession(session);
            } else {
                JsonNode dataNode = objectMapper.readTree(deserializer.getRawJson());
                processRawData(dataNode);
            }

            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    private void processSession(Session session) {
        Map<String, String> metadata = session.getMetadata();
        String productId = metadata.get("product_id");
        String userEmail = metadata.get("email");
        String paymentStatus = session.getPaymentStatus();

        if ("paid".equals(paymentStatus)) {
            BigDecimal amount = BigDecimal.valueOf(session.getAmountTotal())
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            stripeService.createPurchase(productId, userEmail, amount);
        }
    }

    private void processRawData(JsonNode dataNode) {
        JsonNode metadataNode = dataNode.path("metadata");
        String productId = metadataNode.path("product_id").asText();
        String userEmail = metadataNode.path("email").asText();
        String paymentStatus = dataNode.path("payment_status").asText();
        long amountTotal = dataNode.path("amount_total").asLong();

        if ("paid".equals(paymentStatus)) {
            BigDecimal amount = BigDecimal.valueOf(amountTotal)
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            stripeService.createPurchase(productId, userEmail, amount);
        }
    }
}
