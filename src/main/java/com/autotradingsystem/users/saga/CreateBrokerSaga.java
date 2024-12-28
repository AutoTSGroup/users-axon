package com.autotradingsystem.users.saga;

import com.autotradingsystem.messages.commands.user.AttachBrokerToUserCommand;
import com.autotradingsystem.messages.commands.user.DisAttachBrokerToUserCommand;
import com.autotradingsystem.messages.events.broker.CreatedBrokerEvent;
import com.autotradingsystem.messages.events.user.AttachedBrokerToUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class CreateBrokerSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "brokerId")
    public void handle(CreatedBrokerEvent event){
        try {
            AttachBrokerToUserCommand command = AttachBrokerToUserCommand.builder()
                    .userId(event.getUserId())
                    .brokerId(event.getBrokerId())
                    .build();

            commandGateway.sendAndWait(command);
        }catch (Exception e){
            DisAttachBrokerToUserCommand command = DisAttachBrokerToUserCommand.builder()
                    .userId(event.getUserId())
                    .brokerId(event.getBrokerId())
                    .build();
            commandGateway.sendAndWait(command);
            SagaLifecycle.end();
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "brokerId")
    public void handle(AttachedBrokerToUserEvent event){
        log.info("handle  AttachedBrokerToUserEvent");
        log.info("event data in brokerToUser: " + event);
        System.out.println("event data in brokerToUser: " + event);
    }

}
