package com.EcoSphere.odoo.scoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ESGScoreDTO {

    private int environmentalScore;
    private int socialScore;
    private int governanceScore;
    private int overallScore;
}