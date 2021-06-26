package com.cloudphotosapp.cloudphotosapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cloudphotosapp.cloudphotosapp.domain.NestedTestEntity;
import com.cloudphotosapp.cloudphotosapp.domain.TestEntity;
import com.cloudphotosapp.cloudphotosapp.domain.TestEntityList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
public class TestController {

    ///////////// working with testFilePhoto

    @GetMapping("/test")
    public String initialTest (Model model) {

        TestEntity testEntity = new TestEntity();
        testEntity.setName("test");
        testEntity.setId(123L);
        model.addAttribute("testEntity", testEntity);
        model.addAttribute("jsonObjectMapper", new ObjectMapper());

        Set<TestEntity> testEntitySet = new HashSet<>();
        testEntitySet.add (new TestEntity("name0", 0L, true, new NestedTestEntity("nested1", 1L)));
        testEntitySet.add (new TestEntity("name1", 1L, false, new NestedTestEntity("nested2", 2L)));
        testEntitySet.add (new TestEntity("name2", 2L, false, new NestedTestEntity("nested3", 3L)));

        List<TestEntity> testEntityList = new ArrayList<TestEntity>(testEntitySet);

/*
        log.debug("testEntityList in getMapping /test:");
        Iterator iter = testEntityList.iterator();

        TestEntity cur = (TestEntity) iter.next();
        log.debug("cur name = " + cur.getName()  + " isOk = " + cur.getIsOk() + " id = " + cur.getId()
                + " hasNext = " + iter.hasNext());
        cur = (TestEntity) iter.next();
        log.debug("cur name = " + cur.getName()  + " isOk = " + cur.getIsOk() + " id = " + cur.getId()
                + " hasNext = " + iter.hasNext());
        cur = (TestEntity) iter.next();
        log.debug("cur name = " + cur.getName()  + " isOk = " + cur.getIsOk() + " id = " + cur.getId()
                + " hasNext = " + iter.hasNext());
*/
        TestEntityList TestEntityListObject = new TestEntityList(testEntityList);
        model.addAttribute("testEntityList", TestEntityListObject);

        return "test";
    }

