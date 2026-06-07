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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MWalletDefaultNominal;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MWalletDefaultNominalRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MWalletDefaultNominalServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MWalletDefaultNominalServiceTests {

    @Mock
    private MWalletDefaultNominalRepository mWalletDefaultNominalRepository;

    @InjectMocks
    private MWalletDefaultNominalServiceImpl mWalletDefaultNominalServiceImpl;

    private MWalletDefaultNominal mWalletDefaultNominal = new MWalletDefaultNominal();

    @BeforeEach
    public void setup() {
        mWalletDefaultNominal.setId(0L);
        mWalletDefaultNominal.setNominal(0);
        mWalletDefaultNominal.setCreatedBy(0L);
        mWalletDefaultNominal.setCreatedOn(new Date());
        mWalletDefaultNominal.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMWalletDefaultNominalById_CaseDataFound() {
        // when
        when(mWalletDefaultNominalRepository.findById(0L))
                .thenReturn(Optional.of(mWalletDefaultNominal));

        MWalletDefaultNominal mWalletDefaultNominalDb = mWalletDefaultNominalServiceImpl.getMWalletDefaultNominal(0L);

        // then
        assertEquals(mWalletDefaultNominalDb, mWalletDefaultNominal);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMWalletDefaultNominalById_CaseDataNotFound() {
        // when
        when(mWalletDefaultNominalRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mWalletDefaultNominalServiceImpl.getMWalletDefaultNominal(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMWalletDefaultNominal_CaseDataNotFound() {
        // given
        given(mWalletDefaultNominalRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mWalletDefaultNominalRepository.save(mWalletDefaultNominal))
                .thenReturn(mWalletDefaultNominal);

        MWalletDefaultNominal mWalletDefaultNominalDb = mWalletDefaultNominalServiceImpl.createMWalletDefaultNominal(mWalletDefaultNominal);

        // then
        assertEquals(mWalletDefaultNominalDb.getId(), mWalletDefaultNominal.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMWalletDefaultNominal_CaseDataFound() {
        // given
        given(mWalletDefaultNominalRepository.findById(0L))
                .willReturn(Optional.of(mWalletDefaultNominal));

        try {
            mWalletDefaultNominalServiceImpl.createMWalletDefaultNominal(mWalletDefaultNominal);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMWalletDefaultNominal_CaseDataFound() {
        // given
        given(mWalletDefaultNominalRepository.findById(0L))
                .willReturn(Optional.of(mWalletDefaultNominal));

        // When
        mWalletDefaultNominal.setNominal(1);
        when(mWalletDefaultNominalRepository.save(mWalletDefaultNominal))
                .thenReturn(mWalletDefaultNominal);

        MWalletDefaultNominal mWalletDefaultNominalNew = mWalletDefaultNominalServiceImpl.updateMWalletDefaultNominal(mWalletDefaultNominal);

        // then
        assertEquals(mWalletDefaultNominal.getNominal(), mWalletDefaultNominalNew.getNominal());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMWalletDefaultNominal_CaseDataNotFound() {
        // given
        given(mWalletDefaultNominalRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mWalletDefaultNominalServiceImpl.updateMWalletDefaultNominal(mWalletDefaultNominal);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMWalletDefaultNominal_CaseDataFound() {
        // given
        given(mWalletDefaultNominalRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mWalletDefaultNominalRepository.save(mWalletDefaultNominal))
                .thenReturn(mWalletDefaultNominal);
        mWalletDefaultNominalServiceImpl.createMWalletDefaultNominal(mWalletDefaultNominal);

        // When
        mWalletDefaultNominalServiceImpl.deleteMWalletDefaultNominal(0L);

        try {
            mWalletDefaultNominalServiceImpl.getMWalletDefaultNominal(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMWalletDefaultNominal_CaseDataNotFound() {
        // given
        given(mWalletDefaultNominalRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mWalletDefaultNominalServiceImpl.deleteMWalletDefaultNominal(0L);

        try {
            mWalletDefaultNominalServiceImpl.getMWalletDefaultNominal(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
