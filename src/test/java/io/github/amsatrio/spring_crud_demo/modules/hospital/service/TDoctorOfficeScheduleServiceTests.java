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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TDoctorOfficeSchedule;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TDoctorOfficeScheduleRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TDoctorOfficeScheduleServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TDoctorOfficeScheduleServiceTests {

    @Mock
    private TDoctorOfficeScheduleRepository tDoctorOfficeScheduleRepository;

    @InjectMocks
    private TDoctorOfficeScheduleServiceImpl tDoctorOfficeScheduleServiceImpl;

    private TDoctorOfficeSchedule tDoctorOfficeSchedule = new TDoctorOfficeSchedule();

    @BeforeEach
    public void setup() {
        tDoctorOfficeSchedule.setId(0L);
        tDoctorOfficeSchedule.setSlot(0);
        tDoctorOfficeSchedule.setCreatedBy(0L);
        tDoctorOfficeSchedule.setCreatedOn(new Date());
        tDoctorOfficeSchedule.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTDoctorOfficeScheduleById_CaseDataFound() {
        // when
        when(tDoctorOfficeScheduleRepository.findById(0L))
                .thenReturn(Optional.of(tDoctorOfficeSchedule));

        TDoctorOfficeSchedule tDoctorOfficeScheduleDb = tDoctorOfficeScheduleServiceImpl.getTDoctorOfficeSchedule(0L);

        // then
        assertEquals(tDoctorOfficeScheduleDb, tDoctorOfficeSchedule);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTDoctorOfficeScheduleById_CaseDataNotFound() {
        // when
        when(tDoctorOfficeScheduleRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tDoctorOfficeScheduleServiceImpl.getTDoctorOfficeSchedule(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTDoctorOfficeSchedule_CaseDataNotFound() {
        // given
        given(tDoctorOfficeScheduleRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tDoctorOfficeScheduleRepository.save(tDoctorOfficeSchedule))
                .thenReturn(tDoctorOfficeSchedule);

        TDoctorOfficeSchedule tDoctorOfficeScheduleDb = tDoctorOfficeScheduleServiceImpl.createTDoctorOfficeSchedule(tDoctorOfficeSchedule);

        // then
        assertEquals(tDoctorOfficeScheduleDb.getId(), tDoctorOfficeSchedule.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTDoctorOfficeSchedule_CaseDataFound() {
        // given
        given(tDoctorOfficeScheduleRepository.findById(0L))
                .willReturn(Optional.of(tDoctorOfficeSchedule));

        try {
            tDoctorOfficeScheduleServiceImpl.createTDoctorOfficeSchedule(tDoctorOfficeSchedule);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTDoctorOfficeSchedule_CaseDataFound() {
        // given
        given(tDoctorOfficeScheduleRepository.findById(0L))
                .willReturn(Optional.of(tDoctorOfficeSchedule));

        // When
        tDoctorOfficeSchedule.setSlot(1);
        when(tDoctorOfficeScheduleRepository.save(tDoctorOfficeSchedule))
                .thenReturn(tDoctorOfficeSchedule);

        TDoctorOfficeSchedule tDoctorOfficeScheduleNew = tDoctorOfficeScheduleServiceImpl.updateTDoctorOfficeSchedule(tDoctorOfficeSchedule);

        // then
        assertEquals(tDoctorOfficeSchedule.getSlot(), tDoctorOfficeScheduleNew.getSlot());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTDoctorOfficeSchedule_CaseDataNotFound() {
        // given
        given(tDoctorOfficeScheduleRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tDoctorOfficeScheduleServiceImpl.updateTDoctorOfficeSchedule(tDoctorOfficeSchedule);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTDoctorOfficeSchedule_CaseDataFound() {
        // given
        given(tDoctorOfficeScheduleRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tDoctorOfficeScheduleRepository.save(tDoctorOfficeSchedule))
                .thenReturn(tDoctorOfficeSchedule);
        tDoctorOfficeScheduleServiceImpl.createTDoctorOfficeSchedule(tDoctorOfficeSchedule);

        // When
        tDoctorOfficeScheduleServiceImpl.deleteTDoctorOfficeSchedule(0L);

        try {
            tDoctorOfficeScheduleServiceImpl.getTDoctorOfficeSchedule(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTDoctorOfficeSchedule_CaseDataNotFound() {
        // given
        given(tDoctorOfficeScheduleRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tDoctorOfficeScheduleServiceImpl.deleteTDoctorOfficeSchedule(0L);

        try {
            tDoctorOfficeScheduleServiceImpl.getTDoctorOfficeSchedule(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
