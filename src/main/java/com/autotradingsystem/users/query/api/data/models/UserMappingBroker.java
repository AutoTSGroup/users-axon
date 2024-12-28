package com.autotradingsystem.users.query.api.data.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_broker")
public class UserMappingBroker {

    @Id
    UUID brokerId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity userEntity;

}
