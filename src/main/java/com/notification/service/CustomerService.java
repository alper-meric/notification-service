package com.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CustomerService {

    // Mock implementation to get customer info from customer service
    public Map<String, String> getCustomerInfo(String userId) {
        log.info("Getting customer info for userId: {}", userId);

        // Mock implementation - return different info based on payment method ID
        Map<String, String> CustomerInfo = new HashMap<>();


        CustomerInfo.put("name", "Joe");
        CustomerInfo.put("surname", "Doe");
        CustomerInfo.put("phoneNumber", "123456789");
        CustomerInfo.put("email", "foo@bar.com");

        return CustomerInfo;
    }
} 