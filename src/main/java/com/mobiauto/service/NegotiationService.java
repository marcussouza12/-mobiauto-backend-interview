package com.mobiauto.service;

import com.mobiauto.data.Negotiation;
import com.mobiauto.repository.NegotiationRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
@Serdeable
public class NegotiationService {

    private final NegotiationRepository negotiationRepository;

    public NegotiationService(NegotiationRepository negotiationRepository) {
        this.negotiationRepository = negotiationRepository;
    }

    public Negotiation saveNegotiation(Negotiation negotiation) {
        return negotiationRepository.save(negotiation);
    }

    public Negotiation updateNegotiation(Negotiation negotiation) {
        return negotiationRepository.update(negotiation);
    }

    public List<Negotiation> getAllNegotiations() {
        return negotiationRepository.findAll();
    }

    public @Nullable Negotiation getNegotiationById(Long id) {
        return negotiationRepository.findById(id).orElse(null);
    }

    public void deleteNegotiation(Long id) {
        negotiationRepository.deleteById(id);
    }

}
