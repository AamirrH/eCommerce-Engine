package com.code.prodapp.inventoryservice.controllers;


import com.code.prodapp.inventoryservice.DTOs.ProductDTO;
import com.code.prodapp.inventoryservice.clients.OrdersFeignClient;
import com.code.prodapp.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;
    private final OrdersFeignClient ordersFeignClient;

    @GetMapping("/fetchProducts")
    public String fetchProducts() {
        log.info("fetchProducts");
        return ordersFeignClient.testOrders();
    }

    @GetMapping("/discovered-services")
    public List<String> getDiscoveredServices() {
        return discoveryClient.getServices();
    }



    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllInventory());
    }

    @PostMapping("/{ID}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(name = "ID") Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }





}
