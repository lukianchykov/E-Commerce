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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "order")
public class Order extends IdentifiableEntity implements Serializable {

    private static final long serialVersionUID = -2543425088717298236L;

    @NotNull
    @JsonProperty("number")
    @Column(name = "number")
    private String number;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonProperty("order_status")
    @Column(name = "order_status", length = 20)
    private OrderStatus status;

    @NotNull
    @JsonProperty("total_items")
    @Column(name = "total_items", precision = 10, scale = 2)
    private BigDecimal totalItems;

    @NotNull
    @JsonProperty("total_payments")
    @Column(name = "total_payments", precision = 10, scale = 2)
    private BigDecimal totalPayments;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Item> items = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Payment> payments = new ArrayList<>();
}
