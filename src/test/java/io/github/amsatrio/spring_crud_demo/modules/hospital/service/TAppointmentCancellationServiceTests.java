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

import com.github.amsatrio.spring_hospital.model.entity.TAppointmentCancellation;
import com.github.amsatrio.spring_hospital.repository.TAppointmentCancellationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TAppointmentCancellationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TAppointmentCancellationServiceTests {

    @Mock
    private TAppointmentCancellationRepository tAppointmentCancellationRepository;

    @InjectMocks
    private TAppointmentCancellationServiceImpl tAppointmentCancellationServiceImpl;

    private TAppointmentCancellation tAppointmentCancellation = new TAppointmentCancellation();

    @BeforeEach
    public void setup() {
        tAppointmentCancellation.setId(0L);
        tAppointmentCancellation.setAppointmentId(0L);
        tAppointmentCancellation.setCreatedBy(0L);
        tAppointmentCancellation.setCreatedOn(new Date());
        tAppointmentCancellation.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTAppointmentCancellationById_CaseDataFound() {
        // when
        when(tAppointmentCancellationRepository.findById(0L))
                .thenReturn(Optional.of(tAppointmentCancellation));

        TAppointmentCancellation tAppointmentCancellationDb = tAppointmentCancellationServiceImpl.getTAppointmentCancellation(0L);

        // then
        assertEquals(tAppointmentCancellationDb, tAppointmentCancellation);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTAppointmentCancellationById_CaseDataNotFound() {
        // when
        when(tAppointmentCancellationRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tAppointmentCancellationServiceImpl.getTAppointmentCancellation(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTAppointmentCancellation_CaseDataNotFound() {
        // given
        given(tAppointmentCancellationRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tAppointmentCancellationRepository.save(tAppointmentCancellation))
                .thenReturn(tAppointmentCancellation);

        TAppointmentCancellation tAppointmentCancellationDb = tAppointmentCancellationServiceImpl.createTAppointmentCancellation(tAppointmentCancellation);

        // then
        assertEquals(tAppointmentCancellationDb.getId(), tAppointmentCancellation.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTAppointmentCancellation_CaseDataFound() {
        // given
        given(tAppointmentCancellationRepository.findById(0L))
                .willReturn(Optional.of(tAppointmentCancellation));

        try {
            tAppointmentCancellationServiceImpl.createTAppointmentCancellation(tAppointmentCancellation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTAppointmentCancellation_CaseDataFound() {
        // given
        given(tAppointmentCancellationRepository.findById(0L))
                .willReturn(Optional.of(tAppointmentCancellation));

        // When
        tAppointmentCancellation.setAppointmentId(1L);
        when(tAppointmentCancellationRepository.save(tAppointmentCancellation))
                .thenReturn(tAppointmentCancellation);

        TAppointmentCancellation tAppointmentCancellationNew = tAppointmentCancellationServiceImpl.updateTAppointmentCancellation(tAppointmentCancellation);

        // then
        assertEquals(tAppointmentCancellation.getAppointmentId(), tAppointmentCancellationNew.getAppointmentId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTAppointmentCancellation_CaseDataNotFound() {
        // given
        given(tAppointmentCancellationRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tAppointmentCancellationServiceImpl.updateTAppointmentCancellation(tAppointmentCancellation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTAppointmentCancellation_CaseDataFound() {
        // given
        given(tAppointmentCancellationRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tAppointmentCancellationRepository.save(tAppointmentCancellation))
                .thenReturn(tAppointmentCancellation);
        tAppointmentCancellationServiceImpl.createTAppointmentCancellation(tAppointmentCancellation);

        // When
        tAppointmentCancellationServiceImpl.deleteTAppointmentCancellation(0L);

        try {
            tAppointmentCancellationServiceImpl.getTAppointmentCancellation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTAppointmentCancellation_CaseDataNotFound() {
        // given
        given(tAppointmentCancellationRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tAppointmentCancellationServiceImpl.deleteTAppointmentCancellation(0L);

        try {
            tAppointmentCancellationServiceImpl.getTAppointmentCancellation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
