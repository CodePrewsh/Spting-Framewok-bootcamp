package com.preciousProject.Fullstack.repository;

import com.preciousProject.Fullstack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
