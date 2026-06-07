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

import com.github.amsatrio.spring_hospital.model.entity.TCustomerCustomNominal;
import com.github.amsatrio.spring_hospital.repository.TCustomerCustomNominalRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerCustomNominalServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerCustomNominalServiceTests {

    @Mock
    private TCustomerCustomNominalRepository tCustomerCustomNominalRepository;

    @InjectMocks
    private TCustomerCustomNominalServiceImpl tCustomerCustomNominalServiceImpl;

    private TCustomerCustomNominal tCustomerCustomNominal = new TCustomerCustomNominal();

    @BeforeEach
    public void setup() {
        tCustomerCustomNominal.setId(0L);
        tCustomerCustomNominal.setNominal(0);
        tCustomerCustomNominal.setCreatedBy(0L);
        tCustomerCustomNominal.setCreatedOn(new Date());
        tCustomerCustomNominal.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerCustomNominalById_CaseDataFound() {
        // when
        when(tCustomerCustomNominalRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerCustomNominal));

        TCustomerCustomNominal tCustomerCustomNominalDb = tCustomerCustomNominalServiceImpl.getTCustomerCustomNominal(0L);

        // then
        assertEquals(tCustomerCustomNominalDb, tCustomerCustomNominal);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerCustomNominalById_CaseDataNotFound() {
        // when
        when(tCustomerCustomNominalRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerCustomNominalServiceImpl.getTCustomerCustomNominal(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerCustomNominal_CaseDataNotFound() {
        // given
        given(tCustomerCustomNominalRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerCustomNominalRepository.save(tCustomerCustomNominal))
                .thenReturn(tCustomerCustomNominal);

        TCustomerCustomNominal tCustomerCustomNominalDb = tCustomerCustomNominalServiceImpl.createTCustomerCustomNominal(tCustomerCustomNominal);

        // then
        assertEquals(tCustomerCustomNominalDb.getId(), tCustomerCustomNominal.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerCustomNominal_CaseDataFound() {
        // given
        given(tCustomerCustomNominalRepository.findById(0L))
                .willReturn(Optional.of(tCustomerCustomNominal));

        try {
            tCustomerCustomNominalServiceImpl.createTCustomerCustomNominal(tCustomerCustomNominal);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerCustomNominal_CaseDataFound() {
        // given
        given(tCustomerCustomNominalRepository.findById(0L))
                .willReturn(Optional.of(tCustomerCustomNominal));

        // When
        tCustomerCustomNominal.setNominal(1);
        when(tCustomerCustomNominalRepository.save(tCustomerCustomNominal))
                .thenReturn(tCustomerCustomNominal);

        TCustomerCustomNominal tCustomerCustomNominalNew = tCustomerCustomNominalServiceImpl.updateTCustomerCustomNominal(tCustomerCustomNominal);

        // then
        assertEquals(tCustomerCustomNominal.getNominal(), tCustomerCustomNominalNew.getNominal());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerCustomNominal_CaseDataNotFound() {
        // given
        given(tCustomerCustomNominalRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerCustomNominalServiceImpl.updateTCustomerCustomNominal(tCustomerCustomNominal);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerCustomNominal_CaseDataFound() {
        // given
        given(tCustomerCustomNominalRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerCustomNominalRepository.save(tCustomerCustomNominal))
                .thenReturn(tCustomerCustomNominal);
        tCustomerCustomNominalServiceImpl.createTCustomerCustomNominal(tCustomerCustomNominal);

        // When
        tCustomerCustomNominalServiceImpl.deleteTCustomerCustomNominal(0L);

        try {
            tCustomerCustomNominalServiceImpl.getTCustomerCustomNominal(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerCustomNominal_CaseDataNotFound() {
        // given
        given(tCustomerCustomNominalRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerCustomNominalServiceImpl.deleteTCustomerCustomNominal(0L);

        try {
            tCustomerCustomNominalServiceImpl.getTCustomerCustomNominal(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
