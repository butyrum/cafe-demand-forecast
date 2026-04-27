package com.cafe.forecast.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "mogo_id")
    private String mogoId;

    @Column(name = "mogo_product_id")
    private String mogoProductId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "sale_date")
    private LocalDate saleDate;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "total_value", precision = 10, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "synced_from_mogo")
    private Boolean syncedFromMogo = false;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "status")
    private String status;
}
