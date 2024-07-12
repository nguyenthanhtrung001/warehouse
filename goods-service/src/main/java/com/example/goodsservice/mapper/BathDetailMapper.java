package com.example.goodsservice.mapper;

import com.example.goodsservice.dto.BathDetailRequest;

import com.example.goodsservice.dto.Import_Export_DetailRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface BathDetailMapper {
    @Mapping(source = "product_Id", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    BathDetailRequest toBathDetailRequest (Import_Export_DetailRequest request);
}
