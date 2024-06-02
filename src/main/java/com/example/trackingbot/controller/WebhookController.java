package com.example.trackingbot.controller;

import com.example.trackingbot.TrackinfModel;
import com.example.trackingbot.service.WebhookService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api")
public class WebhookController {

    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public WebhookController(WebhookService webhookService, ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
        this.webhookService = webhookService;
    }


    @PostMapping("/webhook")
    public ResponseEntity<Map<String, Object>> handleWebhook(@RequestBody String payload1) {
        try {

            JsonNode jsonNode = objectMapper.readTree(payload1);
            String orderId = null;

            if (jsonNode.has("queryResult") && jsonNode.get("queryResult").has("parameters") && jsonNode.get("queryResult").has("parameters")) {
                orderId = jsonNode.get("queryResult").get("parameters").get("orderId").asText();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("fulfillmentText", "No query text found.");
                return ResponseEntity.ok(response);
            }

            String date = webhookService.getTrackingDate();

            String soundLocation="https://firebasestorage.googleapis.com/v0/b/lead-2e832.appspot.com/o/motrola_tone.mp3?alt=media&token=536abb22-d6e2-4baf-bc67-02d68c8af957";
            System.out.println("TrackingID: " + date);

            Map<String, Object> response = new HashMap<>();
            response.put("fulfillmentText", "Your order id " + orderId + " will be shipped on " + date);  // Simple text response


            // Custom payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("richContent", List.of(
                    List.of(
                            Map.of(
                                    "type", "info",
                                    "title", "Order Shipping Information",
                                    "subtitle", "Your order ID " + orderId + " will be shipped on " + date
                            )
                    )
            ));
            // Fulfillment messages
            Map<String, Object> payloadWrapper = new HashMap<>();
            payloadWrapper.put("payload", payload);
            response.put("fulfillmentMessages", List.of(payloadWrapper));

            return ResponseEntity.ok(response);


        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("fulfillmentText", "An error occurred while processing your request.");
            return ResponseEntity.ok(response);
        }
    }
}


