package com.cafe.forecast.service;

import com.cafe.forecast.dto.ProductDTO;
import com.cafe.forecast.mapper.ProductMapper;
import com.cafe.forecast.model.Product;
import com.cafe.forecast.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Cappuccino");
        product.setCategory("Bebidas");
        product.setPrice(BigDecimal.valueOf(8.50));
        product.setUnitOfMeasure("ml");
        product.setIsActive(true);

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Cappuccino");
        productDTO.setCategory("Bebidas");
        productDTO.setPrice(BigDecimal.valueOf(8.50));
        productDTO.setUnitOfMeasure("ml");
        productDTO.setIsActive(true);
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void testFindAll() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        List<ProductDTO> result = productService.findAll();

        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .contains(productDTO);

        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar apenas produtos ativos")
    void testFindAllActive() {
        when(productRepository.findByIsActiveTrue()).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        List<ProductDTO> result = productService.findAllActive();

        assertThat(result).isNotEmpty().hasSize(1);
        verify(productRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Deve buscar produtos por categoria")
    void testFindByCategory() {
        when(productRepository.findByCategory("Bebidas")).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        List<ProductDTO> result = productService.findByCategory("Bebidas");

        assertThat(result).isNotEmpty();
        verify(productRepository, times(1)).findByCategory("Bebidas");
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void testFindByIdSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.findById(1L);

        assertThat(result).isEqualTo(productDTO);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando produto não encontrado")
    void testFindByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado com id: 999");

        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve salvar novo produto com sucesso")
    void testSaveSuccess() {
        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.save(productDTO);

        assertThat(result).isEqualTo(productDTO);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void testUpdateSuccess() {
        ProductDTO updateDTO = new ProductDTO();
        updateDTO.setName("Cappuccino Premium");
        updateDTO.setCategory("Bebidas Premium");
        updateDTO.setPrice(BigDecimal.valueOf(12.00));
        updateDTO.setUnitOfMeasure("ml");
        updateDTO.setIsActive(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.update(1L, updateDTO);

        assertThat(result).isNotNull();
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao atualizar produto inexistente")
    void testUpdateNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.update(999L, productDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado com id: 999");
    }

    @Test
    @DisplayName("Deve desativar produto com sucesso")
    void testDeleteSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.delete(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
        assertThat(product.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao desativar produto inexistente")
    void testDeleteNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.delete(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado com id: 999");
    }
}
