package com.bg.joker.mapper;

import com.bg.joker.entity.Event;
import com.bg.joker.entity.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreAdminMapper {
    List<Event> findEventsForAdmin(@Param("userId") Long userId, @Param("storeId") Long storeId);

    List<Store> findActiveStoresByAdminId(@Param("userId") Long userId);
}
