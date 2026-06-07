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

import com.github.amsatrio.spring_hospital.model.entity.TAppointmentRescheduleHistory;
import com.github.amsatrio.spring_hospital.repository.TAppointmentRescheduleHistoryRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TAppointmentRescheduleHistoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TAppointmentRescheduleHistoryServiceTests {

    @Mock
    private TAppointmentRescheduleHistoryRepository tAppointmentRescheduleHistoryRepository;

    @InjectMocks
    private TAppointmentRescheduleHistoryServiceImpl tAppointmentRescheduleHistoryServiceImpl;

    private TAppointmentRescheduleHistory tAppointmentRescheduleHistory = new TAppointmentRescheduleHistory();

    @BeforeEach
    public void setup() {
        tAppointmentRescheduleHistory.setId(0L);
        tAppointmentRescheduleHistory.setAppointmentDate(new Date());
        tAppointmentRescheduleHistory.setCreatedBy(0L);
        tAppointmentRescheduleHistory.setCreatedOn(new Date());
        tAppointmentRescheduleHistory.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTAppointmentRescheduleHistoryById_CaseDataFound() {
        // when
        when(tAppointmentRescheduleHistoryRepository.findById(0L))
                .thenReturn(Optional.of(tAppointmentRescheduleHistory));

        TAppointmentRescheduleHistory tAppointmentRescheduleHistoryDb = tAppointmentRescheduleHistoryServiceImpl.getTAppointmentRescheduleHistory(0L);

        // then
        assertEquals(tAppointmentRescheduleHistoryDb, tAppointmentRescheduleHistory);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTAppointmentRescheduleHistoryById_CaseDataNotFound() {
        // when
        when(tAppointmentRescheduleHistoryRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tAppointmentRescheduleHistoryServiceImpl.getTAppointmentRescheduleHistory(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTAppointmentRescheduleHistory_CaseDataNotFound() {
        // given
        given(tAppointmentRescheduleHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tAppointmentRescheduleHistoryRepository.save(tAppointmentRescheduleHistory))
                .thenReturn(tAppointmentRescheduleHistory);

        TAppointmentRescheduleHistory tAppointmentRescheduleHistoryDb = tAppointmentRescheduleHistoryServiceImpl.createTAppointmentRescheduleHistory(tAppointmentRescheduleHistory);

        // then
        assertEquals(tAppointmentRescheduleHistoryDb.getId(), tAppointmentRescheduleHistory.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTAppointmentRescheduleHistory_CaseDataFound() {
        // given
        given(tAppointmentRescheduleHistoryRepository.findById(0L))
                .willReturn(Optional.of(tAppointmentRescheduleHistory));

        try {
            tAppointmentRescheduleHistoryServiceImpl.createTAppointmentRescheduleHistory(tAppointmentRescheduleHistory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTAppointmentRescheduleHistory_CaseDataFound() {
        // given
        given(tAppointmentRescheduleHistoryRepository.findById(0L))
                .willReturn(Optional.of(tAppointmentRescheduleHistory));

        // When
        tAppointmentRescheduleHistory.setAppointmentDate(new Date());
        when(tAppointmentRescheduleHistoryRepository.save(tAppointmentRescheduleHistory))
                .thenReturn(tAppointmentRescheduleHistory);

        TAppointmentRescheduleHistory tAppointmentRescheduleHistoryNew = tAppointmentRescheduleHistoryServiceImpl.updateTAppointmentRescheduleHistory(tAppointmentRescheduleHistory);

        // then
        assertEquals(tAppointmentRescheduleHistory.getAppointmentDate(), tAppointmentRescheduleHistoryNew.getAppointmentDate());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTAppointmentRescheduleHistory_CaseDataNotFound() {
        // given
        given(tAppointmentRescheduleHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tAppointmentRescheduleHistoryServiceImpl.updateTAppointmentRescheduleHistory(tAppointmentRescheduleHistory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTAppointmentRescheduleHistory_CaseDataFound() {
        // given
        given(tAppointmentRescheduleHistoryRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tAppointmentRescheduleHistoryRepository.save(tAppointmentRescheduleHistory))
                .thenReturn(tAppointmentRescheduleHistory);
        tAppointmentRescheduleHistoryServiceImpl.createTAppointmentRescheduleHistory(tAppointmentRescheduleHistory);

        // When
        tAppointmentRescheduleHistoryServiceImpl.deleteTAppointmentRescheduleHistory(0L);

        try {
            tAppointmentRescheduleHistoryServiceImpl.getTAppointmentRescheduleHistory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTAppointmentRescheduleHistory_CaseDataNotFound() {
        // given
        given(tAppointmentRescheduleHistoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tAppointmentRescheduleHistoryServiceImpl.deleteTAppointmentRescheduleHistory(0L);

        try {
            tAppointmentRescheduleHistoryServiceImpl.getTAppointmentRescheduleHistory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
