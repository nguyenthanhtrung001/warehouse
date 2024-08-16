package com.example.orderservice.security;

import com.example.orderservice.dto.response.OrderQuantity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;


public class EncoderDecoder {

    public static String encodeToJsonBase64(List<OrderQuantity> orderQuantityList) {
        try {
            // Chuyển đổi danh sách thành chuỗi JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(orderQuantityList);

            // Mã hóa chuỗi JSON thành Base64
            String encodedString = Base64.getEncoder().encodeToString(jsonString.getBytes());

            return encodedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<OrderQuantity> decodeFromJsonBase64(String encodedString) {
        try {
            // Giải mã chuỗi Base64 thành chuỗi JSON
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            String jsonString = new String(decodedBytes);

            // Chuyển đổi chuỗi JSON thành danh sách các đối tượng OrderQuantity
            ObjectMapper objectMapper = new ObjectMapper();
            List<OrderQuantity> orderQuantityList = objectMapper.readValue(jsonString, new TypeReference<List<OrderQuantity>>() {});

            return orderQuantityList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
