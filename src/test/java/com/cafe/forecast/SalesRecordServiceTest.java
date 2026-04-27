package com.cafe.forecast.service;

import com.cafe.forecast.dto.SalesRecordDTO;
import com.cafe.forecast.mapper.SalesRecordMapper;
import com.cafe.forecast.model.Product;
import com.cafe.forecast.model.SalesRecord;
import com.cafe.forecast.repository.ProductRepository;
import com.cafe.forecast.repository.SalesRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SalesRecordService Tests")
class SalesRecordServiceTest {

    @Mock
    private SalesRecordRepository salesRecordRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SalesRecordMapper salesRecordMapper;

    @InjectMocks
    private SalesRecordService salesRecordService;

    private Product product;
    private SalesRecord salesRecord;
    private SalesRecordDTO salesRecordDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Cappuccino");
        product.setCategory("Bebidas");
        product.setPrice(BigDecimal.valueOf(8.50));
        product.setUnitOfMeasure("ml");
        product.setIsActive(true);

        salesRecord = new SalesRecord();
        salesRecord.setId(1L);
        salesRecord.setProduct(product);
        salesRecord.setSaleDate(LocalDate.now());
        salesRecord.setQuantitySold(5);
        salesRecord.setDayOfWeek("MONDAY");

        salesRecordDTO = new SalesRecordDTO();
        salesRecordDTO.setId(1L);
        salesRecordDTO.setProductId(1L);
        salesRecordDTO.setProductName("Cappuccino");
        salesRecordDTO.setSaleDate(LocalDate.now());
        salesRecordDTO.setQuantitySold(5);
        salesRecordDTO.setDayOfWeek("MONDAY");
    }

    @Test
    @DisplayName("Deve listar todos os registros de venda")
    void testFindAll() {
        when(salesRecordRepository.findAll()).thenReturn(List.of(salesRecord));
        when(salesRecordMapper.toDTO(salesRecord)).thenReturn(salesRecordDTO);

        List<SalesRecordDTO> result = salesRecordService.findAll();

        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .contains(salesRecordDTO);

        verify(salesRecordRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar registro por ID com sucesso")
    void testFindByIdSuccess() {
        when(salesRecordRepository.findById(1L)).thenReturn(Optional.of(salesRecord));
        when(salesRecordMapper.toDTO(salesRecord)).thenReturn(salesRecordDTO);

        SalesRecordDTO result = salesRecordService.findById(1L);

        assertThat(result).isEqualTo(salesRecordDTO);
        verify(salesRecordRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando registro não encontrado")
    void testFindByIdNotFound() {
        when(salesRecordRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> salesRecordService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Registro não encontrado com id: 999");
    }

    @Test
    @DisplayName("Deve buscar vendas por produto")
    void testFindByProduct() {
        when(salesRecordRepository.findByProductId(1L)).thenReturn(List.of(salesRecord));
        when(salesRecordMapper.toDTO(salesRecord)).thenReturn(salesRecordDTO);

        List<SalesRecordDTO> result = salesRecordService.findByProduct(1L);

        assertThat(result).isNotEmpty().hasSize(1);
        verify(salesRecordRepository, times(1)).findByProductId(1L);
    }

    @Test
    @DisplayName("Deve buscar vendas por período")
    void testFindByPeriod() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(salesRecordRepository.findBySaleDateBetween(startDate, endDate))
                .thenReturn(List.of(salesRecord));
        when(salesRecordMapper.toDTO(salesRecord)).thenReturn(salesRecordDTO);

        List<SalesRecordDTO> result = salesRecordService.findByPeriod(startDate, endDate);

        assertThat(result).isNotEmpty();
        verify(salesRecordRepository, times(1)).findBySaleDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Deve buscar vendas por dia da semana")
    void testFindByDayOfWeek() {
        when(salesRecordRepository.findByDayOfWeek("MONDAY"))
                .thenReturn(List.of(salesRecord));
        when(salesRecordMapper.toDTO(salesRecord)).thenReturn(salesRecordDTO);

        List<SalesRecordDTO> result = salesRecordService.findByDayOfWeek("MONDAY");

        assertThat(result).isNotEmpty();
        verify(salesRecordRepository, times(1)).findByDayOfWeek("MONDAY");
    }

    @Test
    @DisplayName("Deve calcular total vendido por produto")
    void testGetTotalSoldByProduct() {
        when(salesRecordRepository.sumQuantitySoldByProductId(1L)).thenReturn(150);

        Integer result = salesRecordService.getTotalSoldByProduct(1L);

        assertThat(result).isEqualTo(150);
        verify(salesRecordRepository, times(1)).sumQuantitySoldByProductId(1L);
    }

    @Test
    @DisplayName("Deve retornar 0 quando não há vendas")
    void testGetTotalSoldByProductZero() {
        when(salesRecordRepository.sumQuantitySoldByProductId(1L)).thenReturn(null);

        Integer result = salesRecordService.getTotalSoldByProduct(1L);

        assertThat(result).isZero();
    }

    @Test
    @DisplayName("Deve salvar novo registro de venda com sucesso")
    void testSaveSuccess() {
        when(salesRecordMapper.toEntity(salesRecordDTO)).thenReturn(salesRecord);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(salesRecordRepository.save(any(SalesRecord.class))).thenReturn(salesRecord);
        when(salesRecordMapper.toDTO(salesRecord)).thenReturn(salesRecordDTO);

        SalesRecordDTO result = salesRecordService.save(salesRecordDTO);

        assertThat(result).isEqualTo(salesRecordDTO);
        verify(salesRecordRepository, times(1)).save(any(SalesRecord.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao salvar com produto inexistente")
    void testSaveProductNotFound() {
        when(salesRecordMapper.toEntity(salesRecordDTO)).thenReturn(salesRecord);
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        SalesRecordDTO invalidDTO = new SalesRecordDTO();
        invalidDTO.setProductId(999L);

        assertThatThrownBy(() -> salesRecordService.save(invalidDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado com id: 999");
    }

    @Test
    @DisplayName("Deve deletar registro com sucesso")
    void testDeleteSuccess() {
        when(salesRecordRepository.existsById(1L)).thenReturn(true);

        salesRecordService.delete(1L);

        verify(salesRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar registro inexistente")
    void testDeleteNotFound() {
        when(salesRecordRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> salesRecordService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Registro não encontrado com id: 999");
    }

    @Test
    @DisplayName("Deve buscar entidades por produto")
    void testFindEntitiesByProduct() {
        when(salesRecordRepository.findByProductId(1L)).thenReturn(List.of(salesRecord));

        List<SalesRecord> result = salesRecordService.findEntitiesByProduct(1L);

        assertThat(result).isNotEmpty().hasSize(1);
        assertThat(result.get(0).getProduct().getId()).isEqualTo(1L);
    }
}
