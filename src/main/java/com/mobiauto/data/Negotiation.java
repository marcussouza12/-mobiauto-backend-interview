package com.mobiauto.data;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Serdeable
public class Negotiation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private double initialOffer;

    private Date startDate;

    @OneToMany(mappedBy = "negotiation", cascade = CascadeType.ALL)
    private List<Message> messages;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public double getInitialOffer() {
        return initialOffer;
    }

    public void setInitialOffer(double initialOffer) {
        this.initialOffer = initialOffer;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    public void clearMessages() {
        this.messages.clear();
    }

    public void updateMessage(Message message) {
        for (int i = 0; i < this.messages.size(); i++) {
            if (Objects.equals(this.messages.get(i).getId(), message.getId())) {
                this.messages.set(i, message);
                break;
            }
        }
    }

    public void deleteMessage(Long messageId) {
        for (int i = 0; i < this.messages.size(); i++) {
            if (Objects.equals(this.messages.get(i).getId(), messageId)) {
                this.messages.remove(i);
                break;
            }
        }
    }

    public Message getMessage(Long messageId) {
        for (Message message : this.messages) {
            if (Objects.equals(message.getId(), messageId)) {
                return message;
            }
        }
        return null;
    }

    public int getMessagesCount() {
        return this.messages.size();
    }

    public Message getLastMessage() {
        return this.messages.get(this.messages.size() - 1);
    }

    public boolean hasMessages() {
        return this.messages.size() > 0;
    }

    public boolean hasMessage(Long messageId) {
        for (Message message : this.messages) {
            if (Objects.equals(message.getId(), messageId)) {
                return true;
            }
        }
        return false;
    }
}
