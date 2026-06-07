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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerWalletWithdraw;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TCustomerWalletWithdrawRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerWalletWithdrawServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerWalletWithdrawServiceTests {

    @Mock
    private TCustomerWalletWithdrawRepository tCustomerWalletWithdrawRepository;

    @InjectMocks
    private TCustomerWalletWithdrawServiceImpl tCustomerWalletWithdrawServiceImpl;

    private TCustomerWalletWithdraw tCustomerWalletWithdraw = new TCustomerWalletWithdraw();

    @BeforeEach
    public void setup() {
        tCustomerWalletWithdraw.setId(0L);
        tCustomerWalletWithdraw.setOtp(0);
        tCustomerWalletWithdraw.setCreatedBy(0L);
        tCustomerWalletWithdraw.setCreatedOn(new Date());
        tCustomerWalletWithdraw.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerWalletWithdrawById_CaseDataFound() {
        // when
        when(tCustomerWalletWithdrawRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerWalletWithdraw));

        TCustomerWalletWithdraw tCustomerWalletWithdrawDb = tCustomerWalletWithdrawServiceImpl.getTCustomerWalletWithdraw(0L);

        // then
        assertEquals(tCustomerWalletWithdrawDb, tCustomerWalletWithdraw);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerWalletWithdrawById_CaseDataNotFound() {
        // when
        when(tCustomerWalletWithdrawRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerWalletWithdrawServiceImpl.getTCustomerWalletWithdraw(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerWalletWithdraw_CaseDataNotFound() {
        // given
        given(tCustomerWalletWithdrawRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerWalletWithdrawRepository.save(tCustomerWalletWithdraw))
                .thenReturn(tCustomerWalletWithdraw);

        TCustomerWalletWithdraw tCustomerWalletWithdrawDb = tCustomerWalletWithdrawServiceImpl.createTCustomerWalletWithdraw(tCustomerWalletWithdraw);

        // then
        assertEquals(tCustomerWalletWithdrawDb.getId(), tCustomerWalletWithdraw.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerWalletWithdraw_CaseDataFound() {
        // given
        given(tCustomerWalletWithdrawRepository.findById(0L))
                .willReturn(Optional.of(tCustomerWalletWithdraw));

        try {
            tCustomerWalletWithdrawServiceImpl.createTCustomerWalletWithdraw(tCustomerWalletWithdraw);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerWalletWithdraw_CaseDataFound() {
        // given
        given(tCustomerWalletWithdrawRepository.findById(0L))
                .willReturn(Optional.of(tCustomerWalletWithdraw));

        // When
        tCustomerWalletWithdraw.setOtp(1);
        when(tCustomerWalletWithdrawRepository.save(tCustomerWalletWithdraw))
                .thenReturn(tCustomerWalletWithdraw);

        TCustomerWalletWithdraw tCustomerWalletWithdrawNew = tCustomerWalletWithdrawServiceImpl.updateTCustomerWalletWithdraw(tCustomerWalletWithdraw);

        // then
        assertEquals(tCustomerWalletWithdraw.getOtp(), tCustomerWalletWithdrawNew.getOtp());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerWalletWithdraw_CaseDataNotFound() {
        // given
        given(tCustomerWalletWithdrawRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerWalletWithdrawServiceImpl.updateTCustomerWalletWithdraw(tCustomerWalletWithdraw);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerWalletWithdraw_CaseDataFound() {
        // given
        given(tCustomerWalletWithdrawRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerWalletWithdrawRepository.save(tCustomerWalletWithdraw))
                .thenReturn(tCustomerWalletWithdraw);
        tCustomerWalletWithdrawServiceImpl.createTCustomerWalletWithdraw(tCustomerWalletWithdraw);

        // When
        tCustomerWalletWithdrawServiceImpl.deleteTCustomerWalletWithdraw(0L);

        try {
            tCustomerWalletWithdrawServiceImpl.getTCustomerWalletWithdraw(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerWalletWithdraw_CaseDataNotFound() {
        // given
        given(tCustomerWalletWithdrawRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerWalletWithdrawServiceImpl.deleteTCustomerWalletWithdraw(0L);

        try {
            tCustomerWalletWithdrawServiceImpl.getTCustomerWalletWithdraw(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
