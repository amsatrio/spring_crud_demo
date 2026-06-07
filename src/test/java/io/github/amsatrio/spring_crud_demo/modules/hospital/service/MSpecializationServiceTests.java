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

import com.github.amsatrio.spring_hospital.model.entity.MSpecialization;
import com.github.amsatrio.spring_hospital.repository.MSpecializationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MSpecializationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MSpecializationServiceTests {

    @Mock
    private MSpecializationRepository mSpecializationRepository;

    @InjectMocks
    private MSpecializationServiceImpl mSpecializationServiceImpl;

    private MSpecialization mSpecialization = new MSpecialization();

    @BeforeEach
    public void setup() {
        mSpecialization.setId(0L);
        mSpecialization.setName("init");
        mSpecialization.setCreatedBy(0L);
        mSpecialization.setCreatedOn(new Date());
        mSpecialization.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMSpecializationById_CaseDataFound() {
        // when
        when(mSpecializationRepository.findById(0L))
                .thenReturn(Optional.of(mSpecialization));

        MSpecialization mSpecializationDb = mSpecializationServiceImpl.getMSpecialization(0L);

        // then
        assertEquals(mSpecializationDb, mSpecialization);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMSpecializationById_CaseDataNotFound() {
        // when
        when(mSpecializationRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mSpecializationServiceImpl.getMSpecialization(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMSpecialization_CaseDataNotFound() {
        // given
        given(mSpecializationRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mSpecializationRepository.save(mSpecialization))
                .thenReturn(mSpecialization);

        MSpecialization mSpecializationDb = mSpecializationServiceImpl.createMSpecialization(mSpecialization);

        // then
        assertEquals(mSpecializationDb.getId(), mSpecialization.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMSpecialization_CaseDataFound() {
        // given
        given(mSpecializationRepository.findById(0L))
                .willReturn(Optional.of(mSpecialization));

        try {
            mSpecializationServiceImpl.createMSpecialization(mSpecialization);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMSpecialization_CaseDataFound() {
        // given
        given(mSpecializationRepository.findById(0L))
                .willReturn(Optional.of(mSpecialization));

        // When
        mSpecialization.setName("update");
        when(mSpecializationRepository.save(mSpecialization))
                .thenReturn(mSpecialization);

        MSpecialization mSpecializationNew = mSpecializationServiceImpl.updateMSpecialization(mSpecialization);

        // then
        assertEquals(mSpecialization.getName(), mSpecializationNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMSpecialization_CaseDataNotFound() {
        // given
        given(mSpecializationRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mSpecializationServiceImpl.updateMSpecialization(mSpecialization);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMSpecialization_CaseDataFound() {
        // given
        given(mSpecializationRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mSpecializationRepository.save(mSpecialization))
                .thenReturn(mSpecialization);
        mSpecializationServiceImpl.createMSpecialization(mSpecialization);

        // When
        mSpecializationServiceImpl.deleteMSpecialization(0L);

        try {
            mSpecializationServiceImpl.getMSpecialization(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMSpecialization_CaseDataNotFound() {
        // given
        given(mSpecializationRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mSpecializationServiceImpl.deleteMSpecialization(0L);

        try {
            mSpecializationServiceImpl.getMSpecialization(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
