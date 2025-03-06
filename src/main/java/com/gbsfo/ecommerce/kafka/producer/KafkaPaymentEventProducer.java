package com.gbsfo.ecommerce.kafka.producer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.gbsfo.ecommerce.domain.Payment;
import com.gbsfo.kafka.payment.PaymentCreated;
import com.gbsfo.kafka.payment.PaymentDeleted;
import com.gbsfo.kafka.payment.PaymentUpdated;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaPaymentEventProducer {

    @Autowired
    private final KafkaTemplate<Integer, String> paymentCreatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<Integer, String> paymentUpdatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<Integer, String> paymentDeletedKafkaTemplate;

    @Value("${spring.kafka.topic.payment}")
    private String paymentTopic;

    public KafkaPaymentEventProducer(KafkaTemplate<Integer, String> paymentCreatedKafkaTemplate,
                                     KafkaTemplate<Integer, String> paymentUpdatedKafkaTemplate,
                                     KafkaTemplate<Integer, String> paymentDeletedKafkaTemplate) {
        this.paymentCreatedKafkaTemplate = paymentCreatedKafkaTemplate;
        this.paymentUpdatedKafkaTemplate = paymentUpdatedKafkaTemplate;
        this.paymentDeletedKafkaTemplate = paymentDeletedKafkaTemplate;
    }

    public void sendPaymentEventCreated(Payment payment) {
        PaymentCreated paymentCreated = PaymentCreated.newBuilder()
            .setPaymentId(payment.getId())
            .setNumber(payment.getNumber())
            .setSum(ByteBuffer.wrap(payment.getSum().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .setPaymentDateTime(payment.getPaymentDateTime().toEpochMilli())
            .build();

        paymentCreatedKafkaTemplate.send(paymentTopic, paymentCreated.getPaymentId().intValue(), paymentCreated.toString());
        log.info("Payment Created event {} sent to topic: {}", paymentCreated, paymentTopic);
    }

    public void sendPaymentEventUpdated(Payment payment) {
        PaymentUpdated paymentUpdated = PaymentUpdated.newBuilder()
            .setPaymentId(payment.getId())
            .setNumber(payment.getNumber())
            .setSum(ByteBuffer.wrap(payment.getSum().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .setPaymentDateTime(payment.getPaymentDateTime().toEpochMilli())
            .build();

        paymentUpdatedKafkaTemplate.send(paymentTopic, paymentUpdated.getPaymentId().intValue(), paymentUpdated.toString());
        log.info("Payment Updated event {} sent to topic: {}", paymentUpdated, paymentTopic);
    }

    public void sendPaymentEventDeleted(Payment payment) {
        PaymentDeleted paymentDeleted = PaymentDeleted.newBuilder()
            .setPaymentId(payment.getId())
            .setNumber(payment.getNumber())
            .setSum(ByteBuffer.wrap(payment.getSum().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .setPaymentDateTime(payment.getPaymentDateTime().toEpochMilli())
            .build();

        paymentDeletedKafkaTemplate.send(paymentTopic, paymentDeleted.getPaymentId().intValue(), paymentDeleted.toString());
        log.info("Payment Deleted event {} sent to topic: {}", paymentDeleted, paymentTopic);
    }
}
