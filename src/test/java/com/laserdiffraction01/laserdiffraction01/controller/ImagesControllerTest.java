package com.laserdiffraction01.laserdiffraction01.controller;

import com.laserdiffraction01.laserdiffraction01.repository.FilePhotoRepository;
import com.laserdiffraction01.laserdiffraction01.service.ImageService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImagesControllerTest {
    @Mock
    ImageService imageService;

    @Mock
    FilePhotoRepository filePhotoRepository;

    ImagesController imagesController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        imagesController = new ImagesController(filePhotoRepository, imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imagesController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void handleImagePostTest() throws Exception {

        //given

        MockMultipartFile file1 = new MockMultipartFile("imagefile", "filename1.jpg", "image/jpeg", "some xml1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("imagefile", "filename2.jpg", "image/jpeg", "some xml2".getBytes());


        mockMvc.perform(multipart("/photos/1/imageuploadform/").file(file1).file(file2))
                .andExpect(status().is3xxRedirection());

        verify(imageService, times(2)).saveImageFile(anyLong(), any());


    }
}

/*
                MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                        "Spring Framework Guru".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyLong(), any());
         */

        /*
            @PostMapping("photos/{folderId}/imageuploadform/")
    public String handleImagePost(@PathVariable String folderId, @RequestParam("imagefile") List<MultipartFile> files){

        for (MultipartFile file : files) {
            log.debug("ContentController.handleImagePost. Image name = " + file.getOriginalFilename());
            if (file.getOriginalFilename().isEmpty() == false)
                imageService.saveImageFile(Long.valueOf(folderId), file);

        }
        return "redirect:/photos/" + folderId;
    }
         */
        /*
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        //when
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(anyLong());*/

/*
    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void getImageForm() throws Exception {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        //when
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(anyLong());

    }
 */