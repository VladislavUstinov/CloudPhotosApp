package com.laserdiffraction01.laserdiffraction01.controller;

import com.laserdiffraction01.laserdiffraction01.DTO.FoldersPhotosDTO;
import com.laserdiffraction01.laserdiffraction01.service.FolderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FullScreenControllerTest {

    @Mock
    FolderService folderService;

    FullScreenController fullScreenController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        fullScreenController = new FullScreenController(folderService);


        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(fullScreenController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void goFullScreenTest() throws Exception {
        mockMvc.perform(post("/photos/goFullScreen/currentFolder/1/selectedPhoto/1")
                .flashAttr("foldersPhotosDTO", new FoldersPhotosDTO()))
            .andExpect(model().attributeExists("selectedPhotoId"))
            .andExpect(model().attributeExists("currentFolderId"))
            .andExpect(model().attributeExists("foldersPhotosDTO"))
            .andExpect(status().isOk())
            .andExpect(view().name("fullscreen"));
    }
}