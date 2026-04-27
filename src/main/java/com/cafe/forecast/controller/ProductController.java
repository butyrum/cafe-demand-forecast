package com.cafe.forecast.controller;

import com.cafe.forecast.dto.ApiResponse;
import com.cafe.forecast.dto.ProductDTO;
import com.cafe.forecast.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Gerenciamento de produtos do cardápio")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> findAll() {
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok(ApiResponse.ok(products, "Produtos listados com sucesso"));
    }

    @GetMapping("/active")
    @Operation(summary = "Listar produtos ativos")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> findAllActive() {
        List<ProductDTO> products = productService.findAllActive();
        return ResponseEntity.ok(ApiResponse.ok(products, "Produtos ativos listados com sucesso"));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Buscar produtos por categoria")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> findByCategory(@PathVariable String category) {
        List<ProductDTO> products = productService.findByCategory(category);
        return ResponseEntity.ok(ApiResponse.ok(products,
                String.format("Produtos da categoria '%s' listados com sucesso", category)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ApiResponse<ProductDTO>> findById(@PathVariable Long id) {
        ProductDTO product = productService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(product, "Produto encontrado"));
    }

    @PostMapping
    @Operation(summary = "Criar novo produto")
    public ResponseEntity<ApiResponse<ProductDTO>> save(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(created, "Produto criado com sucesso"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ApiResponse<ProductDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updated = productService.update(id, productDTO);
        return ResponseEntity.ok(ApiResponse.ok(updated, "Produto atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar produto")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null, "Produto removido com sucesso"));
    }
}
