package com.cafe.forecast.repository;

import com.cafe.forecast.model.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {

    // ================= Mogo Integration =================

    /**
     * Verifica se venda existe pelo ID externo do Mogo
     */
    boolean existsByMogoId(String mogoId);

    /**
     * Busca entidade por ID externo do Mogo
     */
    Optional<SalesRecord> findByMogoId(String mogoId);

    /**
     * Conta vendas sincronizadas do Mogo
     */
    long countBySyncedFromMogoTrue();

    /**
     * Conta vendas sincronizadas por data
     */
    long countBySaleDateAndSyncedFromMogoTrue(LocalDate date);

    /**
     * Busca vendas sincronizadas do Mogo por data
     */
    List<SalesRecord> findBySaleDateAndSyncedFromMogoTrue(LocalDate date);

    // ================= Consultas Gerais =================

    /**
     * Busca vendas por data
     */
    List<SalesRecord> findBySaleDate(LocalDate date);

    /**
     * Busca vendas por produto
     */
    List<SalesRecord> findByProductId(Long productId);

    /**
     * Busca vendas por período
     */
    List<SalesRecord> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Busca vendas por produto e período
     */
    List<SalesRecord> findByProductIdAndSaleDateBetween(Long productId, LocalDate startDate, LocalDate endDate);

    /**
     * Busca vendas por dia da semana (MONDAY, TUESDAY, etc.)
     */
    List<SalesRecord> findByDayOfWeek(String dayOfWeek);

    /**
     * ✅ Soma quantidade vendida por produto (USANDO @Query)
     */
    @Query("SELECT COALESCE(SUM(sr.quantitySold), 0) FROM SalesRecord sr WHERE sr.product.id = :productId")
    Integer sumQuantitySoldByProductId(@Param("productId") Long productId);

    /**
     * Busca entidades por produto (para IA)
     */
    List<SalesRecord> findEntitiesByProductId(Long productId);
}
