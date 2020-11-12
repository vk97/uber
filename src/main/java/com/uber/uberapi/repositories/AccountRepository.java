package com.uber.uberapi.repositories;

import com.uber.uberapi.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Booking,Long> {
}
