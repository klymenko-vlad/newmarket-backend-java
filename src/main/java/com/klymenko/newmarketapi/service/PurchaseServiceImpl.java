package com.klymenko.newmarketapi.service;

import com.klymenko.newmarketapi.entities.Purchase;
import com.klymenko.newmarketapi.entities.User;
import com.klymenko.newmarketapi.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserServiceImpl userService;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, UserServiceImpl userService) {
        this.purchaseRepository = purchaseRepository;
        this.userService = userService;
    }

    @Override
    public List<Purchase> getAllPurchasesByUserId(Long userId) {
        User user = userService.getUserById(userId);

        return purchaseRepository.getAllPurchase(user.getId());
    }
}
