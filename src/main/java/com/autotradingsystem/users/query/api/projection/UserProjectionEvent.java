package com.autotradingsystem.users.query.api.projection;

import com.autotradingsystem.messages.events.user.AttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.DisAttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.UserCreatedEvent;
import com.autotradingsystem.users.query.api.data.models.UserEntity;
import com.autotradingsystem.users.query.api.data.models.UserMappingBroker;
import com.autotradingsystem.users.query.api.data.repository.UserMappedBrokerRepository;
import com.autotradingsystem.users.query.api.data.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProjectionEvent {

    private final UserRepository userRepository;
    private final UserMappedBrokerRepository userMappedBrokerRepository;

    @EventHandler
    public void on(UserCreatedEvent event){
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(event, userEntity);
        userRepository.save(userEntity);
    }

    @EventHandler
    public void on(AttachedBrokerToUserEvent event){
        var user = userRepository.findByUserId(event.getUserId())
                .orElseThrow(()-> new EntityNotFoundException());

        var userMappedBroker = new UserMappingBroker(event.getBrokerId(), user);
        userMappedBrokerRepository.save(userMappedBroker);
    }

    @EventHandler
    public void on(DisAttachedBrokerToUserEvent event){
        userMappedBrokerRepository.deleteById(event.getBrokerId());
    }

}
