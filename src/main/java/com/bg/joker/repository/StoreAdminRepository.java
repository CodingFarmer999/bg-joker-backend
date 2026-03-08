package com.bg.joker.repository;

import com.bg.joker.entity.StoreAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreAdminRepository extends JpaRepository<StoreAdmin, Long> {

    List<StoreAdmin> findByUserId(Long userId);

    List<StoreAdmin> findByStoreId(Long storeId);

    Optional<StoreAdmin> findByUserIdAndStoreId(Long userId, Long storeId);
}
