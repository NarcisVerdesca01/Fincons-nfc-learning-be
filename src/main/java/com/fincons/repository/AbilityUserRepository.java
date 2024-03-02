package com.fincons.repository;

import com.fincons.entity.Ability;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AbilityUserRepository extends JpaRepository<AbilityUser,Long> {


    boolean existsByAbilityAndUser(Ability existingAbilityToAssociate, User existingUserToAddAssociate);
}
