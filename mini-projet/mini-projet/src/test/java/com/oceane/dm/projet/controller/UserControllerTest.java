package com.oceane.dm.projet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceane.dm.projet.model.RegisterRequest;
import com.oceane.dm.projet.model.dto.TwoFactorUserDTO;
import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.models.repository.TwoFactorUserRepository;
import com.oceane.dm.projet.service.AuthService;
import com.oceane.dm.projet.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private TwoFactorUserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testGetCurrentUser() throws Exception {
        TwoFactorUser user = new TwoFactorUser();
        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/api/users/current"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ }")); // Add specific user details as needed

        verify(userService, times(1)).getCurrentUser();
    }

    @Test
    public void testRegisterUser() throws Exception {
        TwoFactorUser user = new TwoFactorUser();

        // Simuler la méthode createUser avec le rôle correct
        when(userService.createUser(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                eq(TwoFactorUser.Role.USER_ROLE))) // Utiliser USER_ROLE au lieu de USER
                .thenReturn(user);

        // Effectuer la requête avec le rôle correct dans le corps de la requête
        mockMvc.perform(post("/api/users/register")
                        .contentType("application/json")
                        .content("{\"identifier\":\"test\",\"password\":\"password123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"company\":\"TestCo\",\"role\":\"USER_ROLE\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{}")); // Adapter selon les détails attendus

        // Vérifier que createUser a été appelée une fois avec les bons paramètres
        verify(userService, times(1)).createUser(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                eq(TwoFactorUser.Role.USER_ROLE)); // Utiliser USER_ROLE au lieu de USER
    }



    @Test
    public void testActivate2FA() throws Exception {
        // Setup mock user
        TwoFactorUser user = new TwoFactorUser();
        user.setEmail("test@example.com"); // Ensure the email is set

        // Mock repository and service calls
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(authService.generateSecretKey()).thenReturn("secretKey");
        when(authService.getQRCode(anyString(), anyString())).thenReturn("qrCode");

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/activate-2fa")
                        .param("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.secret").value("secretKey"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.qrCode").value("qrCode"));

        // Verify interactions
        verify(userRepository, times(1)).findById(anyLong());
        verify(authService, times(1)).generateSecretKey();
        verify(authService, times(1)).getQRCode(anyString(), anyString());
    }

   /* @Test
    public void testDeactivate2FA() throws Exception {
        // Préparation des données
        TwoFactorUser user = new TwoFactorUser();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userService.updateis2Fa(any(TwoFactorUser.class))).thenReturn(user);  // Mock de updateis2Fa

        // Exécution de la requête
        mockMvc.perform(post("/deactivate-2fa")
                        .param("userId", "1"))
                .andExpect(status().isOk());

        // Vérification des interactions
        verify(userRepository, times(1)).findById(anyLong());  // Vérifie findById
        verify(userService, times(1)).updateis2Fa(any(TwoFactorUser.class));  // Vérifie updateis2Fa
        verify(userRepository, times(1)).save(any(TwoFactorUser.class));  // Vérifie save
        verifyNoMoreInteractions(userRepository, userService);  // Vérifie qu'il n'y a pas d'autres interactions
    }*/




    @Test
    public void testUpdateUser() throws Exception {
        // Création d'un objet TwoFactorUserDTO avec tous les champs requis
        TwoFactorUserDTO userDTO = new TwoFactorUserDTO();
        userDTO.setIdentifier("testIdentifier");
        userDTO.setPassword("testPassword");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setCaptchaSession("testCaptchaSession");
        userDTO.setCaptchaAnswer("testCaptchaAnswer");

        TwoFactorUser user = new TwoFactorUser();
        when(userService.updateUser(anyLong(), any(TwoFactorUserDTO.class)))
                .thenReturn(Optional.of(user));

        // Effectuer la requête avec tous les champs requis
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/update-user")
                        .param("userId", "1")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Vérifier que la méthode a été appelée une fois
        verify(userService, times(1)).updateUser(anyLong(), any(TwoFactorUserDTO.class));
    }


    @Test
    public void testGetAllUsers() throws Exception {
        TwoFactorUser user = new TwoFactorUser();
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists()); // Add specific user details as needed

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(anyLong());
    }
}
