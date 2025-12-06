package com.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        String message = "Remote service error";

        try {
            if (response.body() != null) {
                JsonNode body = mapper.readTree(response.body().asInputStream());
                if (body.has("message")) {
                    message = body.get("message").asText().trim();
                }
            }
        } catch (IOException ignored) {}
        
        String clean = message;
        if (message.contains(":")) {
            clean = message.substring(message.lastIndexOf(":") + 1).trim();
        }

        String lower = message.toLowerCase();

        if (lower.contains("inactive")) {
            return new AccountInactiveException(clean);
        }

        if (lower.contains("insufficient")) {
            return new InsufficientBalanceException(clean);
        }

        if (lower.contains("not found")) {
            return new AccountNotFoundException(clean);
        }

        return new Exception(clean);
    }
}
