package com.autotradingsystem.users.query.api.data.repository;

import com.autotradingsystem.users.query.api.data.models.UserMappingBroker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserMappedBrokerRepository extends JpaRepository<UserMappingBroker, UUID> {

}
