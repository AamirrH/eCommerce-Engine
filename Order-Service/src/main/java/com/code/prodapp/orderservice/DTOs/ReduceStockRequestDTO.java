package com.code.prodapp.orderservice.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ReduceStockRequestDTO {

    private Long productId;
    private Integer quantity;




}
