package com.example.goodsservice.mapper;

import com.example.goodsservice.dto.BathRequest;
import com.example.goodsservice.dto.Import_Export_Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BathMapper {
    @Mapping(source = "batchName", target = "batchName")
    @Mapping(source = "expiryDate", target = "expiryDate")
    @Mapping(source = "note", target = "note")
    BathRequest toBathRequest(Import_Export_Request request);
}
