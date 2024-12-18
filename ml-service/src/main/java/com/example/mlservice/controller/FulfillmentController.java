package com.example.mlservice.controller;

import com.example.mlservice.service.impl.DialogflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/dialogflow")
public class FulfillmentController {

    @Autowired
    private DialogflowService dialogflowService;

    @PostMapping("/fulfillment")
    public Map<String, Object> handleFulfillment(@RequestBody Map<String, Object> request) {
        System.out.println("Received request from Dialogflow: " + request);

        String intentName;

        try {
            Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
            Map<String, Object> intent = (Map<String, Object>) queryResult.get("intent");
            intentName = (String) intent.get("displayName");
        } catch (Exception e) {
            System.out.println("Error processing request: " + e.getMessage());
            return Map.of("fulfillmentText", "Đã xảy ra lỗi khi xử lý yêu cầu.");
        }

        String response;

        switch (intentName) {
            case "Kiểm tra hàng tồn":
                response = "Hiện có 78 sản phẩm trong kho.";
                break;
            case "Nhập hàng mới":
                response = "Đã thêm sản phẩm mới vào kho thành công!";
                break;
            default:
                response = "Xin lỗi, tôi không hiểu yêu cầu của bạn.";
        }

        System.out.println("Response to Dialogflow: " + response);
        return Map.of("fulfillmentText", response);
    }
}
