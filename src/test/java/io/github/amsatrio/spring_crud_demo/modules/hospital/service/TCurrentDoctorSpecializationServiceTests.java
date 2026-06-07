package io.github.amsatrio.spring_crud_demo.modules.hospital.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import com.github.amsatrio.spring_hospital.model.entity.TCurrentDoctorSpecialization;
import com.github.amsatrio.spring_hospital.repository.TCurrentDoctorSpecializationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCurrentDoctorSpecializationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCurrentDoctorSpecializationServiceTests {

    @Mock
    private TCurrentDoctorSpecializationRepository tCurrentDoctorSpecializationRepository;

    @InjectMocks
    private TCurrentDoctorSpecializationServiceImpl tCurrentDoctorSpecializationServiceImpl;

    private TCurrentDoctorSpecialization tCurrentDoctorSpecialization = new TCurrentDoctorSpecialization();

    @BeforeEach
    public void setup() {
        tCurrentDoctorSpecialization.setId(0L);
        tCurrentDoctorSpecialization.setSpecializationId(0L);
        tCurrentDoctorSpecialization.setCreatedBy(0L);
        tCurrentDoctorSpecialization.setCreatedOn(new Date());
        tCurrentDoctorSpecialization.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCurrentDoctorSpecializationById_CaseDataFound() {
        // when
        when(tCurrentDoctorSpecializationRepository.findById(0L))
                .thenReturn(Optional.of(tCurrentDoctorSpecialization));

        TCurrentDoctorSpecialization tCurrentDoctorSpecializationDb = tCurrentDoctorSpecializationServiceImpl.getTCurrentDoctorSpecialization(0L);

        // then
        assertEquals(tCurrentDoctorSpecializationDb, tCurrentDoctorSpecialization);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCurrentDoctorSpecializationById_CaseDataNotFound() {
        // when
        when(tCurrentDoctorSpecializationRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCurrentDoctorSpecializationServiceImpl.getTCurrentDoctorSpecialization(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCurrentDoctorSpecialization_CaseDataNotFound() {
        // given
        given(tCurrentDoctorSpecializationRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCurrentDoctorSpecializationRepository.save(tCurrentDoctorSpecialization))
                .thenReturn(tCurrentDoctorSpecialization);

        TCurrentDoctorSpecialization tCurrentDoctorSpecializationDb = tCurrentDoctorSpecializationServiceImpl.createTCurrentDoctorSpecialization(tCurrentDoctorSpecialization);

        // then
        assertEquals(tCurrentDoctorSpecializationDb.getId(), tCurrentDoctorSpecialization.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCurrentDoctorSpecialization_CaseDataFound() {
        // given
        given(tCurrentDoctorSpecializationRepository.findById(0L))
                .willReturn(Optional.of(tCurrentDoctorSpecialization));

        try {
            tCurrentDoctorSpecializationServiceImpl.createTCurrentDoctorSpecialization(tCurrentDoctorSpecialization);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCurrentDoctorSpecialization_CaseDataFound() {
        // given
        given(tCurrentDoctorSpecializationRepository.findById(0L))
                .willReturn(Optional.of(tCurrentDoctorSpecialization));

        // When
        tCurrentDoctorSpecialization.setSpecializationId(1L);
        when(tCurrentDoctorSpecializationRepository.save(tCurrentDoctorSpecialization))
                .thenReturn(tCurrentDoctorSpecialization);

        TCurrentDoctorSpecialization tCurrentDoctorSpecializationNew = tCurrentDoctorSpecializationServiceImpl.updateTCurrentDoctorSpecialization(tCurrentDoctorSpecialization);

        // then
        assertEquals(tCurrentDoctorSpecialization.getSpecializationId(), tCurrentDoctorSpecializationNew.getSpecializationId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCurrentDoctorSpecialization_CaseDataNotFound() {
        // given
        given(tCurrentDoctorSpecializationRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCurrentDoctorSpecializationServiceImpl.updateTCurrentDoctorSpecialization(tCurrentDoctorSpecialization);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCurrentDoctorSpecialization_CaseDataFound() {
        // given
        given(tCurrentDoctorSpecializationRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCurrentDoctorSpecializationRepository.save(tCurrentDoctorSpecialization))
                .thenReturn(tCurrentDoctorSpecialization);
        tCurrentDoctorSpecializationServiceImpl.createTCurrentDoctorSpecialization(tCurrentDoctorSpecialization);

        // When
        tCurrentDoctorSpecializationServiceImpl.deleteTCurrentDoctorSpecialization(0L);

        try {
            tCurrentDoctorSpecializationServiceImpl.getTCurrentDoctorSpecialization(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCurrentDoctorSpecialization_CaseDataNotFound() {
        // given
        given(tCurrentDoctorSpecializationRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCurrentDoctorSpecializationServiceImpl.deleteTCurrentDoctorSpecialization(0L);

        try {
            tCurrentDoctorSpecializationServiceImpl.getTCurrentDoctorSpecialization(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
