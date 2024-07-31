package com.example.back.model.dto;

import lombok.Data;
import java.util.List;

/**
 * @author russ2
 */
@Data
public class FloodDataDTO {
    private Double totalWate;
    private List<double[]> coordinates;

    public FloodDataDTO(Double totalWate, List<double[]> coordinates) {
        this.totalWate = totalWate;
        this.coordinates = coordinates;
    }


}
