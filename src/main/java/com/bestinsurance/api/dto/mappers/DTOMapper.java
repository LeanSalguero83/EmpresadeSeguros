package com.bestinsurance.api.dto.mappers;
@FunctionalInterface
public interface DTOMapper<Input, Output> {
    Output map(Input obj);
}
