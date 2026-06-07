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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TTreatmentDiscount;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TTreatmentDiscountRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TTreatmentDiscountServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TTreatmentDiscountServiceTests {

    @Mock
    private TTreatmentDiscountRepository tTreatmentDiscountRepository;

    @InjectMocks
    private TTreatmentDiscountServiceImpl tTreatmentDiscountServiceImpl;

    private TTreatmentDiscount tTreatmentDiscount = new TTreatmentDiscount();

    @BeforeEach
    public void setup() {
        tTreatmentDiscount.setId(0L);
        tTreatmentDiscount.setValue(0F);
        tTreatmentDiscount.setCreatedBy(0L);
        tTreatmentDiscount.setCreatedOn(new Date());
        tTreatmentDiscount.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTTreatmentDiscountById_CaseDataFound() {
        // when
        when(tTreatmentDiscountRepository.findById(0L))
                .thenReturn(Optional.of(tTreatmentDiscount));

        TTreatmentDiscount tTreatmentDiscountDb = tTreatmentDiscountServiceImpl.getTTreatmentDiscount(0L);

        // then
        assertEquals(tTreatmentDiscountDb, tTreatmentDiscount);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTTreatmentDiscountById_CaseDataNotFound() {
        // when
        when(tTreatmentDiscountRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tTreatmentDiscountServiceImpl.getTTreatmentDiscount(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTTreatmentDiscount_CaseDataNotFound() {
        // given
        given(tTreatmentDiscountRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tTreatmentDiscountRepository.save(tTreatmentDiscount))
                .thenReturn(tTreatmentDiscount);

        TTreatmentDiscount tTreatmentDiscountDb = tTreatmentDiscountServiceImpl.createTTreatmentDiscount(tTreatmentDiscount);

        // then
        assertEquals(tTreatmentDiscountDb.getId(), tTreatmentDiscount.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTTreatmentDiscount_CaseDataFound() {
        // given
        given(tTreatmentDiscountRepository.findById(0L))
                .willReturn(Optional.of(tTreatmentDiscount));

        try {
            tTreatmentDiscountServiceImpl.createTTreatmentDiscount(tTreatmentDiscount);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTTreatmentDiscount_CaseDataFound() {
        // given
        given(tTreatmentDiscountRepository.findById(0L))
                .willReturn(Optional.of(tTreatmentDiscount));

        // When
        tTreatmentDiscount.setValue(1F);
        when(tTreatmentDiscountRepository.save(tTreatmentDiscount))
                .thenReturn(tTreatmentDiscount);

        TTreatmentDiscount tTreatmentDiscountNew = tTreatmentDiscountServiceImpl.updateTTreatmentDiscount(tTreatmentDiscount);

        // then
        assertEquals(tTreatmentDiscount.getValue(), tTreatmentDiscountNew.getValue());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTTreatmentDiscount_CaseDataNotFound() {
        // given
        given(tTreatmentDiscountRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tTreatmentDiscountServiceImpl.updateTTreatmentDiscount(tTreatmentDiscount);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTTreatmentDiscount_CaseDataFound() {
        // given
        given(tTreatmentDiscountRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tTreatmentDiscountRepository.save(tTreatmentDiscount))
                .thenReturn(tTreatmentDiscount);
        tTreatmentDiscountServiceImpl.createTTreatmentDiscount(tTreatmentDiscount);

        // When
        tTreatmentDiscountServiceImpl.deleteTTreatmentDiscount(0L);

        try {
            tTreatmentDiscountServiceImpl.getTTreatmentDiscount(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTTreatmentDiscount_CaseDataNotFound() {
        // given
        given(tTreatmentDiscountRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tTreatmentDiscountServiceImpl.deleteTTreatmentDiscount(0L);

        try {
            tTreatmentDiscountServiceImpl.getTTreatmentDiscount(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
