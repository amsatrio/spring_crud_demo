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

import com.github.amsatrio.spring_hospital.model.entity.TCustomerVa;
import com.github.amsatrio.spring_hospital.repository.TCustomerVaRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerVaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerVaServiceTests {

    @Mock
    private TCustomerVaRepository tCustomerVaRepository;

    @InjectMocks
    private TCustomerVaServiceImpl tCustomerVaServiceImpl;

    private TCustomerVa tCustomerVa = new TCustomerVa();

    @BeforeEach
    public void setup() {
        tCustomerVa.setId(0L);
        tCustomerVa.setVaNumber("init");
        tCustomerVa.setCreatedBy(0L);
        tCustomerVa.setCreatedOn(new Date());
        tCustomerVa.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerVaById_CaseDataFound() {
        // when
        when(tCustomerVaRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerVa));

        TCustomerVa tCustomerVaDb = tCustomerVaServiceImpl.getTCustomerVa(0L);

        // then
        assertEquals(tCustomerVaDb, tCustomerVa);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerVaById_CaseDataNotFound() {
        // when
        when(tCustomerVaRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerVaServiceImpl.getTCustomerVa(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerVa_CaseDataNotFound() {
        // given
        given(tCustomerVaRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerVaRepository.save(tCustomerVa))
                .thenReturn(tCustomerVa);

        TCustomerVa tCustomerVaDb = tCustomerVaServiceImpl.createTCustomerVa(tCustomerVa);

        // then
        assertEquals(tCustomerVaDb.getId(), tCustomerVa.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerVa_CaseDataFound() {
        // given
        given(tCustomerVaRepository.findById(0L))
                .willReturn(Optional.of(tCustomerVa));

        try {
            tCustomerVaServiceImpl.createTCustomerVa(tCustomerVa);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerVa_CaseDataFound() {
        // given
        given(tCustomerVaRepository.findById(0L))
                .willReturn(Optional.of(tCustomerVa));

        // When
        tCustomerVa.setVaNumber("update");
        when(tCustomerVaRepository.save(tCustomerVa))
                .thenReturn(tCustomerVa);

        TCustomerVa tCustomerVaNew = tCustomerVaServiceImpl.updateTCustomerVa(tCustomerVa);

        // then
        assertEquals(tCustomerVa.getVaNumber(), tCustomerVaNew.getVaNumber());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerVa_CaseDataNotFound() {
        // given
        given(tCustomerVaRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerVaServiceImpl.updateTCustomerVa(tCustomerVa);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerVa_CaseDataFound() {
        // given
        given(tCustomerVaRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerVaRepository.save(tCustomerVa))
                .thenReturn(tCustomerVa);
        tCustomerVaServiceImpl.createTCustomerVa(tCustomerVa);

        // When
        tCustomerVaServiceImpl.deleteTCustomerVa(0L);

        try {
            tCustomerVaServiceImpl.getTCustomerVa(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerVa_CaseDataNotFound() {
        // given
        given(tCustomerVaRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerVaServiceImpl.deleteTCustomerVa(0L);

        try {
            tCustomerVaServiceImpl.getTCustomerVa(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
