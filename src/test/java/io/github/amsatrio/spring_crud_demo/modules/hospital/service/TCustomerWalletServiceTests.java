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

import com.github.amsatrio.spring_hospital.model.entity.TCustomerWallet;
import com.github.amsatrio.spring_hospital.repository.TCustomerWalletRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerWalletServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerWalletServiceTests {

    @Mock
    private TCustomerWalletRepository tCustomerWalletRepository;

    @InjectMocks
    private TCustomerWalletServiceImpl tCustomerWalletServiceImpl;

    private TCustomerWallet tCustomerWallet = new TCustomerWallet();

    @BeforeEach
    public void setup() {
        tCustomerWallet.setId(0L);
        tCustomerWallet.setPoints(0F);
        tCustomerWallet.setCreatedBy(0L);
        tCustomerWallet.setCreatedOn(new Date());
        tCustomerWallet.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerWalletById_CaseDataFound() {
        // when
        when(tCustomerWalletRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerWallet));

        TCustomerWallet tCustomerWalletDb = tCustomerWalletServiceImpl.getTCustomerWallet(0L);

        // then
        assertEquals(tCustomerWalletDb, tCustomerWallet);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerWalletById_CaseDataNotFound() {
        // when
        when(tCustomerWalletRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerWalletServiceImpl.getTCustomerWallet(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerWallet_CaseDataNotFound() {
        // given
        given(tCustomerWalletRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerWalletRepository.save(tCustomerWallet))
                .thenReturn(tCustomerWallet);

        TCustomerWallet tCustomerWalletDb = tCustomerWalletServiceImpl.createTCustomerWallet(tCustomerWallet);

        // then
        assertEquals(tCustomerWalletDb.getId(), tCustomerWallet.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerWallet_CaseDataFound() {
        // given
        given(tCustomerWalletRepository.findById(0L))
                .willReturn(Optional.of(tCustomerWallet));

        try {
            tCustomerWalletServiceImpl.createTCustomerWallet(tCustomerWallet);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerWallet_CaseDataFound() {
        // given
        given(tCustomerWalletRepository.findById(0L))
                .willReturn(Optional.of(tCustomerWallet));

        // When
        tCustomerWallet.setPoints(1F);
        when(tCustomerWalletRepository.save(tCustomerWallet))
                .thenReturn(tCustomerWallet);

        TCustomerWallet tCustomerWalletNew = tCustomerWalletServiceImpl.updateTCustomerWallet(tCustomerWallet);

        // then
        assertEquals(tCustomerWallet.getPoints(), tCustomerWalletNew.getPoints());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerWallet_CaseDataNotFound() {
        // given
        given(tCustomerWalletRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerWalletServiceImpl.updateTCustomerWallet(tCustomerWallet);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerWallet_CaseDataFound() {
        // given
        given(tCustomerWalletRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerWalletRepository.save(tCustomerWallet))
                .thenReturn(tCustomerWallet);
        tCustomerWalletServiceImpl.createTCustomerWallet(tCustomerWallet);

        // When
        tCustomerWalletServiceImpl.deleteTCustomerWallet(0L);

        try {
            tCustomerWalletServiceImpl.getTCustomerWallet(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerWallet_CaseDataNotFound() {
        // given
        given(tCustomerWalletRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerWalletServiceImpl.deleteTCustomerWallet(0L);

        try {
            tCustomerWalletServiceImpl.getTCustomerWallet(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
