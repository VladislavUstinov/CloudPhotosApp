package com.laserdiffraction01.laserdiffraction01.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {

    String name = "";
    Long id = 1L;
    Boolean isOk = true;
}
