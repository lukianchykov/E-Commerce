package com.gbsfo.ecommerce.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gbsfo.ecommerce.domain.Item;
import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.ecommerce.dto.ItemDto;
import com.gbsfo.ecommerce.dto.PaymentDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MapperUtils {

    public static List<Item> mapItems(List<ItemDto> itemsList) {
        // map other emails
        List<Item> result = Optional.ofNullable(itemsList)
            .orElse(new ArrayList<>())
            .stream()
            .map(item -> Item.builder()
                .name(item.getName())
                .price(item.getPrice())
                .build())
            .collect(Collectors.toList());
        // remove duplicates by distinct
        return result.stream()
            .filter(FunctionalUtils.distinctByKey(Item::getName))
            .collect(Collectors.toList());
    }

    public static List<Payment> mapPayments(List<PaymentDto> paymentsList) {
        // map other phones
        List<Payment> result = Optional.ofNullable(paymentsList)
            .orElse(new ArrayList<>())
            .stream()
            .map(payment -> Payment.builder()
                .number(payment.getNumber())
                .sum(payment.getSum())
                .build())
            .collect(Collectors.toList());
        // remove duplicates by distinct
        return result.stream()
            .filter(FunctionalUtils.distinctByKey(Payment::getNumber))
            .collect(Collectors.toList());
    }
}
