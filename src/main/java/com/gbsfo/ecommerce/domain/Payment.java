package com.gbsfo.ecommerce.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "payment")
public class Payment extends IdentifiableEntity implements Serializable {

    private static final long serialVersionUID = -2543425088717298237L;

    @JsonProperty("number")
    @Column(name = "number", nullable = false)
    private String number;

    @JsonProperty("sum")
    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @JsonProperty("paymentDateTime")
    @Column(name = "payment_date_time", nullable = false)
    @CreationTimestamp
    private Instant paymentDateTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private Order order;
}
