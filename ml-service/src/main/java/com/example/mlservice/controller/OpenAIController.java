package com.example.mlservice.controller;
import com.example.mlservice.service.IOpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OpenAIController {

    @Autowired
    private IOpenAIService openAIService;

    @PostMapping("/query")
    public String getChatResponse(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        return openAIService.getChatResponse(prompt);
    }
}
