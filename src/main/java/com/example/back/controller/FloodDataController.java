package com.example.back.controller;

import com.example.back.model.entity.FloodData;
import com.example.back.model.dto.FloodDataDTO;
import com.example.back.service.FloodDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RESTful API控制器，处理与洪水模拟数据相关的HTTP请求。
 * 将数据转换为DTO对象，并返回给前端
 * 允许跨域请求，指定前端应用的位置
 *
 * @author russ2
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class FloodDataController {

    /**
     * 日志记录器实例，用于记录信息和调试
     */

    private static final Logger log = LoggerFactory.getLogger(FloodDataController.class);

    /**
     * 通过@Autowired注解自动装配FloodDataService实例
     */

    @Autowired
    private FloodDataService floodDataService;

    /**
     * 前端通过改变后缀，请求不同table_id和时序编号sid的数据
     *
     * @param tableId
     * @param sId
     * @return
     */
    @GetMapping("flood-data/{tableId}/{sId}")
    public List<FloodDataDTO> getFloodDataByTable(@PathVariable String tableId, @PathVariable Integer sId) {

        log.info("getFloodDataByTable 方法被调用.");

       try {
           //使用自定义接口进行查询
           List<FloodData> floodDataList = floodDataService.findByTableAndSequence(tableId, sId);
           List<FloodDataDTO> dtoList = floodDataList.stream()
                   .map(floodData -> new FloodDataDTO(floodData.getTotalWate(), floodData.parseGeomYclToDoubles()))
                   .collect(Collectors.toList());
           log.info("返回了 {} 条洪水数据条目.", dtoList.size());

           return dtoList;
       } catch (Exception e) {
           log.error("处理洪水数据请求时发生错误：", e);
           //错误时返回空列表
           return Collections.emptyList();
       }

    }
}




