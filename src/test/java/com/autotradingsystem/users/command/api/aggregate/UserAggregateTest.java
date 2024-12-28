package com.autotradingsystem.users.command.api.aggregate;

import com.autotradingsystem.messages.commands.user.AttachBrokerToUserCommand;
import com.autotradingsystem.messages.commands.user.CreateUserCommand;
import com.autotradingsystem.messages.commands.user.DisAttachBrokerToUserCommand;
import com.autotradingsystem.messages.events.user.AttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.DisAttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.UserCreatedEvent;
import org.axonframework.eventsourcing.eventstore.EventStoreException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserAggregateTest {

    private FixtureConfiguration<UserAggregate> aggregate;

    private CreateUserCommand createUserCommand;

    @Before
    public void setUp() {
        aggregate = new AggregateTestFixture<>(UserAggregate.class);
        createUserCommand = CreateUserCommand.builder()
                .userId(1L)
                .firstName("alex")
                .lastName("jon")
                .phoneNumber("123")
                .username("user")
                .build();
    }

    @Test
    public void create_user_test() {
        var expectedEvent = UserCreatedEvent.builder()
                .userId(1L)
                .firstName("alex")
                .lastName("jon")
                .phoneNumber("123")
                .username("user")
                .build();
        aggregate.givenNoPriorActivity()
                .when(createUserCommand)
                .expectEvents(expectedEvent);
    }

    @Test
    public void dublicate_create_user_test_throw_ex() {
        aggregate.givenCommands(createUserCommand)
                .when(createUserCommand).expectException(EventStoreException.class);
    }

    @Test
    public void attach_broker_to_user_test() {
        UUID brokerId = UUID.randomUUID();
        var attachBrokerCommand = AttachBrokerToUserCommand.builder()
                .userId(1L)
                .brokerId(brokerId)
                .build();

        var expectedAttachEvent = AttachedBrokerToUserEvent.builder()
                .userId(1L)
                .brokerId(brokerId)
                .build();

        aggregate.givenCommands(createUserCommand)
                .when(attachBrokerCommand)
                .expectEvents(expectedAttachEvent)
                .expectState(userAggregate -> {
                    assertEquals(1, userAggregate.brokers.size());
                    assertTrue(userAggregate.brokers.contains(brokerId));
                });
    }

    @Test
    public void disattach_broker_from_user_test() {

        UUID brokerId = UUID.randomUUID();
        var attachBrokerCommand = AttachBrokerToUserCommand.builder()
                .userId(1L)
                .brokerId(brokerId)
                .build();

        var disattachBrokerCommand = DisAttachBrokerToUserCommand.builder()
                .userId(1L)
                .brokerId(brokerId)
                .build();

        var expectedDisattachEvent = DisAttachedBrokerToUserEvent.builder()
                .userId(1L)
                .brokerId(brokerId)
                .build();

        aggregate.givenCommands(createUserCommand, attachBrokerCommand)
                .when(disattachBrokerCommand)
                .expectEvents(expectedDisattachEvent)
                .expectState(userAggregate -> {
                    assertTrue(userAggregate.brokers.isEmpty());
                });
    }


    @Test
    public void duplicate_attach_broker_to_user_test_should_throw_exception() {
        UUID brokerId = UUID.randomUUID();
        var attachBrokerCommand = AttachBrokerToUserCommand.builder()
                .userId(1L)
                .brokerId(brokerId)
                .build();

        aggregate.givenCommands(createUserCommand, attachBrokerCommand)
                .when(attachBrokerCommand)
                .expectException(IllegalStateException.class)
                .expectNoEvents(); // Убедитесь, что в агрегате есть логика для предотвращения дублирования
    }

    @Test
    public void dublicate_disattach_broker_from_user_test_throw_exception() {
        UUID brokerId = UUID.randomUUID();

        var disattachBrokerCommand = DisAttachBrokerToUserCommand.builder()
                .userId(1L)
                .brokerId(brokerId)
                .build();


        aggregate.givenCommands(createUserCommand)
                .when(disattachBrokerCommand)
                .expectException(IllegalStateException.class)
                .expectNoEvents();
    }

}