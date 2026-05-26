package com.code.prodapp.inventoryservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

// Path -> Request Mapping for a particular Controller
@FeignClient(name = "Order-Service",path = "/orders")
public interface OrdersFeignClient {

    @GetMapping("/testOrders")
    String testOrders();


}
