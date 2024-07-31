package com.example.back.repository;

import com.example.back.model.entity.FloodData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author russ2
 */
public interface FloodDataRepository extends JpaRepository<FloodData, Long> {
    /**
     * 根据tableId查询数据
     * @param tableId
     * @return
     */
    List<FloodData> findByTableId(String tableId);
}
