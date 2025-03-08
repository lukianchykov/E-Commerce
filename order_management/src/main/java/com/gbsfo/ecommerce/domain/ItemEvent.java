package com.gbsfo.ecommerce.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@RequiredArgsConstructor
@ToString(callSuper = true)
@Table(name = "item_events")
public class ItemEvent {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;

    @JsonProperty("price")
    @Column(name = "price", nullable = false)
    @JsonDeserialize(using = BigDecimalAvroDeserializer.class)
    private BigDecimal price;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "order_event_id")
//    @ToString.Exclude
//    private OrderEvent orderEvent;
}