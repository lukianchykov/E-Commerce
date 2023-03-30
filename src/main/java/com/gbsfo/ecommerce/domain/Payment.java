package com.gbsfo.ecommerce.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gbsfo.ecommerce.utils.identifiable.IdentifiableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@SuperBuilder
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "payments")
public class Payment extends IdentifiableEntity implements Serializable {

    private static final long serialVersionUID = -2543425088717298237L;

    @JsonProperty("number")
    @Column(name = "number", nullable = false)
    private String number;

    @JsonProperty("sum")
    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @JsonProperty("paymentDateTime")
    @Column(name = "payment_date_time")
    @CreationTimestamp
    private Instant paymentDateTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;
}
