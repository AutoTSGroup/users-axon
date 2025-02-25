package com.autotradingsystem.users.query.api.data.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
@Entity
@Table(schema = "usersAxon", name = "trader_bot_users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "userId", unique = true)
    Long userId;
    @Column(name = "username", unique = true)
    String username;
    String firstName;
    String lastName;
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    List<UserMappingBroker> userBrokers = new ArrayList<>();

}
