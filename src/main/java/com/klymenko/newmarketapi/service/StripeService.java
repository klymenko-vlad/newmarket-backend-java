package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.dto.stripe.ProductRequest;
import com.klymenko.newmarketapi.dto.stripe.StripeResponse;
import com.klymenko.newmarketapi.entities.Product;
import com.klymenko.newmarketapi.entities.Purchase;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.repository.PurchaseRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StripeService {

    @Value("${STRIPE_API_KEY}")
    private String secretKey;

    @Value("${FIRST_PURCHASE_COUPON}")
    private String firstPurchaseCoupon;

    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final PurchaseRepository purchaseRepository;

    public StripeService(ProductServiceImpl productService, UserServiceImpl userService, PurchaseRepository purchaseRepository) {
        this.productService = productService;
        this.userService = userService;
        this.purchaseRepository = purchaseRepository;
    }

    public StripeResponse checkoutProducts(ProductRequest productRequest) {
        Stripe.apiKey = secretKey;

        String uriString = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        User user = userService.getLoggedInUser();

        Product product = productService.getProductById(productRequest.getProductId());

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(product.getTitle())
                .setDescription(product.getDescription() != null ? product.getDescription() : "No description provided")
                .addImage(product.getMainPictureUrl())
                .build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("EUR")
                .setUnitAmount(productRequest.getAmount())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(productRequest.getQuantity())
                .setPriceData(priceData)
                .build();

        SessionCreateParams.ShippingAddressCollection shippingAddressCollection =
                SessionCreateParams.ShippingAddressCollection.builder()
                        .addAllAllowedCountry(List.of(SessionCreateParams.ShippingAddressCollection.AllowedCountry.SK, SessionCreateParams.ShippingAddressCollection.AllowedCountry.US, SessionCreateParams.ShippingAddressCollection.AllowedCountry.CZ, SessionCreateParams.ShippingAddressCollection.AllowedCountry.PL))
                        .build();

        SessionCreateParams.AutomaticTax automaticTax = SessionCreateParams.AutomaticTax.builder().setEnabled(true).build();

        SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(uriString + "/success")
                .setCancelUrl(uriString + "/cancel")
                .addLineItem(lineItem)
                .addDiscount(SessionCreateParams.Discount.builder().setCoupon(firstPurchaseCoupon).build())
                .setShippingAddressCollection(shippingAddressCollection)
                .setAutomaticTax(automaticTax)
                .putMetadata("product_id", product.getId())
                .putMetadata("email", (user.getEmail()))
                .addAllPaymentMethodType(List.of(SessionCreateParams.PaymentMethodType.CARD, SessionCreateParams.PaymentMethodType.IDEAL, SessionCreateParams.PaymentMethodType.PAYPAL))
                .build();

        Session session;

        try {
            session = Session.create(params);
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return StripeResponse.builder()
                .status("SUCCESS")
                .message("Payment session created")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }

    public void createPurchase(String productId, String userEmail, BigDecimal amount) {
        System.out.println("Creating purchase:");
        System.out.println("Product ID: " + productId);
        System.out.println("User Email: " + userEmail);
        System.out.println("Amount: " + amount);
        try {
            User user = userService.getUserByEmail(userEmail);
            Product product = productService.getProductById(productId);
            Purchase purchase = Purchase.builder()
                    .amount(amount)
                    .user(user)
                    .product(product)
                    .build();
            purchaseRepository.save(purchase);
            System.out.println("Purchase saved successfully");
        } catch (Exception e) {
            System.err.println("Error creating purchase: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create purchase", e);
        }
    }
}
