package com.cafe.forecast.mapper;

import com.cafe.forecast.dto.SalesRecordDTO;
import com.cafe.forecast.model.SalesRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalesRecordMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    SalesRecordDTO toDTO(SalesRecord salesRecord);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(target = "product.name", ignore = true)
    SalesRecord toEntity(SalesRecordDTO salesRecordDTO);
}
