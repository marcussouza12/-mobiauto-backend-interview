package com.mobiauto.repository;

import com.mobiauto.data.Message;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
