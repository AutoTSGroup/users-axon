package com.autotradingsystem.users.command.api.controller;

import com.autotradingsystem.messages.commands.broker.CreateBrokerCommand;
import com.autotradingsystem.messages.commands.user.CreateUserCommand;
import com.autotradingsystem.messages.commands.user.DisAttachBrokerToUserCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserBrokerController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("/addUser/{userId}")
    public void addUser(@PathVariable("userId") Long userId){
        CreateUserCommand command = CreateUserCommand.builder()
                .userId(userId)
                .username("alex")
                .firstName("rop")
                .lastName("ret")
                .phoneNumber("8129")
                .build();
        commandGateway.send(command);
    }

    @PostMapping("/addBroker/{userId}")
    public void addBroker(@PathVariable("userId") Long userId){

        CreateBrokerCommand command = CreateBrokerCommand.builder()
                .brokerId(UUID.randomUUID())
                .token("token")
                .userId(userId)
                .brokerName("TINKOFF")
                .build();
        commandGateway.sendAndWait(command);
    }

    @PostMapping("/disAttached")
    public void disAttach(@Param("userId") Long userId, @Param("brokerId") UUID brokerId){
        DisAttachBrokerToUserCommand command = DisAttachBrokerToUserCommand.builder()
                .userId(userId)
                .brokerId(brokerId)
                .build();
        commandGateway.sendAndWait(command);
    }
}
