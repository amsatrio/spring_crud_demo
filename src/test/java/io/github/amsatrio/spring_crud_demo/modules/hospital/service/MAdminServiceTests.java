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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MAdmin;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MAdminRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MAdminServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MAdminServiceTests {

    @Mock
    private MAdminRepository mAdminRepository;

    @InjectMocks
    private MAdminServiceImpl mAdminServiceImpl;

    private MAdmin mAdmin = new MAdmin();

    @BeforeEach
    public void setup() {
        mAdmin.setId(0L);
        mAdmin.setCode("init");
        mAdmin.setCreatedBy(0L);
        mAdmin.setCreatedOn(new Date());
        mAdmin.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMAdminById_CaseDataFound() {
        // when
        when(mAdminRepository.findById(0L))
                .thenReturn(Optional.of(mAdmin));

        MAdmin mAdminDb = mAdminServiceImpl.getMAdmin(0L);

        // then
        assertEquals(mAdminDb, mAdmin);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMAdminById_CaseDataNotFound() {
        // when
        when(mAdminRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mAdminServiceImpl.getMAdmin(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMAdmin_CaseDataNotFound() {
        // given
        given(mAdminRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mAdminRepository.save(mAdmin))
                .thenReturn(mAdmin);

        MAdmin mAdminDb = mAdminServiceImpl.createMAdmin(mAdmin);

        // then
        assertEquals(mAdminDb.getId(), mAdmin.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMAdmin_CaseDataFound() {
        // given
        given(mAdminRepository.findById(0L))
                .willReturn(Optional.of(mAdmin));

        try {
            mAdminServiceImpl.createMAdmin(mAdmin);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMAdmin_CaseDataFound() {
        // given
        given(mAdminRepository.findById(0L))
                .willReturn(Optional.of(mAdmin));

        // When
        mAdmin.setCode("update");
        when(mAdminRepository.save(mAdmin))
                .thenReturn(mAdmin);

        MAdmin mAdminNew = mAdminServiceImpl.updateMAdmin(mAdmin);

        // then
        assertEquals(mAdmin.getCode(), mAdminNew.getCode());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMAdmin_CaseDataNotFound() {
        // given
        given(mAdminRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mAdminServiceImpl.updateMAdmin(mAdmin);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMAdmin_CaseDataFound() {
        // given
        given(mAdminRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mAdminRepository.save(mAdmin))
                .thenReturn(mAdmin);
        mAdminServiceImpl.createMAdmin(mAdmin);

        // When
        mAdminServiceImpl.deleteMAdmin(0L);

        try {
            mAdminServiceImpl.getMAdmin(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMAdmin_CaseDataNotFound() {
        // given
        given(mAdminRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mAdminServiceImpl.deleteMAdmin(0L);

        try {
            mAdminServiceImpl.getMAdmin(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
