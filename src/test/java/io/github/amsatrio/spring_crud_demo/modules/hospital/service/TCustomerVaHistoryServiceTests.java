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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerVaHistory;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TCustomerVaHistoryRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerVaHistoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerVaHistoryServiceTests {

    @Mock
    private TCustomerVaHistoryRepository tCustomerVaHistoryRepository;

    @InjectMocks
    private TCustomerVaHistoryServiceImpl tCustomerVaHistoryServiceImpl;

    private TCustomerVaHistory tCustomerVaHistory = new TCustomerVaHistory();

    @BeforeEach
    public void setup() {
        tCustomerVaHistory.setId(0L);
        tCustomerVaHistory.setExpiredOn(new Date());
        tCustomerVaHistory.setCreatedBy(0L);
        tCustomerVaHistory.setCreatedOn(new Date());
        tCustomerVaHistory.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerVaHistoryById_CaseDataFound() {
        // when
        when(tCustomerVaHistoryRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerVaHistory));

        TCustomerVaHistory tCustomerVaHistoryDb = tCustomerVaHistoryServiceImpl.getTCustomerVaHistory(0L);

        // then
        assertEquals(tCustomerVaHistoryDb, tCustomerVaHistory);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerVaHistoryById_CaseDataNotFound() {
        // when
        when(tCustomerVaHistoryRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerVaHistoryServiceImpl.getTCustomerVaHistory(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerVaHistory_CaseDataNotFound() {
        // given
        given(tCustomerVaHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerVaHistoryRepository.save(tCustomerVaHistory))
                .thenReturn(tCustomerVaHistory);

        TCustomerVaHistory tCustomerVaHistoryDb = tCustomerVaHistoryServiceImpl.createTCustomerVaHistory(tCustomerVaHistory);

        // then
        assertEquals(tCustomerVaHistoryDb.getId(), tCustomerVaHistory.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerVaHistory_CaseDataFound() {
        // given
        given(tCustomerVaHistoryRepository.findById(0L))
                .willReturn(Optional.of(tCustomerVaHistory));

        try {
            tCustomerVaHistoryServiceImpl.createTCustomerVaHistory(tCustomerVaHistory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerVaHistory_CaseDataFound() {
        // given
        given(tCustomerVaHistoryRepository.findById(0L))
                .willReturn(Optional.of(tCustomerVaHistory));

        // When
        tCustomerVaHistory.setExpiredOn(new Date());
        when(tCustomerVaHistoryRepository.save(tCustomerVaHistory))
                .thenReturn(tCustomerVaHistory);

        TCustomerVaHistory tCustomerVaHistoryNew = tCustomerVaHistoryServiceImpl.updateTCustomerVaHistory(tCustomerVaHistory);

        // then
        assertEquals(tCustomerVaHistory.getExpiredOn(), tCustomerVaHistoryNew.getExpiredOn());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerVaHistory_CaseDataNotFound() {
        // given
        given(tCustomerVaHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerVaHistoryServiceImpl.updateTCustomerVaHistory(tCustomerVaHistory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerVaHistory_CaseDataFound() {
        // given
        given(tCustomerVaHistoryRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerVaHistoryRepository.save(tCustomerVaHistory))
                .thenReturn(tCustomerVaHistory);
        tCustomerVaHistoryServiceImpl.createTCustomerVaHistory(tCustomerVaHistory);

        // When
        tCustomerVaHistoryServiceImpl.deleteTCustomerVaHistory(0L);

        try {
            tCustomerVaHistoryServiceImpl.getTCustomerVaHistory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerVaHistory_CaseDataNotFound() {
        // given
        given(tCustomerVaHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerVaHistoryServiceImpl.deleteTCustomerVaHistory(0L);

        try {
            tCustomerVaHistoryServiceImpl.getTCustomerVaHistory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
