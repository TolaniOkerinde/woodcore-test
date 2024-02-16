package com.corebanking.sampleApplication.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "displayname", nullable = false)
    private String displayname;

    @OneToOne
    private Customer customer;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "account_balance")
    private BigDecimal accountBalance = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "closed_on_date", nullable = true)
    private Date closedOnDate;

    public Optional<Instant> getCreatedDate() {
        return null == this.createdAt ? Optional.empty() : Optional.of(this.createdAt.toInstant());
    }
    public void setCreatedDate(final Instant createdDate) {
        this.createdAt = null == createdDate ? null : Date.from(createdDate);
    }
    public Optional<Instant> getClosedOnDate() {
        return null == this.closedOnDate ? Optional.empty() : Optional.of(this.closedOnDate.toInstant());
    }
    public void setClosedOnDate(final Instant closedOnDate) {
        this.createdAt = null == closedOnDate ? null : Date.from(closedOnDate);
    }

}
