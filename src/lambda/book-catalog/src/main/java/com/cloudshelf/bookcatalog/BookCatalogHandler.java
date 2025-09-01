package com.cloudshelf.bookcatalog;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * SOLUTIONS ARCHITECT TEMPLATE - Minimal Lambda Handler for JAR Creation
 * 
 * This is a basic handler that can compile into a deployable JAR.
 * DEVELOPER TEAM: Implement full business logic based on architecture docs.
 */
public class BookCatalogHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        
        // SOLUTIONS ARCHITECT: Basic template for DevOps Lambda creation
        // DEVELOPER: Replace with full implementation
        
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        
        try {
            // Basic response for architecture validation
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("service", "book-catalog");
            responseBody.put("status", "template-ready");
            responseBody.put("message", "Solutions Architect template - Developer implementation required");
            responseBody.put("path", request.getPath());
            responseBody.put("method", request.getHttpMethod());
            
            response.setStatusCode(200);
            response.setBody(objectMapper.writeValueAsString(responseBody));
            
            // Basic CORS headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Access-Control-Allow-Origin", "*");
            response.setHeaders(headers);
            
        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            
            response.setStatusCode(500);
            response.setBody("{\"error\":\"Internal server error\"}");
        }
        
        return response;
    }
}
