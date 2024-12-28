package com.autotradingsystem.users.saga;

import com.autotradingsystem.messages.commands.broker.DeleteBrokerCommand;
import com.autotradingsystem.messages.commands.user.AttachBrokerToUserCommand;
import com.autotradingsystem.messages.events.broker.DeletedBrokerEvent;
import com.autotradingsystem.messages.events.user.AttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.DisAttachedBrokerToUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class DisAttachBrokerToUserSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "brokerId")
    public void handle(DisAttachedBrokerToUserEvent event){
        DeleteBrokerCommand deleteBrokerCommand = DeleteBrokerCommand.builder()
                .brokerId(event.getBrokerId())
                .build();

        commandGateway.sendAndWait(deleteBrokerCommand);

        SagaLifecycle.end();
    }
}
