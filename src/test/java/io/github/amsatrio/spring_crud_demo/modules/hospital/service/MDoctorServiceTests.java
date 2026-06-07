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

import com.github.amsatrio.spring_hospital.model.entity.MDoctor;
import com.github.amsatrio.spring_hospital.repository.MDoctorRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MDoctorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MDoctorServiceTests {

    @Mock
    private MDoctorRepository mDoctorRepository;

    @InjectMocks
    private MDoctorServiceImpl mDoctorServiceImpl;

    private MDoctor mDoctor = new MDoctor();

    @BeforeEach
    public void setup() {
        mDoctor.setId(0L);
        mDoctor.setStr("init");
        mDoctor.setCreatedBy(0L);
        mDoctor.setCreatedOn(new Date());
        mDoctor.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMDoctorById_CaseDataFound() {
        // when
        when(mDoctorRepository.findById(0L))
                .thenReturn(Optional.of(mDoctor));

        MDoctor mDoctorDb = mDoctorServiceImpl.getMDoctor(0L);

        // then
        assertEquals(mDoctorDb, mDoctor);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMDoctorById_CaseDataNotFound() {
        // when
        when(mDoctorRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mDoctorServiceImpl.getMDoctor(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMDoctor_CaseDataNotFound() {
        // given
        given(mDoctorRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mDoctorRepository.save(mDoctor))
                .thenReturn(mDoctor);

        MDoctor mDoctorDb = mDoctorServiceImpl.createMDoctor(mDoctor);

        // then
        assertEquals(mDoctorDb.getId(), mDoctor.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMDoctor_CaseDataFound() {
        // given
        given(mDoctorRepository.findById(0L))
                .willReturn(Optional.of(mDoctor));

        try {
            mDoctorServiceImpl.createMDoctor(mDoctor);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMDoctor_CaseDataFound() {
        // given
        given(mDoctorRepository.findById(0L))
                .willReturn(Optional.of(mDoctor));

        // When
        mDoctor.setStr("update");
        when(mDoctorRepository.save(mDoctor))
                .thenReturn(mDoctor);

        MDoctor mDoctorNew = mDoctorServiceImpl.updateMDoctor(mDoctor);

        // then
        assertEquals(mDoctor.getStr(), mDoctorNew.getStr());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMDoctor_CaseDataNotFound() {
        // given
        given(mDoctorRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mDoctorServiceImpl.updateMDoctor(mDoctor);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMDoctor_CaseDataFound() {
        // given
        given(mDoctorRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mDoctorRepository.save(mDoctor))
                .thenReturn(mDoctor);
        mDoctorServiceImpl.createMDoctor(mDoctor);

        // When
        mDoctorServiceImpl.deleteMDoctor(0L);

        try {
            mDoctorServiceImpl.getMDoctor(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMDoctor_CaseDataNotFound() {
        // given
        given(mDoctorRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mDoctorServiceImpl.deleteMDoctor(0L);

        try {
            mDoctorServiceImpl.getMDoctor(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
