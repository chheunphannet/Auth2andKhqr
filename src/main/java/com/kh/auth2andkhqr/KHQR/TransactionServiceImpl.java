package com.kh.auth2andkhqr.KHQR;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final RestTemplate restTemplate;
    
    @Value("${bakong.api.base-url}")
    private String baseUrl;
    @Value("${bakong.api.token}")
    private String authorization;

    @Override
    public CheckTransactionResponse checkTransactionByMd5(
    		CheckTransactionByMd5Request request
//    		,String authorization
    		) {
        try {
            String url = baseUrl + "/v1/check_transaction_by_md5";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization","Bearer " + authorization);
            
            HttpEntity<CheckTransactionByMd5Request> entity = new HttpEntity<>(request, headers);
            
            ResponseEntity<CheckTransactionResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                CheckTransactionResponse.class
            );
            
            CheckTransactionResponse body = response.getBody();
            log.info("Transaction check response - Code: {}, ErrorCode: {}, Message: {}", 
                    body.getResponseCode(), body.getErrorCode(), body.getResponseMessage());
            
            return body;
            
        } catch (HttpClientErrorException e) {
            log.error("Client error - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw handleHttpClientError(e);
            
        } catch (HttpServerErrorException e) {
            log.error("Server error - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BakongApiException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ErrorCode.CANNOT_CONNECT_SERVER.getCode(),
                "Internal server error occurred"
            );
            
        } catch (ResourceAccessException e) {
            log.error("Connection error: {}", e.getMessage());
            throw new BakongApiException(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ErrorCode.CANNOT_CONNECT_SERVER.getCode(),
                ErrorCode.CANNOT_CONNECT_SERVER.getDescription()
            );
            
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            throw new BakongApiException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ErrorCode.CANNOT_CONNECT_SERVER.getCode(),
                "Unexpected error occurred"
            );
        }
    }
    
    private BakongApiException handleHttpClientError(HttpClientErrorException e) {
        HttpStatus status = (HttpStatus) e.getStatusCode();
        
        // Try to parse error response
        try {
            CheckTransactionResponse errorResponse = e.getResponseBodyAs(CheckTransactionResponse.class);
            if (errorResponse != null && errorResponse.getErrorCode() != null) {
                return new BakongApiException(
                    status.value(),
                    errorResponse.getErrorCode(),
                    errorResponse.getResponseMessage()
                );
            }
        } catch (Exception parseEx) {
            log.warn("Could not parse error response body");
        }
        
        // Handle by HTTP status code
        return switch (status) {
            case BAD_REQUEST -> new BakongApiException(
                HttpStatus.BAD_REQUEST.value(),
                ErrorCode.MISSING_REQUIRED_FIELDS.getCode(),
                "Bad request"
            );
            case UNAUTHORIZED -> new BakongApiException(
                HttpStatus.UNAUTHORIZED.value(),
                ErrorCode.UNAUTHORIZED.getCode(),
                ErrorCode.UNAUTHORIZED.getDescription()
            );
            case FORBIDDEN -> new BakongApiException(
                HttpStatus.FORBIDDEN.value(),
                ErrorCode.UNAUTHORIZED.getCode(),
                "Forbidden"
            );
            case NOT_FOUND -> new BakongApiException(
                HttpStatus.NOT_FOUND.value(),
                ErrorCode.TRANSACTION_NOT_FOUND.getCode(),
                ErrorCode.TRANSACTION_NOT_FOUND.getDescription()
            );
            case TOO_MANY_REQUESTS -> new BakongApiException(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                null,
                "Too many requests"
            );
            default -> new BakongApiException(
                status.value(),
                ErrorCode.CANNOT_CONNECT_SERVER.getCode(),
                "Client error occurred"
            );
        };
    }
}
