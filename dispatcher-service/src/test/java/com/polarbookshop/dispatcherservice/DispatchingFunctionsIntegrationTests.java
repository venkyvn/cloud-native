package com.polarbookshop.dispatcherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@FunctionalSpringBootTest
public class DispatchingFunctionsIntegrationTests {
    @Autowired
    private FunctionCatalog functionCatalog;
    private long orderId = 121;

    @Test
    void packOrder() {
        Function<OrderAcceptedMessage, Long> pack = functionCatalog.lookup(Function.class, "pack");

        assertThat(pack.apply(new OrderAcceptedMessage(orderId))).isEqualTo(orderId);
    }

    @Test
    void labelOrder() {
        Function<Flux<Long>, Flux<OrderDispatchedMessage>> labelOrder = functionCatalog.lookup(Function.class, "label");

        StepVerifier.create(labelOrder.apply(Flux.just(orderId)))
                .expectNextMatches(orderDispatchedMessage -> orderDispatchedMessage.equals(new OrderDispatchedMessage(orderId)))
                .verifyComplete();
    }

    @Test
    void packAndLabelOrder() {
        Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>>
                packAndLabel = functionCatalog.lookup(Function.class, "pack|label");

        StepVerifier.create(packAndLabel.apply(new OrderAcceptedMessage(orderId)))
                .expectNextMatches(
                        orderDispatchedMessage -> orderDispatchedMessage.equals(new OrderDispatchedMessage(orderId))
                )
                .verifyComplete();

    }
}
