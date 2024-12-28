package com.autotradingsystem.users.saga;

import com.autotradingsystem.messages.commands.user.AttachBrokerToUserCommand;
import com.autotradingsystem.messages.commands.user.DisAttachBrokerToUserCommand;
import com.autotradingsystem.messages.events.broker.CreatedBrokerEvent;
import com.autotradingsystem.messages.events.user.AttachedBrokerToUserEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class CreateBrokerSagaTest {

    private SagaTestFixture<CreateBrokerSaga> fixture;
    private CommandGateway commandGateway;
    
    private final UUID brokerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        fixture = new SagaTestFixture<>(CreateBrokerSaga.class);
        commandGateway = Mockito.mock(CommandGateway.class);
        fixture.registerCommandGateway(CommandGateway.class, commandGateway);
    }

    @Test
    void testHandleCreatedBrokerEvent() {
        CreatedBrokerEvent event = CreatedBrokerEvent.builder()
                .brokerId(brokerId)
                .brokerName("TINKOFF")
                .token("token")
                .valid(false)
                .userId(1l)
                .build();

        AttachBrokerToUserCommand expectedCommand = AttachBrokerToUserCommand.builder()
                .userId(1l)
                .brokerId(brokerId)
                .build();

        fixture.givenNoPriorActivity()
                .whenAggregate(brokerId.toString()).publishes(event)
                .expectActiveSagas(1)
                .expectDispatchedCommands(expectedCommand);

        verify(commandGateway, times(1)).sendAndWait(expectedCommand);
    }

    @Test
    void testHandleAttachedBrokerToUserEvent() {
        AttachedBrokerToUserEvent event = AttachedBrokerToUserEvent.builder()
                .brokerId(brokerId)
                .userId(1l)
                .build();
        CreatedBrokerEvent eventCreateBroker = CreatedBrokerEvent.builder()
                .brokerId(brokerId)
                .brokerName("TINKOFF")
                .token("token")
                .valid(false)
                .userId(1l)
                .build();

        fixture.givenAggregate(brokerId.toString()).published(eventCreateBroker)
                .whenAggregate(brokerId.toString()).publishes(event)
                .expectActiveSagas(0)  // Сага завершается
                .expectNoScheduledEvents()
                .expectNoDispatchedCommands();
    }

    @Test
    void testHandleCreatedBrokerEventWithException() {
        CreatedBrokerEvent event = CreatedBrokerEvent.builder()
                .brokerId(brokerId)
                .brokerName("TINKOFF")
                .token("token")
                .valid(false)
                .userId(1l)
                .build();

        Mockito.doThrow(new RuntimeException("Test Exception"))
                .when(commandGateway).sendAndWait(Mockito.any(AttachBrokerToUserCommand.class));

        fixture.givenNoPriorActivity()
                .whenAggregate(brokerId.toString()).publishes(event)
                .expectActiveSagas(0) // Сага завершается при ошибке
                .expectNoScheduledEvents();

        DisAttachBrokerToUserCommand expectedCompensatingCommand = DisAttachBrokerToUserCommand.builder()
                .userId(1l)
                .brokerId(brokerId)
                .build();

        verify(commandGateway, times(1)).sendAndWait(expectedCompensatingCommand);
    }




}
