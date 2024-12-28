package com.autotradingsystem.users.query.api.projection;

import com.autotradingsystem.messages.events.user.AttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.DisAttachedBrokerToUserEvent;
import com.autotradingsystem.messages.events.user.UserCreatedEvent;
import com.autotradingsystem.users.query.api.data.models.UserEntity;
import com.autotradingsystem.users.query.api.data.models.UserMappingBroker;
import com.autotradingsystem.users.query.api.data.repository.UserMappedBrokerRepository;
import com.autotradingsystem.users.query.api.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProjectionEventTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMappedBrokerRepository userMappedBrokerRepository;

    @InjectMocks
    private UserProjectionEvent userProjectionEvent;

    @Test
    public void on_userCreatedEvent_should_save_user() {
        // Arrange
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(1L)
                .firstName("alex")
                .lastName("jon")
                .phoneNumber("123")
                .username("user")
                .build();
        
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(event, userEntity);

        // Act
        userProjectionEvent.on(event);

        // Assert
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void on_attachedBrokerToUserEvent_should_save_userMappedBroker() {
        // Arrange
        AttachedBrokerToUserEvent event = AttachedBrokerToUserEvent.builder()
                .userId(1L)
                .brokerId(UUID.randomUUID())
                .build();

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(event.getUserId());

        when(userRepository.findByUserId(event.getUserId())).thenReturn(Optional.of(userEntity));

        UserMappingBroker userMappedBroker = new UserMappingBroker(event.getBrokerId(), userEntity);

        // Act
        userProjectionEvent.on(event);

        // Assert
        verify(userRepository, times(1)).findByUserId(event.getUserId());
        verify(userMappedBrokerRepository, times(1)).save(userMappedBroker);
    }

    @Test
    public void on_disattachedBrokerToUserEvent_should_delete_userMappedBroker() {
        // Arrange
        DisAttachedBrokerToUserEvent event = DisAttachedBrokerToUserEvent.builder()
                .userId(1L)
                .brokerId(UUID.randomUUID())
                .build();

        // Act
        userProjectionEvent.on(event);

        // Assert
        verify(userMappedBrokerRepository, times(1)).deleteById(event.getBrokerId());
    }
}
