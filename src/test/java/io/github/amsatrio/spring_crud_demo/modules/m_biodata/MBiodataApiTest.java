package io.github.amsatrio.spring_crud_demo.modules.m_biodata;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.amsatrio.spring_crud_demo.modules.hospital.m_biodata.MBiodata;
import io.github.amsatrio.spring_crud_demo.modules.hospital.m_biodata.MBiodataRequest;
import io.github.amsatrio.spring_crud_demo.modules.hospital.m_biodata.MBiodataService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MBiodataApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MBiodataService mBiodataService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser // Simulates an authenticated user
    @DisplayName("GET /v1/m-biodata/{id} - Success")
    void getMBiodata_ShouldReturnData() throws Exception {
        // Arrange
        MBiodata mockData = new MBiodata();
        mockData.setId(1L);
        mockData.setFullname("John Doe");

        when(mBiodataService.getMBiodata(1L)).thenReturn(mockData);

        // Act & Assert
        mockMvc.perform(get("/v1/m-biodata/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullname").value("John Doe"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /v1/m-biodata/header - Success")
    void getMBiodataHeader_ShouldReturnMap() throws Exception {
        // This tests the Converter.modelToHeaderMap logic triggered by the API
        mockMvc.perform(get("/v1/m-biodata/header"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /v1/m-biodata - Success")
    void createMBiodata_ShouldReturnCreated() throws Exception {
        // Arrange
        MBiodataRequest request = new MBiodataRequest();
        // Set necessary fields for your request object here
        request.setFullname("New Entry");

        MBiodata savedData = new MBiodata();
        savedData.setId(10L);
        savedData.setFullname("New Entry");

        // We use any() because the controller converts the request to a new Entity
        // object internally
        when(mBiodataService.createMBiodata(org.mockito.ArgumentMatchers.any(MBiodata.class)))
                .thenReturn(savedData);

        // Act & Assert
        mockMvc.perform(post("/v1/m-biodata")
                .with(csrf()) // Required if CSRF is enabled in your security config
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /v1/m-biodata - Success")
    void updateMBiodata_ShouldReturnUpdated() throws Exception {
        MBiodataRequest request = new MBiodataRequest();
        request.setFullname("Updated Name");

        MBiodata updatedData = new MBiodata();
        updatedData.setId(1L);
        updatedData.setFullname("Updated Name");

        when(mBiodataService.updateMBiodata(any(MBiodata.class))).thenReturn(updatedData);

        mockMvc.perform(put("/v1/m-biodata")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /v1/m-biodata/{id} - Success")
    void deleteMBiodata_ShouldReturnSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/v1/m-biodata/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /v1/m-biodata - Test JSON Filter Parsing")
    void getPageMBiodata_ShouldHandleFilters() throws Exception {
        // Arrange
        // Note: The controller expects valid JSON strings for 'filter' and 'sort'
        // params
        String filterJson = "[{\"id\":\"fullname\",\"value\":\"test\",\"matchMode\":\"CONTAINS\"}]";
        String sortJson = "[{\"id\": \"fullname\", \"desc\": true}]";

        // Act & Assert
        mockMvc.perform(get("/v1/m-biodata")
                .param("filter", filterJson)
                .param("page", "0")
                .param("size", "5")
                .param("sort", sortJson))
                .andExpect(status().isOk());

        sortJson = "[{\"id\": \"fullname\", 'desc': true}]";

        // Act & Assert
        mockMvc.perform(get("/v1/m-biodata")
                .param("filter", filterJson)
                .param("page", "0")
                .param("size", "5")
                .param("sort", sortJson))
                .andExpect(status().isBadRequest());
    }
}