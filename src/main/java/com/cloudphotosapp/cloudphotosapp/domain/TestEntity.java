package com.cloudphotosapp.cloudphotosapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {

    String name = "";
    Long id = 1L;
    Boolean isOk = true;

    NestedTestEntity nestedTestEntity = new NestedTestEntity();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity that = (TestEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(id, that.id) && Objects.equals(isOk, that.isOk) && Objects.equals(nestedTestEntity, that.nestedTestEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, isOk, nestedTestEntity);
    }
}
