package com.gbsfo.ecommerce.domain;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@SuperBuilder
@RequiredArgsConstructor
@ToString(callSuper = true)
@Table(name = "payment_events")
public class PaymentEvent {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("number")
    @Column(name = "number", nullable = false)
    private String number;

    @JsonProperty("sum")
    @Column(name = "sum", nullable = false)
    @JsonDeserialize(using = BigDecimalAvroDeserializer.class)
    private BigDecimal sum;

    @JsonProperty("paymentDateTime")
    @Column(name = "payment_date_time")
    @CreationTimestamp
    private Instant paymentDateTime;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "order_event_id")
//    @ToString.Exclude
//    private OrderEvent orderEvent;
}
