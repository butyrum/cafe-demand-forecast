package com.cafe.forecast.service;

import com.cafe.forecast.exception.ResourceNotFoundException;
import com.cafe.forecast.dto.SalesRecordDTO;
import com.cafe.forecast.mapper.SalesRecordMapper;
import com.cafe.forecast.model.SalesRecord;
import com.cafe.forecast.repository.ProductRepository;
import com.cafe.forecast.repository.SalesRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesRecordService {

    private final SalesRecordRepository salesRecordRepository;
    private final ProductRepository productRepository;
    private final SalesRecordMapper salesRecordMapper;

    public List<SalesRecordDTO> findAll() {
        log.info("Listando todos os registros de venda");
        return salesRecordRepository.findAll()
                .stream()
                .map(salesRecordMapper::toDTO)
                .toList();
    }

    public SalesRecordDTO findById(Long id) {
        log.info("Buscando registro de venda com ID: {}", id);
        SalesRecord record = salesRecordRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Registro não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Registro não encontrado com id: " + id);
                });
        return salesRecordMapper.toDTO(record);
    }

    public List<SalesRecordDTO> findByProduct(Long productId) {
        log.info("Listando vendas do produto com ID: {}", productId);
        return salesRecordRepository.findByProductId(productId)
                .stream()
                .map(salesRecordMapper::toDTO)
                .toList();
    }

    public List<SalesRecordDTO> findByPeriod(LocalDate startDate, LocalDate endDate) {
        log.info("Listando vendas entre {} e {}", startDate, endDate);
        return salesRecordRepository.findBySaleDateBetween(startDate, endDate)
                .stream()
                .map(salesRecordMapper::toDTO)
                .toList();
    }

    public List<SalesRecordDTO> findByProductAndPeriod(Long productId, LocalDate startDate, LocalDate endDate) {
        log.info("Listando vendas do produto {} entre {} e {}", productId, startDate, endDate);
        return salesRecordRepository.findByProductIdAndSaleDateBetween(productId, startDate, endDate)
                .stream()
                .map(salesRecordMapper::toDTO)
                .toList();
    }

    public List<SalesRecordDTO> findByDayOfWeek(String dayOfWeek) {
        log.info("Listando vendas do dia da semana: {}", dayOfWeek);
        return salesRecordRepository.findByDayOfWeek(dayOfWeek.toUpperCase())
                .stream()
                .map(salesRecordMapper::toDTO)
                .toList();
    }

    public Integer getTotalSoldByProduct(Long productId) {
        log.info("Calculando total vendido do produto com ID: {}", productId);
        Integer total = salesRecordRepository.sumQuantitySoldByProductId(productId);
        return total != null ? total : 0;
    }

    public SalesRecordDTO save(SalesRecordDTO salesRecordDTO) {
        log.info("Registrando nova venda - Produto ID: {}, Quantidade: {}, Data: {}",
                salesRecordDTO.getProductId(), salesRecordDTO.getQuantitySold(), salesRecordDTO.getSaleDate());

        SalesRecord record = salesRecordMapper.toEntity(salesRecordDTO);
        record.setProduct(productRepository.findById(salesRecordDTO.getProductId())
                .orElseThrow(() -> {
                    log.warn("Produto não encontrado para registro de venda. ID: {}", salesRecordDTO.getProductId());
                    return new ResourceNotFoundException("Produto não encontrado com id: " + salesRecordDTO.getProductId());
                }));

        SalesRecord saved = salesRecordRepository.save(record);
        log.info("Venda registrada com sucesso. ID: {}", saved.getId());
        return salesRecordMapper.toDTO(saved);
    }

    public void delete(Long id) {
        log.info("Deletando registro de venda com ID: {}", id);
        if (!salesRecordRepository.existsById(id)) {
            log.warn("Registro não encontrado para deleção. ID: {}", id);
            throw new ResourceNotFoundException("Registro não encontrado com id: " + id);
        }
        salesRecordRepository.deleteById(id);
        log.info("Registro deletado com sucesso. ID: {}", id);
    }

    public List<SalesRecord> findEntitiesByProduct(Long productId) {
        return salesRecordRepository.findByProductId(productId);
    }
}
