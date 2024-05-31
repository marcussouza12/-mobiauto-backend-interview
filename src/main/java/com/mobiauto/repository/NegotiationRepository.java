package com.mobiauto.repository;

import com.mobiauto.data.Negotiation;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation, Long> {
}
