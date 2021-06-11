package com.laserdiffraction01.laserdiffraction01.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEntityList {
    private List<TestEntity> listTestEntitiesInClass = new ArrayList<>();


    /*
    //commented version with Set. I tried to make next to be a pointer to current entity in a Set
    //but Thymeleaf engine didn't bind my set object to ModelAttribute when submitting the form
    private Set<TestEntity> listTestEntitiesInClass = new HashSet<>();

    public TestEntity getByIndex (int index) {
        if (index < 0 || listTestEntitiesInClass == null || listTestEntitiesInClass.size() == 0 ||
            index >= listTestEntitiesInClass.size())
            return null;

        Iterator iter =  listTestEntitiesInClass.iterator();

        TestEntity result = null;
        for (int i = 0; i <= index; i ++)
            result = (TestEntity) iter.next();

        return result;
    }


    private Iterator iter = listTestEntitiesInClass.iterator();

    private TestEntity next = new TestEntity();

    public TestEntityList(Set<TestEntity> listTestEntitiesInClass) {
        this.listTestEntitiesInClass = listTestEntitiesInClass;
        iter = listTestEntitiesInClass.iterator();
    }

    private int waitingIndex = 0;

    public TestEntity getNextItem(){
        System.out.println("getNext () waitingIndex = " + waitingIndex);
        waitingIndex++;

        if (listTestEntitiesInClass==null || listTestEntitiesInClass.size()==0)
            return new TestEntity();

        if (iter == null)
            iter = listTestEntitiesInClass.iterator();

        if (iter.hasNext()==false) {
            iter = listTestEntitiesInClass.iterator();
        }
        next = (TestEntity) iter.next();

        return next;
    }*/
}
