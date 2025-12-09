// src/main/java/com/toolvault/identity_service/repository/MainRepository.java
package com.toolvault.identity_service.repository;

import com.toolvault.identity_service.domain.MainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainRepository extends JpaRepository<MainEntity, Long> {
    // add query methods here if needed
}
