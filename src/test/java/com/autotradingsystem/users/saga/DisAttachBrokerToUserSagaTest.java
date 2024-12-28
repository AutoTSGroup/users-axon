package com.autotradingsystem.users.saga;

import com.autotradingsystem.messages.commands.broker.DeleteBrokerCommand;
import com.autotradingsystem.messages.events.user.DisAttachedBrokerToUserEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DisAttachBrokerToUserSagaTest {


    private SagaTestFixture<DisAttachBrokerToUserSaga> fixture;
    private CommandGateway commandGateway;

    private final UUID brokerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        fixture = new SagaTestFixture<>(DisAttachBrokerToUserSaga.class);
        commandGateway = Mockito.mock(CommandGateway.class);
        fixture.registerCommandGateway(CommandGateway.class, commandGateway);
    }

    @Test
    void test() {
        DisAttachedBrokerToUserEvent event = DisAttachedBrokerToUserEvent.builder()
                .brokerId(brokerId)
                .userId(1l)
                .build();

        DeleteBrokerCommand expectedCommand = DeleteBrokerCommand.builder()
                .brokerId(brokerId)
                .build();

        fixture.givenNoPriorActivity()
                .whenAggregate(brokerId.toString()).publishes(event)
                .expectActiveSagas(0)
                .expectNoScheduledEvents()
                .expectDispatchedCommands(expectedCommand);

        //verify(commandGateway, times(1)).sendAndWait(expectedCommand);
    }
    @Test
    void test_2() {
        DisAttachedBrokerToUserEvent event = DisAttachedBrokerToUserEvent.builder()
                .brokerId(brokerId)
                .userId(1l)
                .build();

        Mockito.doThrow(new RuntimeException("Test exc"))
                .when(commandGateway)
                .sendAndWait(any(DeleteBrokerCommand.class));

        DeleteBrokerCommand expectedCommand = DeleteBrokerCommand.builder()
                .brokerId(brokerId)
                .build();


        fixture.givenNoPriorActivity()
                .whenAggregate(brokerId.toString()).publishes(event)
                .expectActiveSagas(1)
                .expectDispatchedCommands(expectedCommand)
                .expectNoScheduledEvents();

    }
}
