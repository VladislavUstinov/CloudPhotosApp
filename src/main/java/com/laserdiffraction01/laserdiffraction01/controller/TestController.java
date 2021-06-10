package com.laserdiffraction01.laserdiffraction01.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.TestEntity;
import com.laserdiffraction01.laserdiffraction01.domain.TestEntityList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

        List<TestEntity> testEntityList = new ArrayList<>();
        testEntityList.add (new TestEntity("name1", 0L, true));
        testEntityList.add (new TestEntity("name2", 1L, false));
        testEntityList.add (new TestEntity("name3", 2L, false));

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
        log.debug ("ContentController.testPost, testEntity name = " + testEntity.getName());
        log.debug ("ContentController.testPost, testEntity isOk = " + testEntity.getIsOk());

        log.debug ("testpost POST");

        return "redirect:test";
    }

    @PostMapping("testpostBooleanList")
    public String testPostBooleanListFirst (Model model, @ModelAttribute("testEntityList") TestEntityList testEntityList){
        log.debug ("ContentController.testPostBooleanList FIRST");
        log.debug ("testEntityList size = " + testEntityList.getListTestEntitiesInClass().size());
        for (TestEntity cur : testEntityList.getListTestEntitiesInClass())
            log.debug ("Current entity name = " + cur.getName() + " ; isOk = " + cur.getIsOk());

        return "redirect:test";
    }

    @PostMapping("testpostBooleanListSecond")
    public String testPostBooleanListSecond (Model model, @ModelAttribute("testEntityList") TestEntityList testEntityList){
        log.debug ("ContentController.testPostBooleanList SECOND");
        log.debug ("testEntityList size = " + testEntityList.getListTestEntitiesInClass().size());
        for (TestEntity cur : testEntityList.getListTestEntitiesInClass())
            log.debug ("Current entity name = " + cur.getName() + " ; isOk = " + cur.getIsOk());

        return "redirect:test";
    }
}

/*



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