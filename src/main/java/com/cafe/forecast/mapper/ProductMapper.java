package com.cafe.forecast.mapper;

import com.cafe.forecast.dto.ProductDTO;
import com.cafe.forecast.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);

    Product toEntity(ProductDTO productDTO);
}
