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

import com.github.amsatrio.spring_hospital.model.entity.TResetPassword;
import com.github.amsatrio.spring_hospital.repository.TResetPasswordRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TResetPasswordServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TResetPasswordServiceTests {

    @Mock
    private TResetPasswordRepository tResetPasswordRepository;

    @InjectMocks
    private TResetPasswordServiceImpl tResetPasswordServiceImpl;

    private TResetPassword tResetPassword = new TResetPassword();

    @BeforeEach
    public void setup() {
        tResetPassword.setId(0L);
        tResetPassword.setResetFor("init");
        tResetPassword.setCreatedBy(0L);
        tResetPassword.setCreatedOn(new Date());
        tResetPassword.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTResetPasswordById_CaseDataFound() {
        // when
        when(tResetPasswordRepository.findById(0L))
                .thenReturn(Optional.of(tResetPassword));

        TResetPassword tResetPasswordDb = tResetPasswordServiceImpl.getTResetPassword(0L);

        // then
        assertEquals(tResetPasswordDb, tResetPassword);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTResetPasswordById_CaseDataNotFound() {
        // when
        when(tResetPasswordRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tResetPasswordServiceImpl.getTResetPassword(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTResetPassword_CaseDataNotFound() {
        // given
        given(tResetPasswordRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tResetPasswordRepository.save(tResetPassword))
                .thenReturn(tResetPassword);

        TResetPassword tResetPasswordDb = tResetPasswordServiceImpl.createTResetPassword(tResetPassword);

        // then
        assertEquals(tResetPasswordDb.getId(), tResetPassword.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTResetPassword_CaseDataFound() {
        // given
        given(tResetPasswordRepository.findById(0L))
                .willReturn(Optional.of(tResetPassword));

        try {
            tResetPasswordServiceImpl.createTResetPassword(tResetPassword);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTResetPassword_CaseDataFound() {
        // given
        given(tResetPasswordRepository.findById(0L))
                .willReturn(Optional.of(tResetPassword));

        // When
        tResetPassword.setResetFor("update");
        when(tResetPasswordRepository.save(tResetPassword))
                .thenReturn(tResetPassword);

        TResetPassword tResetPasswordNew = tResetPasswordServiceImpl.updateTResetPassword(tResetPassword);

        // then
        assertEquals(tResetPassword.getResetFor(), tResetPasswordNew.getResetFor());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTResetPassword_CaseDataNotFound() {
        // given
        given(tResetPasswordRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tResetPasswordServiceImpl.updateTResetPassword(tResetPassword);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTResetPassword_CaseDataFound() {
        // given
        given(tResetPasswordRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tResetPasswordRepository.save(tResetPassword))
                .thenReturn(tResetPassword);
        tResetPasswordServiceImpl.createTResetPassword(tResetPassword);

        // When
        tResetPasswordServiceImpl.deleteTResetPassword(0L);

        try {
            tResetPasswordServiceImpl.getTResetPassword(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTResetPassword_CaseDataNotFound() {
        // given
        given(tResetPasswordRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tResetPasswordServiceImpl.deleteTResetPassword(0L);

        try {
            tResetPasswordServiceImpl.getTResetPassword(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
