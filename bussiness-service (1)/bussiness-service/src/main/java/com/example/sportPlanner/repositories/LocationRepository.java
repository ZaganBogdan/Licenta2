package com.example.sportPlanner.repositories;

import com.example.sportPlanner.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
