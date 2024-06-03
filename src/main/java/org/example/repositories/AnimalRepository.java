package org.example.repositories;

import org.example.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {

    @Query("""
            FROM Animal
            WHERE MOD (YEAR(birthDate), 4) = 0
            AND (MOD (YEAR(birthDate), 100) != 0
                OR MOD (YEAR(birthDate), 400) = 0)
            """)
    List<Animal> findLeapYearNames();

    @Query("""
            FROM Animal
            WHERE YEAR(birthDate) < (YEAR(CURRENT_DATE) - :age)
            """)
    List<Animal> findByBirthDateBefore(@Param("age") int age);

    Optional<Animal> findFirstByOrderByBirthDateAsc();

    @Query("SELECT AVG(YEAR (CURRENT_DATE) -  YEAR (birthDate)) FROM Animal")
    double findAverageAge();

    @Query("""
            FROM Animal
            WHERE YEAR(birthDate) < (YEAR(CURRENT_DATE) - 5)
            AND cost > (SELECT AVG(a2.cost) FROM Animal a2)
            """)
    List<Animal> findOldAndExpensive();

    @Query("FROM Animal ORDER BY cost LIMIT 3")
    List<Animal> findMinCostAnimals();
}