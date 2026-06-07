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

import com.github.amsatrio.spring_hospital.model.entity.TToken;
import com.github.amsatrio.spring_hospital.repository.TTokenRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TTokenServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TTokenServiceTests {

    @Mock
    private TTokenRepository tTokenRepository;

    @InjectMocks
    private TTokenServiceImpl tTokenServiceImpl;

    private TToken tToken = new TToken();

    @BeforeEach
    public void setup() {
        tToken.setId(0L);
        tToken.setUsedFor("init");
        tToken.setCreatedBy(0L);
        tToken.setCreatedOn(new Date());
        tToken.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTTokenById_CaseDataFound() {
        // when
        when(tTokenRepository.findById(0L))
                .thenReturn(Optional.of(tToken));

        TToken tTokenDb = tTokenServiceImpl.getTToken(0L);

        // then
        assertEquals(tTokenDb, tToken);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTTokenById_CaseDataNotFound() {
        // when
        when(tTokenRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tTokenServiceImpl.getTToken(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTToken_CaseDataNotFound() {
        // given
        given(tTokenRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tTokenRepository.save(tToken))
                .thenReturn(tToken);

        TToken tTokenDb = tTokenServiceImpl.createTToken(tToken);

        // then
        assertEquals(tTokenDb.getId(), tToken.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTToken_CaseDataFound() {
        // given
        given(tTokenRepository.findById(0L))
                .willReturn(Optional.of(tToken));

        try {
            tTokenServiceImpl.createTToken(tToken);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTToken_CaseDataFound() {
        // given
        given(tTokenRepository.findById(0L))
                .willReturn(Optional.of(tToken));

        // When
        tToken.setUsedFor("update");
        when(tTokenRepository.save(tToken))
                .thenReturn(tToken);

        TToken tTokenNew = tTokenServiceImpl.updateTToken(tToken);

        // then
        assertEquals(tToken.getUsedFor(), tTokenNew.getUsedFor());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTToken_CaseDataNotFound() {
        // given
        given(tTokenRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tTokenServiceImpl.updateTToken(tToken);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTToken_CaseDataFound() {
        // given
        given(tTokenRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tTokenRepository.save(tToken))
                .thenReturn(tToken);
        tTokenServiceImpl.createTToken(tToken);

        // When
        tTokenServiceImpl.deleteTToken(0L);

        try {
            tTokenServiceImpl.getTToken(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTToken_CaseDataNotFound() {
        // given
        given(tTokenRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tTokenServiceImpl.deleteTToken(0L);

        try {
            tTokenServiceImpl.getTToken(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
