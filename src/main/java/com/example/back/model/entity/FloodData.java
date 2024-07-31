package com.example.back.model.entity;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 洪水数据对象
 *
 * @author russ2
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "flood")
public class FloodData {

    @Id
    private long id;

    @Column(name = "table_id")
    private String tableId;

    @Column(name = "total_wate")
    private Double totalWate;

    @Column(name = "geom_ycl")
    private String geomYcl;

    @Column(name = "sid")
    private Integer sId;

//    @Column(name = "surface_el")
//    private BigDecimal surfaceEl;

    /**
     * 解析地理坐标字符串，并将经度、纬度和海拔转换为double类型的数组。
     *
     * @return 包含经度、纬度和海拔的double类型数组列表。
     */
    public List<double[]> parseGeomYclToDoubles() {

        List<double[]> result = new ArrayList<>();

        try {
            // 移除字符串中的大括号和逗号，以便更容易地解析数据
            String[] segments = geomYcl.replaceAll("\\{", "").replaceAll("\\}", "").split(",");

            // 验证分割后的段数是否为3的倍数
            if (segments.length % 3 != 0) {
                throw new IllegalArgumentException("The number of segments in the geomYcl string is not a multiple of three.");
            }

            for (int i = 0; i < segments.length; i += 3) {
                double longitude = Double.parseDouble(segments[i]);
                double latitude = Double.parseDouble(segments[i + 1]);
                double altitude = Double.parseDouble(segments[i + 2]);

                // 将经度、纬度和海拔添加到结果列表中
                result.add(new double[]{longitude, latitude, altitude});
            }
        }  catch (NumberFormatException e) {
            // 处理数字解析异常
            System.err.println("Failed to parse geomYcl string to doubles: " + geomYcl);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // 处理非法参数异常
            System.err.println("Invalid geomYcl format: " + geomYcl);
            e.printStackTrace();
        }

        return result;
    }


}
