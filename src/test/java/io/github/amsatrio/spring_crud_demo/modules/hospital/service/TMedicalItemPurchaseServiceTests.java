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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TMedicalItemPurchase;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TMedicalItemPurchaseRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TMedicalItemPurchaseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TMedicalItemPurchaseServiceTests {

    @Mock
    private TMedicalItemPurchaseRepository tMedicalItemPurchaseRepository;

    @InjectMocks
    private TMedicalItemPurchaseServiceImpl tMedicalItemPurchaseServiceImpl;

    private TMedicalItemPurchase tMedicalItemPurchase = new TMedicalItemPurchase();

    @BeforeEach
    public void setup() {
        tMedicalItemPurchase.setId(0L);
        tMedicalItemPurchase.setPaymentMethodId(0L);
        tMedicalItemPurchase.setCreatedBy(0L);
        tMedicalItemPurchase.setCreatedOn(new Date());
        tMedicalItemPurchase.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTMedicalItemPurchaseById_CaseDataFound() {
        // when
        when(tMedicalItemPurchaseRepository.findById(0L))
                .thenReturn(Optional.of(tMedicalItemPurchase));

        TMedicalItemPurchase tMedicalItemPurchaseDb = tMedicalItemPurchaseServiceImpl.getTMedicalItemPurchase(0L);

        // then
        assertEquals(tMedicalItemPurchaseDb, tMedicalItemPurchase);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTMedicalItemPurchaseById_CaseDataNotFound() {
        // when
        when(tMedicalItemPurchaseRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tMedicalItemPurchaseServiceImpl.getTMedicalItemPurchase(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTMedicalItemPurchase_CaseDataNotFound() {
        // given
        given(tMedicalItemPurchaseRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tMedicalItemPurchaseRepository.save(tMedicalItemPurchase))
                .thenReturn(tMedicalItemPurchase);

        TMedicalItemPurchase tMedicalItemPurchaseDb = tMedicalItemPurchaseServiceImpl.createTMedicalItemPurchase(tMedicalItemPurchase);

        // then
        assertEquals(tMedicalItemPurchaseDb.getId(), tMedicalItemPurchase.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTMedicalItemPurchase_CaseDataFound() {
        // given
        given(tMedicalItemPurchaseRepository.findById(0L))
                .willReturn(Optional.of(tMedicalItemPurchase));

        try {
            tMedicalItemPurchaseServiceImpl.createTMedicalItemPurchase(tMedicalItemPurchase);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTMedicalItemPurchase_CaseDataFound() {
        // given
        given(tMedicalItemPurchaseRepository.findById(0L))
                .willReturn(Optional.of(tMedicalItemPurchase));

        // When
        tMedicalItemPurchase.setPaymentMethodId(1L);
        when(tMedicalItemPurchaseRepository.save(tMedicalItemPurchase))
                .thenReturn(tMedicalItemPurchase);

        TMedicalItemPurchase tMedicalItemPurchaseNew = tMedicalItemPurchaseServiceImpl.updateTMedicalItemPurchase(tMedicalItemPurchase);

        // then
        assertEquals(tMedicalItemPurchase.getPaymentMethodId(), tMedicalItemPurchaseNew.getPaymentMethodId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTMedicalItemPurchase_CaseDataNotFound() {
        // given
        given(tMedicalItemPurchaseRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tMedicalItemPurchaseServiceImpl.updateTMedicalItemPurchase(tMedicalItemPurchase);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTMedicalItemPurchase_CaseDataFound() {
        // given
        given(tMedicalItemPurchaseRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tMedicalItemPurchaseRepository.save(tMedicalItemPurchase))
                .thenReturn(tMedicalItemPurchase);
        tMedicalItemPurchaseServiceImpl.createTMedicalItemPurchase(tMedicalItemPurchase);

        // When
        tMedicalItemPurchaseServiceImpl.deleteTMedicalItemPurchase(0L);

        try {
            tMedicalItemPurchaseServiceImpl.getTMedicalItemPurchase(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTMedicalItemPurchase_CaseDataNotFound() {
        // given
        given(tMedicalItemPurchaseRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tMedicalItemPurchaseServiceImpl.deleteTMedicalItemPurchase(0L);

        try {
            tMedicalItemPurchaseServiceImpl.getTMedicalItemPurchase(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
