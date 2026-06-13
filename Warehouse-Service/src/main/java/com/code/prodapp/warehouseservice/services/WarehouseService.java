package com.code.prodapp.warehouseservice.services;

import com.code.prodapp.warehouseservice.DTOs.CreateWarehouseRequestDTO;
import com.code.prodapp.warehouseservice.DTOs.UpdateWarehouseRequestDTO;
import com.code.prodapp.warehouseservice.DTOs.WarehouseResponseDTO;
import com.code.prodapp.warehouseservice.entities.Warehouse;
import com.code.prodapp.warehouseservice.exceptions.WarehouseNotFoundException;
import com.code.prodapp.warehouseservice.repositories.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private static final int WGS_84_SRID = 4326;

    private final WarehouseRepository warehouseRepository;
    private final ModelMapper modelMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), WGS_84_SRID);

    public WarehouseResponseDTO getWarehouseByUUID(UUID id) {
        log.info("Getting warehouse by id {}", id);
        return mapToDTO(findWarehouseEntityById(id));
    }

    public List<WarehouseResponseDTO> getAllWarehouses() {
        log.info("Getting all warehouses");
        return warehouseRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public WarehouseResponseDTO createWarehouse(CreateWarehouseRequestDTO requestDTO) {
        log.info("Creating warehouse {}", requestDTO.getWarehouseName());

        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(requestDTO.getWarehouseName());
        warehouse.setLocation(createPoint(requestDTO.getLat(), requestDTO.getLng()));
        warehouse.setCity(requestDTO.getCity());
        warehouse.setCapacity(requestDTO.getCapacity());
        warehouse.setActive(requestDTO.getActive() == null || requestDTO.getActive());

        return mapToDTO(warehouseRepository.save(warehouse));
    }

    @Transactional
    public WarehouseResponseDTO updateWarehouse(UUID id, UpdateWarehouseRequestDTO requestDTO) {
        log.info("Updating warehouse by id {}", id);

        Warehouse warehouse = findWarehouseEntityById(id);
        warehouse.setWarehouseName(requestDTO.getWarehouseName());
        warehouse.setLocation(createPoint(requestDTO.getLat(), requestDTO.getLng()));
        warehouse.setCity(requestDTO.getCity());
        warehouse.setCapacity(requestDTO.getCapacity());
        warehouse.setActive(requestDTO.getActive());

        return mapToDTO(warehouseRepository.save(warehouse));
    }

    @Transactional
    public void deleteWarehouse(UUID id) {
        log.info("Soft deleting warehouse by id {}", id);

        Warehouse warehouse = findWarehouseEntityById(id);
        warehouse.setActive(false);
        warehouseRepository.save(warehouse);
    }

    private Warehouse findWarehouseEntityById(UUID id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found with id " + id));
    }

    private WarehouseResponseDTO mapToDTO(Warehouse warehouse) {
        return modelMapper.map(warehouse, WarehouseResponseDTO.class);
    }

    // Helper Method
    private Point createPoint(double lat, double lng) {
        Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
        point.setSRID(WGS_84_SRID);
        return point;
    }
}
