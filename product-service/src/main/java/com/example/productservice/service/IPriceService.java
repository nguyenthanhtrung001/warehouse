package com.example.productservice.service;

import com.example.productservice.entity.Price;

import java.util.List;

public interface IPriceService {

    List<Price> getAllPrices();
    Price getPriceById(Long id);
    Price createPrice(Price price);
    boolean updatePrice(Long id, Price price);
    boolean deletePrice(Long id);
}