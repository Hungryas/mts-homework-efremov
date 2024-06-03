package org.example.repositories;

import org.example.entities.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnimalTypeRepository extends JpaRepository<AnimalType, UUID> {

    @Query("FROM AnimalType ORDER BY RANDOM() LIMIT 1")
    AnimalType getRandomType();
}
