package com.java6.nhom1.repository;

import com.java6.nhom1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailEquals(String email);
    List<User> findAllByIdLike(String id);
}
