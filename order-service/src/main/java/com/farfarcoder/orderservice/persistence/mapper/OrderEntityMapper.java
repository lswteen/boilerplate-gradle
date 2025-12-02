package com.farfarcoder.orderservice.persistence.mapper;

import com.farfarcoder.orderservice.business.model.OrderModel;
import com.farfarcoder.orderservice.persistence.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {

    OrderEntityMapper INSTANCE = Mappers.getMapper(OrderEntityMapper.class);

    // Model -> Entity
    @Mapping(target = "status", source = "status")
    Order toEntity(OrderModel model);

    // Entity -> Model
    @Mapping(target = "status", source = "status")
    OrderModel toModel(Order entity);
}
