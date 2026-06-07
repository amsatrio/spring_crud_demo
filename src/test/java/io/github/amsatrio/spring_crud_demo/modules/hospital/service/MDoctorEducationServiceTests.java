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

import com.github.amsatrio.spring_hospital.model.entity.MDoctorEducation;
import com.github.amsatrio.spring_hospital.repository.MDoctorEducationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MDoctorEducationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MDoctorEducationServiceTests {

    @Mock
    private MDoctorEducationRepository mDoctorEducationRepository;

    @InjectMocks
    private MDoctorEducationServiceImpl mDoctorEducationServiceImpl;

    private MDoctorEducation mDoctorEducation = new MDoctorEducation();

    @BeforeEach
    public void setup() {
        mDoctorEducation.setId(0L);
        mDoctorEducation.setIsLastEducation(false);
        mDoctorEducation.setCreatedBy(0L);
        mDoctorEducation.setCreatedOn(new Date());
        mDoctorEducation.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMDoctorEducationById_CaseDataFound() {
        // when
        when(mDoctorEducationRepository.findById(0L))
                .thenReturn(Optional.of(mDoctorEducation));

        MDoctorEducation mDoctorEducationDb = mDoctorEducationServiceImpl.getMDoctorEducation(0L);

        // then
        assertEquals(mDoctorEducationDb, mDoctorEducation);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMDoctorEducationById_CaseDataNotFound() {
        // when
        when(mDoctorEducationRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mDoctorEducationServiceImpl.getMDoctorEducation(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMDoctorEducation_CaseDataNotFound() {
        // given
        given(mDoctorEducationRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mDoctorEducationRepository.save(mDoctorEducation))
                .thenReturn(mDoctorEducation);

        MDoctorEducation mDoctorEducationDb = mDoctorEducationServiceImpl.createMDoctorEducation(mDoctorEducation);

        // then
        assertEquals(mDoctorEducationDb.getId(), mDoctorEducation.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMDoctorEducation_CaseDataFound() {
        // given
        given(mDoctorEducationRepository.findById(0L))
                .willReturn(Optional.of(mDoctorEducation));

        try {
            mDoctorEducationServiceImpl.createMDoctorEducation(mDoctorEducation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMDoctorEducation_CaseDataFound() {
        // given
        given(mDoctorEducationRepository.findById(0L))
                .willReturn(Optional.of(mDoctorEducation));

        // When
        mDoctorEducation.setIsLastEducation(true);
        when(mDoctorEducationRepository.save(mDoctorEducation))
                .thenReturn(mDoctorEducation);

        MDoctorEducation mDoctorEducationNew = mDoctorEducationServiceImpl.updateMDoctorEducation(mDoctorEducation);

        // then
        assertEquals(mDoctorEducation.getIsLastEducation(), mDoctorEducationNew.getIsLastEducation());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMDoctorEducation_CaseDataNotFound() {
        // given
        given(mDoctorEducationRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mDoctorEducationServiceImpl.updateMDoctorEducation(mDoctorEducation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMDoctorEducation_CaseDataFound() {
        // given
        given(mDoctorEducationRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mDoctorEducationRepository.save(mDoctorEducation))
                .thenReturn(mDoctorEducation);
        mDoctorEducationServiceImpl.createMDoctorEducation(mDoctorEducation);

        // When
        mDoctorEducationServiceImpl.deleteMDoctorEducation(0L);

        try {
            mDoctorEducationServiceImpl.getMDoctorEducation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMDoctorEducation_CaseDataNotFound() {
        // given
        given(mDoctorEducationRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mDoctorEducationServiceImpl.deleteMDoctorEducation(0L);

        try {
            mDoctorEducationServiceImpl.getMDoctorEducation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
