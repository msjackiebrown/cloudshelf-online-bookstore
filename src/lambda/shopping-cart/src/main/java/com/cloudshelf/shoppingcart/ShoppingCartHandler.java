package com.cloudshelf.shoppingcart;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * CloudShelf Shopping Cart Lambda Handler - ARCHITECTURAL TEMPLATE
 * 
 * Basic Lambda function structure following:
 * - ADR-003: DynamoDB for Shopping Cart Storage
 * - ADR-004: Lambda Serverless Architecture Strategy
 * - ADR-005: Java 21 Programming Language Selection
 * 
 * DEVELOPER NOTE: Implement business logic for shopping cart operations
 */
public class ShoppingCartHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        
        // SOLUTIONS ARCHITECT: Basic template for DevOps Lambda creation
        // DEVELOPER: Replace with full implementation
        
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        
        try {
            // Basic response for architecture validation
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("service", "shopping-cart");
            responseBody.put("status", "template-ready");
            responseBody.put("message", "Solutions Architect template - Developer implementation required");
            responseBody.put("path", request.getPath());
            responseBody.put("method", request.getHttpMethod());
            
            response.setStatusCode(200);
            response.setBody(objectMapper.writeValueAsString(responseBody));
            response.setHeaders(createCorsHeaders());
            
        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            
            response.setStatusCode(500);
            response.setBody("{\"error\":\"Internal server error\"}");
        }
        
        return response;
    }

    /**
     * Standard CORS headers for API Gateway integration
     */
    private Map<String, String> createCorsHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");
        return headers;
    }
}
