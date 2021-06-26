package com.cloudphotosapp.cloudphotosapp.controller;

import com.cloudphotosapp.cloudphotosapp.domain.FilePhoto;
import com.cloudphotosapp.cloudphotosapp.domain.Folder;
import com.cloudphotosapp.cloudphotosapp.domain.User;
import com.cloudphotosapp.cloudphotosapp.repository.FilePhotoRepository;
import com.cloudphotosapp.cloudphotosapp.repository.FolderRepository;
import com.cloudphotosapp.cloudphotosapp.repository.RoleRepository;
import com.cloudphotosapp.cloudphotosapp.repository.UserRepository;
import com.cloudphotosapp.cloudphotosapp.service.CopyPasteService;
import com.cloudphotosapp.cloudphotosapp.service.FolderService;
import com.cloudphotosapp.cloudphotosapp.service.ImageService;
import com.cloudphotosapp.cloudphotosapp.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//see https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication for details

public class ContentControllerTest {
    @Mock
    ImageService imageService;

    @Mock
    FilePhotoRepository filePhotoRepository;

    @Mock
    UserService userService;

    @Mock
    RoleRepository roleRepository;

    @Mock
    FolderService folderService;

    @Mock
    UserRepository userRepository;

    @Mock
    FolderRepository folderRepository;

    @Mock
    CopyPasteService copyPasteService;

    ContentController contentController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        contentController = new ContentController(userService, roleRepository, folderService, imageService, filePhotoRepository, userRepository, folderRepository, copyPasteService);


        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(contentController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getPhotosAndUsersRootTest () throws Exception
    {

        //given

        Folder rootFolder = new Folder();

        when(folderService.getRootFolderByUsername(anyString())).thenReturn(rootFolder);

        when(filePhotoRepository.findByName(anyString())).thenReturn(new FilePhoto());

        when(userService.loadUserByUsername(anyString())).thenReturn(new User("user", "pass", "pass"));

        mockMvc.perform(MockMvcRequestBuilders.get("/photos"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("foldersPhotosDTO", "folders", "photos","currentFolder"))
                .andExpect(view().name("photos"));

        verify(folderService, times(1)).getRootFolderByUsername(anyString());
        verify(filePhotoRepository, times(2)).findByName(anyString());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getCurrentFolderTest () throws Exception
    {

        //given

        Folder folder = new Folder();
        User user = new User("user", "pass", "pass");
        folder.addOwner(user);

        when(folderService.getFolderById(anyLong())).thenReturn(folder);

        when(filePhotoRepository.findByName(anyString())).thenReturn(new FilePhoto());

        when(userService.loadUserByUsername(anyString())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/photos/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("foldersPhotosDTO", "folders", "photos","currentFolder"))
                .andExpect(view().name("photos"));

        verify(folderService, times(1)).getFolderById(anyLong());
        verify(filePhotoRepository, times(1)).findByName(anyString());
    }

    @Test
    @WithMockUser(roles="USER")
    public void startSearchingTest () throws Exception{

        //given

        Folder folder = new Folder();
        User user = new User("user", "pass", "pass");
        folder.addOwner(user);

        when(folderService.getFolderById(anyLong())).thenReturn(folder);

        when(filePhotoRepository.findByName(anyString())).thenReturn(new FilePhoto());

        when(userService.loadUserByUsername(anyString())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/photos/startSearching/currentFolder/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("foldersPhotosDTO", "folders", "photos","currentFolder",
                        "SearchingIsBeingDoneNow"))
                .andExpect(view().name("photos"));

        verify(folderService, times(1)).getFolderById(anyLong());
        verify(filePhotoRepository, times(1)).findByName(anyString());

        //photos/startSearching/currentFolder/{currentFolderId}
        //SearchingIsBeingDoneNow
    }
    /*
    @Test
    @WithMockUser(roles = "USER")
    public String startSearching () throws Exception{



        mockMvc.perform(post("photos/startSearching/currentFolder/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("foldersPhotosDTO", "folders", "photos","currentFolder"))
                .andExpect(view().name("photos"));

    }
*/


    }

/*
    @GetMapping("/photos")
    String getPhotosAndUsersRoot(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Folder root = folderService.getRootFolderByUsername (currentPrincipalName);

        if (root == null)
            return "photos";//todo add error


        FilePhoto photoPen = filePhotoRepository.findByName(LaserDiffractionBootStrap.PREDEFINED_STATIC_PICTURE_EDIT_PEN);
        if (photoPen == null)
            log.error("DID NOT LOAD photoPen in ContentController.get..Root");

        log.debug("photoPen name = " + photoPen.getName());

        FilePhoto staticPictureEditPen = filePhotoRepository.findByName(LaserDiffractionBootStrap.PREDEFINED_STATIC_PICTURE_EDIT_PEN);

        if (staticPictureEditPen == null)
            log.error("staticPictureEditPen was NOT LOADED from filePhotoRepository in ContentController");

        FoldersPhotosDTO foldersPhotosDTO = new FoldersPhotosDTO(new ArrayList<>(root.getFilePhotos()),
                                                            new ArrayList<>(root.getSubFolders()), getSharedWithMeFoldersToShow (), staticPictureEditPen);

        model.addAttribute("foldersPhotosDTO", foldersPhotosDTO);
        model.addAttribute("folders", root.getSubFolders());
        model.addAttribute("photos", root.getFilePhotos());
        model.addAttribute("currentFolder", root);

        addPhotosTableToModel (model, root.getFilePhotos());

        return "photos";
    }

 */