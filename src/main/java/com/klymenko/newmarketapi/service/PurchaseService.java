package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.entities.Purchase;

import java.util.List;

public interface PurchaseService {
    List<Purchase> getAllPurchasesByUserId(Long userId);

}
