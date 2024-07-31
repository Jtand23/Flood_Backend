package com.example.back.service;

import com.example.back.model.entity.FloodData;
import com.example.back.repository.CustomRepository;
import com.example.back.repository.FloodDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询洪水数据
 *
 * @author russ2
 */
@Service
public class FloodDataService {

    @Autowired
    private FloodDataRepository floodDataRepository;

    @Autowired
    private CustomRepository customRepository;

    public List<FloodData> getAllFloodData() {
        return floodDataRepository.findAll();
    }

    public List<FloodData> findByTableId(String tableId) {
        return floodDataRepository.findByTableId(tableId);
    }

    /**
     * 使用自定义的SQL语句进行JPQL查询
     * @param tableId
     * @return
     */
    public List<FloodData> findByTable(String tableId) {
        return customRepository.findByTable(tableId);
    }

    /**
     * 自定义SQL语句查询指定时序和指定表的洪水数据
     * @param tableId
     * @param sId
     * @return
     */
    public List<FloodData> findByTableAndSequence(String tableId, Integer sId) {
        return customRepository.findByTableAndSequence(sId, tableId);
    }

//    /**
//     * 查询一条数据
//     * @param tableId
//     * @paramm id
//     * @return
//     */
//    public FloodData findByTableAndId(String tableId, Long id) {
//        return customRepository.findByTableAndId(tableId, id);
//    }
}
