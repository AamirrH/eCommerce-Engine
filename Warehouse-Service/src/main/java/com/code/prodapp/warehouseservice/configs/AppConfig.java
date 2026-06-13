package com.code.prodapp.warehouseservice.configs;

import com.code.prodapp.warehouseservice.DTOs.WarehouseResponseDTO;
import com.code.prodapp.warehouseservice.entities.Warehouse;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Warehouse.class, WarehouseResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getLocation().getY(), WarehouseResponseDTO::setLat);
                    mapper.map(src -> src.getLocation().getX(), WarehouseResponseDTO::setLng);
                });

        return modelMapper;
    }
}
