package com.autotradingsystem.users.command.api.aggregate;

import com.autotradingsystem.messages.commands.user.AttachBrokerToUserCommand;
import com.autotradingsystem.messages.commands.user.DisAttachBrokerToUserCommand;
import com.autotradingsystem.messages.events.user.AttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.DisAttachedBrokerToUserEvent;
import com.autotradingsystem.messages.commands.user.CreateUserCommand;
import com.autotradingsystem.messages.events.user.UserCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Класс UserAggregate представляет собой агрегат Axon, который управляет состоянием пользователя.
 * Агрегат выполняет команды, связанные с созданием пользователя и привязкой/отвязкой брокеров.
 */
@Aggregate
@NoArgsConstructor
public class UserAggregate {

    @AggregateIdentifier
    Long userId; // Уникальный идентификатор пользователя
    String username; // Логин пользователя
    String firstName; // Имя пользователя
    String lastName; // Фамилия пользователя
    String phoneNumber; // Номер телефона пользователя
    List<UUID> brokers = new ArrayList<>(); // Список идентификаторов брокеров, связанных с пользователем

    /**
     * Обработчик команды CreateUserCommand.
     * Создает событие UserCreatedEvent для инициализации пользователя.
     */
    @CommandHandler
    public UserAggregate(CreateUserCommand command) {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(command.getUserId())
                .username(command.getUsername())
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .phoneNumber(command.getPhoneNumber())
                .build();
        AggregateLifecycle.apply(event);
    }

    /**
     * Обработчик события UserCreatedEvent.
     * Устанавливает состояние агрегата на основе данных события.
     */
    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        this.userId = event.getUserId();
        this.username = event.getUsername();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.phoneNumber = event.getPhoneNumber();
    }

    /**
     * Обработчик команды AttachBrokerToUserCommand.
     * Привязывает брокера к пользователю и создает событие AttachedBrokerToUserEvent.
     */
    @CommandHandler
    public void handle(AttachBrokerToUserCommand command) {
        if (this.brokers.contains(command.getBrokerId())) {
            throw new IllegalStateException("Нельзя добавлять того же брокера");
        }
        AttachedBrokerToUserEvent event = AttachedBrokerToUserEvent.builder()
                .userId(command.getUserId())
                .brokerId(command.getBrokerId())
                .build();
        AggregateLifecycle.apply(event);
    }

    /**
     * Обработчик события AttachedBrokerToUserEvent.
     * Добавляет идентификатор брокера в список.
     */
    @EventSourcingHandler
    public void on(AttachedBrokerToUserEvent event) {
        if (brokers == null) {
            brokers = new ArrayList<>();
        }
        brokers.add(event.getBrokerId());
    }

    /**
     * Обработчик команды DisAttachBrokerToUserCommand.
     * Отвязывает брокера от пользователя и создает событие DisAttachedBrokerToUserEvent.
     */
    @CommandHandler
    public void handle(DisAttachBrokerToUserCommand command) {
        if (!this.brokers.contains(command.getBrokerId())) {
            throw new IllegalStateException("Нельзя убрать не привязанного брокера");
        }
        DisAttachedBrokerToUserEvent event = DisAttachedBrokerToUserEvent.builder()
                .userId(command.getUserId())
                .brokerId(command.getBrokerId())
                .build();
        AggregateLifecycle.apply(event);
    }

    /**
     * Обработчик события DisAttachedBrokerToUserEvent.
     * Удаляет идентификатор брокера из списка.
     */
    @EventSourcingHandler
    public void on(DisAttachedBrokerToUserEvent event) {
        if (brokers != null) {
            brokers.remove(event.getBrokerId());
        }
    }
}