    @GetMapping("/testGetJson")
    public String testGetJson (Model model, @RequestParam("testEntityJson") String testEntityJson){
        log.debug ("ContentController.testGetJson, testEntity json = " + testEntityJson);

        TestEntity testEntityResult = null;
        try {
            testEntityResult = new ObjectMapper().readValue(testEntityJson, TestEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.debug("Converted testFilePhotoResult: id = " + testEntityResult.getId() + " ; name = " + testEntityResult.getName());

        return "redirect:test";
    }

    @GetMapping("/testRequestParamName")
    public String testRequestParamName(@RequestParam("testEntityName") String testEntityName){
        log.debug ("ContentController.testRequestParamName, testEntityName = " + testEntityName);

        return "redirect:test";
    }

    @GetMapping("/testPathVarId/{id}/some")
    public String testPathVarId(@PathVariable("id") String id, Model model){
        log.debug ("ContentController.testPathVarId, testEntity id = " + id);

        return "redirect:/test";
    }

    @GetMapping(value="/testRestMapping", consumes = {"application/JSON"}) //application/x-www-form-urlencoded;charset=UTF-8
    public String testRestMapping (@RequestBody TestEntity testEntity){
        log.debug ("ContentController.testRestMapping, testEntity name = " + testEntity.getName());


        return "redirect:/test";
    }

    @PostMapping("testpost")
    public String testPost (Model model, @ModelAttribute("testEntity") TestEntity testEntity){
        log.debug ("ContentController.testPost, testEntity nested name = " + testEntity.getNestedTestEntity().getName());
        log.debug ("ContentController.testPost, testEntity nested id = " + testEntity.getNestedTestEntity().getId());
        log.debug ("ContentController.testPost, testEntity name = " + testEntity.getName());
        log.debug ("ContentController.testPost, testEntity isOk = " + testEntity.getIsOk());

        log.debug ("testpost POST");

        return "redirect:test";
    }

    @PostMapping("testpostBooleanList")
    public String testPostBooleanListFirst (Model model, @ModelAttribute("testEntityList") TestEntityList testEntityList){
        Set<TestEntity> set = new HashSet<>(testEntityList.getListTestEntitiesInClass());

        log.debug ("ContentController.testPostBooleanList FIRST");
        log.debug ("testEntityList size = " + set.size());
        for (TestEntity cur : set)
            log.debug ("Current entity name = " + cur.getName() + " ; isOk = " + cur.getIsOk() +
                    " ; id = " + cur.getId());

        return "redirect:test";
    }

    @PostMapping("testpostBooleanListSecond")
    public String testPostBooleanListSecond (Model model, @ModelAttribute("testEntityList") TestEntityList testEntityList){

        Set<TestEntity> set = new HashSet<>(testEntityList.getListTestEntitiesInClass());

        log.debug ("ContentController.testPostBooleanList SECOND");
        log.debug ("testEntityList size = " + set.size());
        for (TestEntity cur : set)
            log.debug ("Current entity name = " + cur.getName() + " ; isOk = " + cur.getIsOk() +
                    " ; id = " + cur.getId());

        return "redirect:test";
    }
}

/*

//Реально работает, как в примере https://www.baeldung.com/thymeleaf-list
    <form th:object="${testEntityList}" th:action="@{'/testpostBooleanList'}" th:method="post">
    <ul>
        <li th:each="entity, itemStat : *{listTestEntitiesInClass}">
                <input th:field="*{listTestEntitiesInClass[__${itemStat.index}__].name}" th:id="${#ids.seq('name')}">
                <input th:field="*{listTestEntitiesInClass[__${itemStat.index}__].id}" th:id="${#ids.seq('id')}">
                <input type="checkbox" th:field="*{listTestEntitiesInClass[__${itemStat.index}__].isOk}" th:id="${#ids.seq('isOk')}">
        </li>
    </ul>
        <input type="submit" name="Button One" class="button" value="First" />
        <input type="submit" th:formaction="@{'/testpostBooleanListSecond'}" name="Button Two" class="button" value="Second" />
    </form>

// Показывает содержимое множества Set под старым названием  listTestEntitiesInClass
// можно все ввести, мой итератор в getNextItem() работает
// но итоговый объект отправляется в ModelAttribute в Controller пустым.
// Я думал,ему нужен уникальный id и он тут есть - th:id="${#ids.seq('name')}" - не помогло
// Видимо, ему ныжны уникальные имена атрибутов, не только указатели, но и имена
// Это означает одно - строго необходимо оборачивать Set в List и затем обратно, иначе binding невозможен
    <form th:object="${testEntityList}" th:action="@{'/testpostBooleanList'}" th:method="post">
    <ul>
        <li th:each="entity, itemStat : *{listTestEntitiesInClass}">
            <div th:with="itemNext=${testEntityList.getNextItem()}">
                <input th:field="*{next.name}" th:id="${#ids.seq('name')}">
                <input th:field="*{next.id}" th:id="${#ids.seq('id')}">
                <input type="checkbox" th:field="*{next.isOk}" th:id="${#ids.seq('isOk')}">

            </div>

        </li>
    </ul>
        <input type="submit" name="Button One" class="button" value="First" />
        <input type="submit" th:formaction="@{'/testpostBooleanListSecond'}" name="Button Two" class="button" value="Second" />
    </form>

// Didn't work:
<form th:object="${testEntityList}" th:action="@{'/testpostBooleanList'}" th:method="post">
<ul>
<li th:each="entity : ${testEntityList}">
<input type="checkbox" th:field="*{testEntityList}" th:value="${entity}" />
<label th:for="${#ids.prev('testEntityList')}"
        th:text="#{${entity.getName()}}">Test Entity Name in List</label>
</li>
</ul>
<input type="submit" value="test post entity list 1" />
</form>

 */

/*
// works, but next is called too often. So really works only if we have one input. May add amount of inputs to getNext iterations
// The object didn't really bind - in Controller I have "testEntityList size = 0"
// этот Next вызывается где-то в 7 раз больше, чем надо, в том числе между input какие-то вложенные вызовы.
//Поэтому нереально сделать нормальный итератор. Вариант с th:with="itemNext=*{next} выдал
            <div>
                <input th:field="*{next.name}"></input>
                <input th:field="*{next.isOk}"></input>
                <input th:field="*{next.id}"></input>
            </div>
//pars exception nextItem is not known
            <div th:with="nextItem=${testEntityList.next}">
                <input th:field="${nextItem.name}"></input>
                <input th:type="checkbox" th:field="${nextItem.isOk}"></input>
                <input th:field="${nextItem.id}"></input>
            </div>
//exception - it wants property with getter and setter, but got a function call
                <input th:field="*{getByIndex(itemStat.index).name}"></input>
                <input th:type="checkbox" th:field="*{getByIndex(itemStat.index).isOk}"></input>
                <input th:field="*{getByIndex(itemStat.index).id}"></input>
//doesn't work. But if it would, we have no real binding to the object submitted in the form unless using * instead of $
                <input th:field="${testEntityList.getByIndex(itemStat.index).name}"></input>
                <input th:type="checkbox" th:field="${testEntityList.getByIndex(itemStat.index).isOk}"></input>
                <input th:field="${testEntityList.getByIndex(itemStat.index).id}"></input>
//
<input th:field="*{getByIndex(itemStat.index).name}"></input>
//__${myVar}__ is a good thing, but here we need a property getByIndex, not function
<input th:field="*{getByIndex(__${itemStat.index}__).name}"></input>

 */