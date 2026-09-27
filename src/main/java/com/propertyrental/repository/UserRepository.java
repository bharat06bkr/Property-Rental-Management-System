package com.propertyrental.repository;

import com.propertyrental.entity.Enums.OwnerStatus;
import com.propertyrental.entity.Enums.Role;
import com.propertyrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByOwnerStatus(OwnerStatus ownerStatus);

    List<User> findByRoleAndOwnerStatus(Role role, OwnerStatus ownerStatus);
}
