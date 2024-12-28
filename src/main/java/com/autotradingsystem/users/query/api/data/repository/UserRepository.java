package com.autotradingsystem.users.query.api.data.repository;

import com.autotradingsystem.users.query.api.data.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Optional<UserEntity> findByUserId(Long id);

}
