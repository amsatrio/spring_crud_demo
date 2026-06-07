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

import com.github.amsatrio.spring_hospital.model.entity.MUser;
import com.github.amsatrio.spring_hospital.repository.MUserRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MUserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MUserServiceTests {

    @Mock
    private MUserRepository mUserRepository;

    @InjectMocks
    private MUserServiceImpl mUserServiceImpl;

    private MUser mUser = new MUser();

    @BeforeEach
    public void setup() {
        mUser.setId(0L);
        mUser.setLastLogin(new Date());
        mUser.setCreatedBy(0L);
        mUser.setCreatedOn(new Date());
        mUser.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMUserById_CaseDataFound() {
        // when
        when(mUserRepository.findById(0L))
                .thenReturn(Optional.of(mUser));

        MUser mUserDb = mUserServiceImpl.getMUser(0L);

        // then
        assertEquals(mUserDb, mUser);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMUserById_CaseDataNotFound() {
        // when
        when(mUserRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mUserServiceImpl.getMUser(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMUser_CaseDataNotFound() {
        // given
        given(mUserRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mUserRepository.save(mUser))
                .thenReturn(mUser);

        MUser mUserDb = mUserServiceImpl.createMUser(mUser);

        // then
        assertEquals(mUserDb.getId(), mUser.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMUser_CaseDataFound() {
        // given
        given(mUserRepository.findById(0L))
                .willReturn(Optional.of(mUser));

        try {
            mUserServiceImpl.createMUser(mUser);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMUser_CaseDataFound() {
        // given
        given(mUserRepository.findById(0L))
                .willReturn(Optional.of(mUser));

        // When
        mUser.setLastLogin(new Date());
        when(mUserRepository.save(mUser))
                .thenReturn(mUser);

        MUser mUserNew = mUserServiceImpl.updateMUser(mUser);

        // then
        assertEquals(mUser.getLastLogin(), mUserNew.getLastLogin());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMUser_CaseDataNotFound() {
        // given
        given(mUserRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mUserServiceImpl.updateMUser(mUser);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMUser_CaseDataFound() {
        // given
        given(mUserRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mUserRepository.save(mUser))
                .thenReturn(mUser);
        mUserServiceImpl.createMUser(mUser);

        // When
        mUserServiceImpl.deleteMUser(0L);

        try {
            mUserServiceImpl.getMUser(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMUser_CaseDataNotFound() {
        // given
        given(mUserRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mUserServiceImpl.deleteMUser(0L);

        try {
            mUserServiceImpl.getMUser(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
