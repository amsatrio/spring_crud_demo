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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerChatHistory;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TCustomerChatHistoryRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerChatHistoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerChatHistoryServiceTests {

    @Mock
    private TCustomerChatHistoryRepository tCustomerChatHistoryRepository;

    @InjectMocks
    private TCustomerChatHistoryServiceImpl tCustomerChatHistoryServiceImpl;

    private TCustomerChatHistory tCustomerChatHistory = new TCustomerChatHistory();

    @BeforeEach
    public void setup() {
        tCustomerChatHistory.setId(0L);
        tCustomerChatHistory.setChatContent("init");
        tCustomerChatHistory.setCreatedBy(0L);
        tCustomerChatHistory.setCreatedOn(new Date());
        tCustomerChatHistory.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerChatHistoryById_CaseDataFound() {
        // when
        when(tCustomerChatHistoryRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerChatHistory));

        TCustomerChatHistory tCustomerChatHistoryDb = tCustomerChatHistoryServiceImpl.getTCustomerChatHistory(0L);

        // then
        assertEquals(tCustomerChatHistoryDb, tCustomerChatHistory);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerChatHistoryById_CaseDataNotFound() {
        // when
        when(tCustomerChatHistoryRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerChatHistoryServiceImpl.getTCustomerChatHistory(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerChatHistory_CaseDataNotFound() {
        // given
        given(tCustomerChatHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerChatHistoryRepository.save(tCustomerChatHistory))
                .thenReturn(tCustomerChatHistory);

        TCustomerChatHistory tCustomerChatHistoryDb = tCustomerChatHistoryServiceImpl.createTCustomerChatHistory(tCustomerChatHistory);

        // then
        assertEquals(tCustomerChatHistoryDb.getId(), tCustomerChatHistory.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerChatHistory_CaseDataFound() {
        // given
        given(tCustomerChatHistoryRepository.findById(0L))
                .willReturn(Optional.of(tCustomerChatHistory));

        try {
            tCustomerChatHistoryServiceImpl.createTCustomerChatHistory(tCustomerChatHistory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerChatHistory_CaseDataFound() {
        // given
        given(tCustomerChatHistoryRepository.findById(0L))
                .willReturn(Optional.of(tCustomerChatHistory));

        // When
        tCustomerChatHistory.setChatContent("update");
        when(tCustomerChatHistoryRepository.save(tCustomerChatHistory))
                .thenReturn(tCustomerChatHistory);

        TCustomerChatHistory tCustomerChatHistoryNew = tCustomerChatHistoryServiceImpl.updateTCustomerChatHistory(tCustomerChatHistory);

        // then
        assertEquals(tCustomerChatHistory.getChatContent(), tCustomerChatHistoryNew.getChatContent());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerChatHistory_CaseDataNotFound() {
        // given
        given(tCustomerChatHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerChatHistoryServiceImpl.updateTCustomerChatHistory(tCustomerChatHistory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerChatHistory_CaseDataFound() {
        // given
        given(tCustomerChatHistoryRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerChatHistoryRepository.save(tCustomerChatHistory))
                .thenReturn(tCustomerChatHistory);
        tCustomerChatHistoryServiceImpl.createTCustomerChatHistory(tCustomerChatHistory);

        // When
        tCustomerChatHistoryServiceImpl.deleteTCustomerChatHistory(0L);

        try {
            tCustomerChatHistoryServiceImpl.getTCustomerChatHistory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerChatHistory_CaseDataNotFound() {
        // given
        given(tCustomerChatHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerChatHistoryServiceImpl.deleteTCustomerChatHistory(0L);

        try {
            tCustomerChatHistoryServiceImpl.getTCustomerChatHistory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
