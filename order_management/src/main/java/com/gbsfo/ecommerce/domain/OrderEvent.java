package com.gbsfo.ecommerce.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "order_events")
public class OrderEvent {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("number")
    @Column(name = "number", nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @JsonProperty("order_status")
    @Column(name = "order_status", length = 20, nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED;

//    @Builder.Default
//    @ToString.Exclude
//    @OneToMany(mappedBy = "orderEvent", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ItemEvent> totalItems = new ArrayList<>();
//
//    @Builder.Default
//    @ToString.Exclude
//    @OneToMany(mappedBy = "orderEvent", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<PaymentEvent> totalPayments = new ArrayList<>();

    public enum OrderStatus {
        @JsonProperty("CREATED") CREATED,
        @JsonProperty("PROCESSING") PROCESSING,
        @JsonProperty("SHIPPING") SHIPPING,
        @JsonProperty("DELIVERED") DELIVERED
    }
}

