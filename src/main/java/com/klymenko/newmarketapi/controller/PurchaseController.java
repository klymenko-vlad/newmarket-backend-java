package com.klymenko.newmarketapi.controller;

import com.klymenko.newmarketapi.entities.Purchase;
import com.klymenko.newmarketapi.service.PurchaseServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseServiceImpl purchaseService;

    public PurchaseController(PurchaseServiceImpl purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/{userId}")
    public List<Purchase> getPurchasesByUserId(@PathVariable Long userId) {
        return purchaseService.getAllPurchasesByUserId(userId);
    }
}
