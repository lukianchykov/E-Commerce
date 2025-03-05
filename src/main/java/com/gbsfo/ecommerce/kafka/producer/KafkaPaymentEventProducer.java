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
    private final KafkaTemplate<String, PaymentCreated> paymentCreatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<String, PaymentUpdated> paymentUpdatedKafkaTemplate;

    @Autowired
    private final KafkaTemplate<String, PaymentDeleted> paymentDeletedKafkaTemplate;

    @Value("${spring.kafka.topic.payment}")
    private String paymentTopic;

    public KafkaPaymentEventProducer(KafkaTemplate<String, PaymentCreated> paymentCreatedKafkaTemplate,
                                     KafkaTemplate<String, PaymentUpdated> paymentUpdatedKafkaTemplate,
                                     KafkaTemplate<String, PaymentDeleted> paymentDeletedKafkaTemplate) {
        this.paymentCreatedKafkaTemplate = paymentCreatedKafkaTemplate;
        this.paymentUpdatedKafkaTemplate = paymentUpdatedKafkaTemplate;
        this.paymentDeletedKafkaTemplate = paymentDeletedKafkaTemplate;
    }

    public void sendPaymentEventCreated(Payment payment) {
        PaymentCreated paymentCreated = PaymentCreated.newBuilder()
            .setNumber(payment.getNumber())
            .setSum(ByteBuffer.wrap(payment.getSum().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .setPaymentDateTime(payment.getPaymentDateTime().toEpochMilli())
            .build();

        paymentCreatedKafkaTemplate.send(paymentTopic, paymentCreated.getPaymentId().toString(), paymentCreated);
        log.info("Payment Created event {} sent to topic: {}", paymentCreated, paymentTopic);
    }

    public void sendPaymentEventUpdated(Payment payment) {
        PaymentUpdated paymentUpdated = PaymentUpdated.newBuilder()
            .setNumber(payment.getNumber())
            .setSum(ByteBuffer.wrap(payment.getSum().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .setPaymentDateTime(payment.getPaymentDateTime().toEpochMilli())
            .build();

        paymentUpdatedKafkaTemplate.send(paymentTopic, paymentUpdated.getPaymentId().toString(), paymentUpdated);
        log.info("Payment Updated event {} sent to topic: {}", paymentUpdated, paymentTopic);
    }

    public void sendPaymentEventDeleted(Payment payment) {
        PaymentDeleted paymentDeleted = PaymentDeleted.newBuilder()
            .setNumber(payment.getNumber())
            .setSum(ByteBuffer.wrap(payment.getSum().toPlainString().getBytes(StandardCharsets.UTF_8)))
            .setPaymentDateTime(payment.getPaymentDateTime().toEpochMilli())
            .build();

        paymentDeletedKafkaTemplate.send(paymentTopic, paymentDeleted.getPaymentId().toString(), paymentDeleted);
        log.info("Payment Deleted event {} sent to topic: {}", paymentDeleted, paymentTopic);
    }
}
