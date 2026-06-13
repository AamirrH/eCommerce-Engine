package com.code.prodapp.warehouseservice.repositories;

import com.code.prodapp.warehouseservice.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {


    // We need to write the query in native SQL because, JPQL might not have support for PostGIS
    // ST_MakePoint makes a geometric point with those specific longitude and latitude
    // ST_SetSRID (Spatial-Reference-ID) converts them into GPS-like coordinates
    /* ::converts those geometric points into geographical points since we need to calculate the distance
     between each warehouse's location (location-column is a geographical entity) and the geographical
     point we calculated and ORDER it in ASCENDING ORDER and pick the smallest distance warehouse.
     */
    @Query(
            value = """
            SELECT * from warehouse
            WHERE active = true
            ORDER BY location <-> ((st_setsrid(st_makepoint(:lon,:lat),4326))::geography)
            LIMIT 1;
            """,nativeQuery = true
    )
    Optional<Warehouse> findNearestWarehouse(@Param("lon") double lon,
                                             @Param("lat") double lat);

}
