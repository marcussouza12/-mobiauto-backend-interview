package com.mobiauto.service;

import com.mobiauto.data.Message;
import com.mobiauto.repository.MessageRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
@Serdeable
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Message message) {
        return messageRepository.update(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public @Nullable Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }


}
