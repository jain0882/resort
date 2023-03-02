package com.resorts.season.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.resorts.season.model.LiftRide;
@Repository
public interface LiftRideRepository extends JpaRepository<LiftRide, Long> {
}
