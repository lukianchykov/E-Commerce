package com.gbsfo.ecommerce.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gbsfo.ecommerce.utils.identifiable.IdentifiableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "order")
public class Order extends IdentifiableEntity implements Serializable {

    private static final long serialVersionUID = -2543425088717298236L;

    @JsonProperty("number")
    @Column(name = "number", nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @JsonProperty("order_status")
    @Column(name = "order_status", length = 20, nullable = false)
    private OrderStatus orderStatus;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private List<Item> total_items = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private List<Payment> total_payments = new ArrayList<>();

    public boolean allItemsPaid() {
        BigDecimal totalPaid = total_payments.stream()
            .map(Payment::getSum)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCost = total_items.stream()
            .map(Item::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPaid.compareTo(totalCost) >= 0;
    }

    public enum OrderStatus {
        @JsonProperty("CREATED") CREATED,
        @JsonProperty("PROCESSING") PROCESSING,
        @JsonProperty("SHIPPING") SHIPPING,
        @JsonProperty("DELIVERED") DELIVERED
    }
}
