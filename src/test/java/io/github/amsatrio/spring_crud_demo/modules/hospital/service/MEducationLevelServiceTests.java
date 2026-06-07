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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MEducationLevel;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MEducationLevelRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MEducationLevelServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MEducationLevelServiceTests {

    @Mock
    private MEducationLevelRepository mEducationLevelRepository;

    @InjectMocks
    private MEducationLevelServiceImpl mEducationLevelServiceImpl;

    private MEducationLevel mEducationLevel = new MEducationLevel();

    @BeforeEach
    public void setup() {
        mEducationLevel.setId(0L);
        mEducationLevel.setName("init");
        mEducationLevel.setCreatedBy(0L);
        mEducationLevel.setCreatedOn(new Date());
        mEducationLevel.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMEducationLevelById_CaseDataFound() {
        // when
        when(mEducationLevelRepository.findById(0L))
                .thenReturn(Optional.of(mEducationLevel));

        MEducationLevel mEducationLevelDb = mEducationLevelServiceImpl.getMEducationLevel(0L);

        // then
        assertEquals(mEducationLevelDb, mEducationLevel);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMEducationLevelById_CaseDataNotFound() {
        // when
        when(mEducationLevelRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mEducationLevelServiceImpl.getMEducationLevel(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMEducationLevel_CaseDataNotFound() {
        // given
        given(mEducationLevelRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mEducationLevelRepository.save(mEducationLevel))
                .thenReturn(mEducationLevel);

        MEducationLevel mEducationLevelDb = mEducationLevelServiceImpl.createMEducationLevel(mEducationLevel);

        // then
        assertEquals(mEducationLevelDb.getId(), mEducationLevel.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMEducationLevel_CaseDataFound() {
        // given
        given(mEducationLevelRepository.findById(0L))
                .willReturn(Optional.of(mEducationLevel));

        try {
            mEducationLevelServiceImpl.createMEducationLevel(mEducationLevel);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMEducationLevel_CaseDataFound() {
        // given
        given(mEducationLevelRepository.findById(0L))
                .willReturn(Optional.of(mEducationLevel));

        // When
        mEducationLevel.setName("update");
        when(mEducationLevelRepository.save(mEducationLevel))
                .thenReturn(mEducationLevel);

        MEducationLevel mEducationLevelNew = mEducationLevelServiceImpl.updateMEducationLevel(mEducationLevel);

        // then
        assertEquals(mEducationLevel.getName(), mEducationLevelNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMEducationLevel_CaseDataNotFound() {
        // given
        given(mEducationLevelRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mEducationLevelServiceImpl.updateMEducationLevel(mEducationLevel);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMEducationLevel_CaseDataFound() {
        // given
        given(mEducationLevelRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mEducationLevelRepository.save(mEducationLevel))
                .thenReturn(mEducationLevel);
        mEducationLevelServiceImpl.createMEducationLevel(mEducationLevel);

        // When
        mEducationLevelServiceImpl.deleteMEducationLevel(0L);

        try {
            mEducationLevelServiceImpl.getMEducationLevel(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMEducationLevel_CaseDataNotFound() {
        // given
        given(mEducationLevelRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mEducationLevelServiceImpl.deleteMEducationLevel(0L);

        try {
            mEducationLevelServiceImpl.getMEducationLevel(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
