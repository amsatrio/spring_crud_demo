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

import com.github.amsatrio.spring_hospital.model.entity.MLocationLevel;
import com.github.amsatrio.spring_hospital.repository.MLocationLevelRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MLocationLevelServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MLocationLevelServiceTests {

    @Mock
    private MLocationLevelRepository mLocationLevelRepository;

    @InjectMocks
    private MLocationLevelServiceImpl mLocationLevelServiceImpl;

    private MLocationLevel mLocationLevel = new MLocationLevel();

    @BeforeEach
    public void setup() {
        mLocationLevel.setId(0L);
        mLocationLevel.setAbbreviation("init");
        mLocationLevel.setCreatedBy(0L);
        mLocationLevel.setCreatedOn(new Date());
        mLocationLevel.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMLocationLevelById_CaseDataFound() {
        // when
        when(mLocationLevelRepository.findById(0L))
                .thenReturn(Optional.of(mLocationLevel));

        MLocationLevel mLocationLevelDb = mLocationLevelServiceImpl.getMLocationLevel(0L);

        // then
        assertEquals(mLocationLevelDb, mLocationLevel);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMLocationLevelById_CaseDataNotFound() {
        // when
        when(mLocationLevelRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mLocationLevelServiceImpl.getMLocationLevel(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMLocationLevel_CaseDataNotFound() {
        // given
        given(mLocationLevelRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mLocationLevelRepository.save(mLocationLevel))
                .thenReturn(mLocationLevel);

        MLocationLevel mLocationLevelDb = mLocationLevelServiceImpl.createMLocationLevel(mLocationLevel);

        // then
        assertEquals(mLocationLevelDb.getId(), mLocationLevel.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMLocationLevel_CaseDataFound() {
        // given
        given(mLocationLevelRepository.findById(0L))
                .willReturn(Optional.of(mLocationLevel));

        try {
            mLocationLevelServiceImpl.createMLocationLevel(mLocationLevel);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMLocationLevel_CaseDataFound() {
        // given
        given(mLocationLevelRepository.findById(0L))
                .willReturn(Optional.of(mLocationLevel));

        // When
        mLocationLevel.setAbbreviation("update");
        when(mLocationLevelRepository.save(mLocationLevel))
                .thenReturn(mLocationLevel);

        MLocationLevel mLocationLevelNew = mLocationLevelServiceImpl.updateMLocationLevel(mLocationLevel);

        // then
        assertEquals(mLocationLevel.getAbbreviation(), mLocationLevelNew.getAbbreviation());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMLocationLevel_CaseDataNotFound() {
        // given
        given(mLocationLevelRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mLocationLevelServiceImpl.updateMLocationLevel(mLocationLevel);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMLocationLevel_CaseDataFound() {
        // given
        given(mLocationLevelRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mLocationLevelRepository.save(mLocationLevel))
                .thenReturn(mLocationLevel);
        mLocationLevelServiceImpl.createMLocationLevel(mLocationLevel);

        // When
        mLocationLevelServiceImpl.deleteMLocationLevel(0L);

        try {
            mLocationLevelServiceImpl.getMLocationLevel(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMLocationLevel_CaseDataNotFound() {
        // given
        given(mLocationLevelRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mLocationLevelServiceImpl.deleteMLocationLevel(0L);

        try {
            mLocationLevelServiceImpl.getMLocationLevel(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
