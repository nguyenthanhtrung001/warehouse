package com.example.productservice.service.impl;

import com.example.productservice.entity.Brand;
import com.example.productservice.entity.Price;
import com.example.productservice.repository.PriceRepository;
import com.example.productservice.service.IBrandService;
import com.example.productservice.service.IPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class implPriceService implements IPriceService {
    @Autowired
    private PriceRepository priceRepository;

    @Override
    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    @Override
    public Price getPriceById(Long id) {
        return priceRepository.findById(id).orElse(null);
    }

    @Override
    public Price createPrice(Price price) {
        return priceRepository.save(price);
    }

    @Override
    public boolean updatePrice(Long id, Price price) {
        Optional<Price> priceOption= priceRepository.findById(id);
        if(priceOption.isPresent()){
            Price priceUpdate= priceOption.get();
            priceUpdate.setPrice(price.getPrice());
            priceUpdate.setEffectiveDate(LocalDateTime.now());
            priceRepository.save(priceUpdate);
            return true;
        }else return false;
    }

    @Override
    public boolean deletePrice(Long id) {
        try {
            priceRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
