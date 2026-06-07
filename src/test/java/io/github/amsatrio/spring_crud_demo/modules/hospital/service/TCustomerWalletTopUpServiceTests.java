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

import com.github.amsatrio.spring_hospital.model.entity.TCustomerWalletTopUp;
import com.github.amsatrio.spring_hospital.repository.TCustomerWalletTopUpRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerWalletTopUpServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerWalletTopUpServiceTests {

    @Mock
    private TCustomerWalletTopUpRepository tCustomerWalletTopUpRepository;

    @InjectMocks
    private TCustomerWalletTopUpServiceImpl tCustomerWalletTopUpServiceImpl;

    private TCustomerWalletTopUp tCustomerWalletTopUp = new TCustomerWalletTopUp();

    @BeforeEach
    public void setup() {
        tCustomerWalletTopUp.setId(0L);
        tCustomerWalletTopUp.setAmount(0F);
        tCustomerWalletTopUp.setCreatedBy(0L);
        tCustomerWalletTopUp.setCreatedOn(new Date());
        tCustomerWalletTopUp.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerWalletTopUpById_CaseDataFound() {
        // when
        when(tCustomerWalletTopUpRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerWalletTopUp));

        TCustomerWalletTopUp tCustomerWalletTopUpDb = tCustomerWalletTopUpServiceImpl.getTCustomerWalletTopUp(0L);

        // then
        assertEquals(tCustomerWalletTopUpDb, tCustomerWalletTopUp);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerWalletTopUpById_CaseDataNotFound() {
        // when
        when(tCustomerWalletTopUpRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerWalletTopUpServiceImpl.getTCustomerWalletTopUp(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerWalletTopUp_CaseDataNotFound() {
        // given
        given(tCustomerWalletTopUpRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerWalletTopUpRepository.save(tCustomerWalletTopUp))
                .thenReturn(tCustomerWalletTopUp);

        TCustomerWalletTopUp tCustomerWalletTopUpDb = tCustomerWalletTopUpServiceImpl.createTCustomerWalletTopUp(tCustomerWalletTopUp);

        // then
        assertEquals(tCustomerWalletTopUpDb.getId(), tCustomerWalletTopUp.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerWalletTopUp_CaseDataFound() {
        // given
        given(tCustomerWalletTopUpRepository.findById(0L))
                .willReturn(Optional.of(tCustomerWalletTopUp));

        try {
            tCustomerWalletTopUpServiceImpl.createTCustomerWalletTopUp(tCustomerWalletTopUp);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerWalletTopUp_CaseDataFound() {
        // given
        given(tCustomerWalletTopUpRepository.findById(0L))
                .willReturn(Optional.of(tCustomerWalletTopUp));

        // When
        tCustomerWalletTopUp.setAmount(1F);
        when(tCustomerWalletTopUpRepository.save(tCustomerWalletTopUp))
                .thenReturn(tCustomerWalletTopUp);

        TCustomerWalletTopUp tCustomerWalletTopUpNew = tCustomerWalletTopUpServiceImpl.updateTCustomerWalletTopUp(tCustomerWalletTopUp);

        // then
        assertEquals(tCustomerWalletTopUp.getAmount(), tCustomerWalletTopUpNew.getAmount());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerWalletTopUp_CaseDataNotFound() {
        // given
        given(tCustomerWalletTopUpRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerWalletTopUpServiceImpl.updateTCustomerWalletTopUp(tCustomerWalletTopUp);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerWalletTopUp_CaseDataFound() {
        // given
        given(tCustomerWalletTopUpRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerWalletTopUpRepository.save(tCustomerWalletTopUp))
                .thenReturn(tCustomerWalletTopUp);
        tCustomerWalletTopUpServiceImpl.createTCustomerWalletTopUp(tCustomerWalletTopUp);

        // When
        tCustomerWalletTopUpServiceImpl.deleteTCustomerWalletTopUp(0L);

        try {
            tCustomerWalletTopUpServiceImpl.getTCustomerWalletTopUp(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerWalletTopUp_CaseDataNotFound() {
        // given
        given(tCustomerWalletTopUpRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerWalletTopUpServiceImpl.deleteTCustomerWalletTopUp(0L);

        try {
            tCustomerWalletTopUpServiceImpl.getTCustomerWalletTopUp(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
