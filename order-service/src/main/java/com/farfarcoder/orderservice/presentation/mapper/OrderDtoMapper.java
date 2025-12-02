package com.farfarcoder.orderservice.presentation.mapper;

import com.farfarcoder.orderservice.business.model.OrderModel;
import com.farfarcoder.orderservice.presentation.dto.OrderRequest;
import com.farfarcoder.orderservice.presentation.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderDtoMapper {

    OrderDtoMapper INSTANCE = Mappers.getMapper(OrderDtoMapper.class);

    // Request -> Model
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderModel toModel(OrderRequest request);

    // Model -> Response
    OrderResponse toResponse(OrderModel model);
}
