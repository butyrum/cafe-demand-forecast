package com.cafe.forecast.service;

import com.cafe.forecast.dto.ProductDTO;
import com.cafe.forecast.exception.ResourceNotFoundException; // ✅ IMPORT ADICIONADO
import com.cafe.forecast.mapper.ProductMapper;
import com.cafe.forecast.model.Product;
import com.cafe.forecast.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDTO> findAll() {
        log.info("Listando todos os produtos");
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public List<ProductDTO> findAllActive() {
        log.info("Listando todos os produtos ativos");
        return productRepository.findByIsActiveTrue()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public List<ProductDTO> findByCategory(String category) {
        log.info("Listando produtos da categoria: {}", category);
        return productRepository.findByCategory(category)
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public ProductDTO findById(Long id) {
        log.info("Buscando produto com ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Produto não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Produto não encontrado com id: " + id);
                });
        return productMapper.toDTO(product);
    }

    public ProductDTO save(ProductDTO productDTO) {
        log.info("Criando novo produto: {}", productDTO.getName());
        Product product = productMapper.toEntity(productDTO);
        Product saved = productRepository.save(product);
        log.info("Produto criado com sucesso. ID: {}", saved.getId());
        return productMapper.toDTO(saved);
    }

    public ProductDTO update(Long id, ProductDTO productDTO) {
        log.info("Atualizando produto com ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Produto não encontrado para atualização. ID: {}", id);
                    return new ResourceNotFoundException("Produto não encontrado com id: " + id);
                });
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setUnitOfMeasure(productDTO.getUnitOfMeasure());
        product.setIsActive(productDTO.getIsActive());
        Product updated = productRepository.save(product);
        log.info("Produto atualizado com sucesso. ID: {}", id);
        return productMapper.toDTO(updated);
    }

    public void delete(Long id) {
        log.info("Desativando produto com ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Produto não encontrado para desativação. ID: {}", id);
                    return new ResourceNotFoundException("Produto não encontrado com id: " + id);
                });
        product.setIsActive(false);
        productRepository.save(product);
        log.info("Produto desativado com sucesso. ID: {}", id);
    }
}
