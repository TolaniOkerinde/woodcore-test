package com.corebanking.sampleApplication.transactions.domain;

import com.corebanking.sampleApplication.account.domain.Account;
import com.corebanking.sampleApplication.account.domain.Customer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "from_account")
    private String fromAccount;

    @Column(name = "toaccount")
    private String toAccount;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "note")
    private String note;

    @Column(name = "sender_name")
    private String senderName;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;


    public Optional<Instant> getCreatedDate() {
        return null == this.createdAt ? Optional.empty() : Optional.of(this.createdAt.toInstant());
    }

    public void setCreatedDate(final Instant createdDate) {
        this.createdAt = null == createdDate ? null : Date.from(createdDate);
    }


}
