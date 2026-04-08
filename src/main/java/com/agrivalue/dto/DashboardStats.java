package com.agrivalue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {
    private long totalUsers;
    private long totalBuyers;
    private long totalFarmers;
    private long totalProducts;
    private long totalOrders;
    private BigDecimal totalRevenue;
    private Map<String, Long> categoryDistribution;
    private Map<String, Long> monthlySales;
}
