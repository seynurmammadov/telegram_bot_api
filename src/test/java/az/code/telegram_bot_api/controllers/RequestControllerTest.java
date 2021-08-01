package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.SPRING_TEST_DATA;
import az.code.telegram_bot_api.exceptions.UserRequestNotFound;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.services.interfaces.RequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Objects;

import static az.code.telegram_bot_api.SPRING_TEST_DATA.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class RequestControllerTest {
    SPRING_TEST_DATA data = new SPRING_TEST_DATA();

    public static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RequestService requestService;

    @BeforeAll
    static void start() {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }


    @Test
    @DisplayName("RequestController - get all without status - HttpStatus.200")
    void getAll() throws Exception {
        UserRequest response = data.generateUserRequest();
        when(requestService.getAll(any(), any())).thenReturn(Collections.singletonList(response));
        mockMvc.perform(get(REQUEST_URL)
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(mapper.writeValueAsString(Collections.singletonList(response))))
                .andExpect(status().isOk())
                .andDo(print());
        verify(requestService, times(1)).getAll(any(), any());
        verify(requestService, times(0)).getAll(any(), any(), any());
    }

    @Test
    @DisplayName("RequestController - get all with status - HttpStatus.200")
    void getAll_test2() throws Exception {
        UserRequest response = data.generateUserRequest();
        when(requestService.getAll(any(), any(), any())).thenReturn(Collections.singletonList(response));
        mockMvc.perform(get(REQUEST_URL)
                .requestAttr("user", data.generateUserTokenDTO())
                .param("status", "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(Collections.singletonList(response))))
                .andDo(print());
        verify(requestService, times(0)).getAll(any(), any());
        verify(requestService, times(1)).getAll(any(), any(), any());
    }

    @Test
    @DisplayName("RequestController - get all archived - HttpStatus.200")
    void archived() throws Exception {
        UserRequest response = data.generateUserRequest();
        when(requestService.getAllArchived(any(), any())).thenReturn(Collections.singletonList(response));
        mockMvc.perform(get(REQUEST_URL + "/archived")
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(Collections.singletonList(response))))
                .andDo(print());
    }

    @Test
    @DisplayName("RequestController - archive request - HttpStatus.200")
    void archiveRequest() throws Exception {
        when(requestService.archiveRequest(any(), any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(put(REQUEST_URL + "/archive/{userRequestId}", 5)
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("RequestController - archive request - HttpStatus.404 UserRequestNotFound()")
    void archiveRequest_test2() throws Exception {
        when(requestService.archiveRequest(any(), any())).thenThrow(new UserRequestNotFound());
        mockMvc.perform(put(REQUEST_URL + "/archive/{userRequestId}", 5)
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserRequestNotFound))
                .andExpect(result -> assertEquals("User request not found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("RequestController - unarchive request - HttpStatus.200")
    void unarchiveRequest() throws Exception {
        when(requestService.unarchiveRequest(any(), any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(put(REQUEST_URL + "/unarchive/{userRequestId}", 5)
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("RequestController - unarchive request - HttpStatus.404 UserRequestNotFound()")
    void unarchiveRequest_test2() throws Exception {
        when(requestService.unarchiveRequest(any(), any())).thenThrow(new UserRequestNotFound());
        mockMvc.perform(put(REQUEST_URL + "/unarchive/{userRequestId}", 5)
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserRequestNotFound))
                .andExpect(result -> assertEquals("User request not found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("RequestController - delete request - HttpStatus.200")
    void deleteRequest() throws Exception {
        when(requestService.deleteArchived(any(), any())).thenReturn(HttpStatus.OK);
        mockMvc.perform(delete(REQUEST_URL + "/archived/{userRequestId}", 5)
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("RequestController - delete request - HttpStatus.404 UserRequestNotFound()")
    void deleteRequest_test2() throws Exception {
        when(requestService.deleteArchived(any(), any())).thenThrow(new UserRequestNotFound());
        mockMvc.perform(delete(REQUEST_URL + "/archived/{userRequestId}", 5)
                .requestAttr("user", data.generateUserTokenDTO())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserRequestNotFound))
                .andExpect(result -> assertEquals("User request not found!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()))
                .andDo(print());
    }

}