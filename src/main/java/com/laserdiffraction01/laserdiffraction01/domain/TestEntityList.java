package com.laserdiffraction01.laserdiffraction01.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEntityList {
    List<TestEntity> listTestEntitiesInClass = new ArrayList<>();
}
