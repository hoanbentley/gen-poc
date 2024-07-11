package com.sia.poc.repository;

import com.sia.poc.domain.entity.Passenger;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findByUid(String uid);
    
    void deleteByUid(String uid);

}
