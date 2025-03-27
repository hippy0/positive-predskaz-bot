package me.hippy.prdsz.dao;

import me.hippy.prdsz.enums.UserRoleKey;
import me.hippy.prdsz.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    Optional<UserRole> findByKey(UserRoleKey key);
}
