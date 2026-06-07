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

import com.github.amsatrio.spring_hospital.model.entity.TAppointmentDone;
import com.github.amsatrio.spring_hospital.repository.TAppointmentDoneRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TAppointmentDoneServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TAppointmentDoneServiceTests {

    @Mock
    private TAppointmentDoneRepository tAppointmentDoneRepository;

    @InjectMocks
    private TAppointmentDoneServiceImpl tAppointmentDoneServiceImpl;

    private TAppointmentDone tAppointmentDone = new TAppointmentDone();

    @BeforeEach
    public void setup() {
        tAppointmentDone.setId(0L);
        tAppointmentDone.setAppointmentId(0L);
        tAppointmentDone.setCreatedBy(0L);
        tAppointmentDone.setCreatedOn(new Date());
        tAppointmentDone.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTAppointmentDoneById_CaseDataFound() {
        // when
        when(tAppointmentDoneRepository.findById(0L))
                .thenReturn(Optional.of(tAppointmentDone));

        TAppointmentDone tAppointmentDoneDb = tAppointmentDoneServiceImpl.getTAppointmentDone(0L);

        // then
        assertEquals(tAppointmentDoneDb, tAppointmentDone);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTAppointmentDoneById_CaseDataNotFound() {
        // when
        when(tAppointmentDoneRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tAppointmentDoneServiceImpl.getTAppointmentDone(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTAppointmentDone_CaseDataNotFound() {
        // given
        given(tAppointmentDoneRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tAppointmentDoneRepository.save(tAppointmentDone))
                .thenReturn(tAppointmentDone);

        TAppointmentDone tAppointmentDoneDb = tAppointmentDoneServiceImpl.createTAppointmentDone(tAppointmentDone);

        // then
        assertEquals(tAppointmentDoneDb.getId(), tAppointmentDone.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTAppointmentDone_CaseDataFound() {
        // given
        given(tAppointmentDoneRepository.findById(0L))
                .willReturn(Optional.of(tAppointmentDone));

        try {
            tAppointmentDoneServiceImpl.createTAppointmentDone(tAppointmentDone);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTAppointmentDone_CaseDataFound() {
        // given
        given(tAppointmentDoneRepository.findById(0L))
                .willReturn(Optional.of(tAppointmentDone));

        // When
        tAppointmentDone.setAppointmentId(1L);
        when(tAppointmentDoneRepository.save(tAppointmentDone))
                .thenReturn(tAppointmentDone);

        TAppointmentDone tAppointmentDoneNew = tAppointmentDoneServiceImpl.updateTAppointmentDone(tAppointmentDone);

        // then
        assertEquals(tAppointmentDone.getAppointmentId(), tAppointmentDoneNew.getAppointmentId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTAppointmentDone_CaseDataNotFound() {
        // given
        given(tAppointmentDoneRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tAppointmentDoneServiceImpl.updateTAppointmentDone(tAppointmentDone);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTAppointmentDone_CaseDataFound() {
        // given
        given(tAppointmentDoneRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tAppointmentDoneRepository.save(tAppointmentDone))
                .thenReturn(tAppointmentDone);
        tAppointmentDoneServiceImpl.createTAppointmentDone(tAppointmentDone);

        // When
        tAppointmentDoneServiceImpl.deleteTAppointmentDone(0L);

        try {
            tAppointmentDoneServiceImpl.getTAppointmentDone(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTAppointmentDone_CaseDataNotFound() {
        // given
        given(tAppointmentDoneRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tAppointmentDoneServiceImpl.deleteTAppointmentDone(0L);

        try {
            tAppointmentDoneServiceImpl.getTAppointmentDone(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
