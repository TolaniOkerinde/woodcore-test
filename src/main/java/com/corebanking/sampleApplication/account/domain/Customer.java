package com.corebanking.sampleApplication.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "phoneNumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    public Optional<Instant> getCreatedDate() {
        return null == this.createdAt ? Optional.empty() : Optional.of(this.createdAt.toInstant());
    }

    public void setCreatedDate(final Instant createdDate) {
        this.createdAt = null == createdDate ? null : Date.from(createdDate);
    }

    public void setUpdatedDate(final Instant updatedDate) {
        this.createdAt = null == updatedDate ? null : Date.from(updatedDate);
    }
}


