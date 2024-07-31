package com.example.back.repository;

import com.example.back.model.entity.FloodData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;


/**
 * 自定义的查询接口
 * @author russ2
 */
public interface CustomRepository extends JpaRepository<FloodData,Long> {
    /**
     * 根据table_id查询
     *
     * @param tableId
     * @return
     */
    @Query("SELECT f from FloodData f where f.tableId = ?1")
    List<FloodData> findByTable(String tableId);

    //根据table_id和sid查询数据（无需优化，数据库表中已经建立这两个字段的索引）
    @Query("SELECT f from FloodData f where f.sId = ?1 and f.tableId = ?2")
    List<FloodData> findByTableAndSequence(Integer sId, String tableId);
}


