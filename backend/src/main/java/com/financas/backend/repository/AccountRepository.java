package com.financas.backend.repository;

import com.financas.backend.entity.Account;
import com.financas.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserId(Long userId);

}
