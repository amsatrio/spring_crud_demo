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

import com.github.amsatrio.spring_hospital.model.entity.TAppointment;
import com.github.amsatrio.spring_hospital.repository.TAppointmentRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TAppointmentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TAppointmentServiceTests {

    @Mock
    private TAppointmentRepository tAppointmentRepository;

    @InjectMocks
    private TAppointmentServiceImpl tAppointmentServiceImpl;

    private TAppointment tAppointment = new TAppointment();

    @BeforeEach
    public void setup() {
        tAppointment.setId(0L);
        tAppointment.setAppointmentDate(new Date());
        tAppointment.setCreatedBy(0L);
        tAppointment.setCreatedOn(new Date());
        tAppointment.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTAppointmentById_CaseDataFound() {
        // when
        when(tAppointmentRepository.findById(0L))
                .thenReturn(Optional.of(tAppointment));

        TAppointment tAppointmentDb = tAppointmentServiceImpl.getTAppointment(0L);

        // then
        assertEquals(tAppointmentDb, tAppointment);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTAppointmentById_CaseDataNotFound() {
        // when
        when(tAppointmentRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tAppointmentServiceImpl.getTAppointment(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTAppointment_CaseDataNotFound() {
        // given
        given(tAppointmentRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tAppointmentRepository.save(tAppointment))
                .thenReturn(tAppointment);

        TAppointment tAppointmentDb = tAppointmentServiceImpl.createTAppointment(tAppointment);

        // then
        assertEquals(tAppointmentDb.getId(), tAppointment.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTAppointment_CaseDataFound() {
        // given
        given(tAppointmentRepository.findById(0L))
                .willReturn(Optional.of(tAppointment));

        try {
            tAppointmentServiceImpl.createTAppointment(tAppointment);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTAppointment_CaseDataFound() {
        // given
        given(tAppointmentRepository.findById(0L))
                .willReturn(Optional.of(tAppointment));

        // When
        tAppointment.setAppointmentDate(new Date());
        when(tAppointmentRepository.save(tAppointment))
                .thenReturn(tAppointment);

        TAppointment tAppointmentNew = tAppointmentServiceImpl.updateTAppointment(tAppointment);

        // then
        assertEquals(tAppointment.getAppointmentDate(), tAppointmentNew.getAppointmentDate());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTAppointment_CaseDataNotFound() {
        // given
        given(tAppointmentRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tAppointmentServiceImpl.updateTAppointment(tAppointment);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTAppointment_CaseDataFound() {
        // given
        given(tAppointmentRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tAppointmentRepository.save(tAppointment))
                .thenReturn(tAppointment);
        tAppointmentServiceImpl.createTAppointment(tAppointment);

        // When
        tAppointmentServiceImpl.deleteTAppointment(0L);

        try {
            tAppointmentServiceImpl.getTAppointment(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTAppointment_CaseDataNotFound() {
        // given
        given(tAppointmentRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tAppointmentServiceImpl.deleteTAppointment(0L);

        try {
            tAppointmentServiceImpl.getTAppointment(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
