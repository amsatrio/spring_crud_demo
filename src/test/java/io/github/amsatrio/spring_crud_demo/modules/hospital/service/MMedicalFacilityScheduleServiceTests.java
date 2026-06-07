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

import com.github.amsatrio.spring_hospital.model.entity.MMedicalFacilitySchedule;
import com.github.amsatrio.spring_hospital.repository.MMedicalFacilityScheduleRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MMedicalFacilityScheduleServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MMedicalFacilityScheduleServiceTests {

    @Mock
    private MMedicalFacilityScheduleRepository mMedicalFacilityScheduleRepository;

    @InjectMocks
    private MMedicalFacilityScheduleServiceImpl mMedicalFacilityScheduleServiceImpl;

    private MMedicalFacilitySchedule mMedicalFacilitySchedule = new MMedicalFacilitySchedule();

    @BeforeEach
    public void setup() {
        mMedicalFacilitySchedule.setId(0L);
        mMedicalFacilitySchedule.setTimeScheduleEnd("init");
        mMedicalFacilitySchedule.setCreatedBy(0L);
        mMedicalFacilitySchedule.setCreatedOn(new Date());
        mMedicalFacilitySchedule.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMMedicalFacilityScheduleById_CaseDataFound() {
        // when
        when(mMedicalFacilityScheduleRepository.findById(0L))
                .thenReturn(Optional.of(mMedicalFacilitySchedule));

        MMedicalFacilitySchedule mMedicalFacilityScheduleDb = mMedicalFacilityScheduleServiceImpl.getMMedicalFacilitySchedule(0L);

        // then
        assertEquals(mMedicalFacilityScheduleDb, mMedicalFacilitySchedule);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMMedicalFacilityScheduleById_CaseDataNotFound() {
        // when
        when(mMedicalFacilityScheduleRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mMedicalFacilityScheduleServiceImpl.getMMedicalFacilitySchedule(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMMedicalFacilitySchedule_CaseDataNotFound() {
        // given
        given(mMedicalFacilityScheduleRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mMedicalFacilityScheduleRepository.save(mMedicalFacilitySchedule))
                .thenReturn(mMedicalFacilitySchedule);

        MMedicalFacilitySchedule mMedicalFacilityScheduleDb = mMedicalFacilityScheduleServiceImpl.createMMedicalFacilitySchedule(mMedicalFacilitySchedule);

        // then
        assertEquals(mMedicalFacilityScheduleDb.getId(), mMedicalFacilitySchedule.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMMedicalFacilitySchedule_CaseDataFound() {
        // given
        given(mMedicalFacilityScheduleRepository.findById(0L))
                .willReturn(Optional.of(mMedicalFacilitySchedule));

        try {
            mMedicalFacilityScheduleServiceImpl.createMMedicalFacilitySchedule(mMedicalFacilitySchedule);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMMedicalFacilitySchedule_CaseDataFound() {
        // given
        given(mMedicalFacilityScheduleRepository.findById(0L))
                .willReturn(Optional.of(mMedicalFacilitySchedule));

        // When
        mMedicalFacilitySchedule.setTimeScheduleEnd("update");
        when(mMedicalFacilityScheduleRepository.save(mMedicalFacilitySchedule))
                .thenReturn(mMedicalFacilitySchedule);

        MMedicalFacilitySchedule mMedicalFacilityScheduleNew = mMedicalFacilityScheduleServiceImpl.updateMMedicalFacilitySchedule(mMedicalFacilitySchedule);

        // then
        assertEquals(mMedicalFacilitySchedule.getTimeScheduleEnd(), mMedicalFacilityScheduleNew.getTimeScheduleEnd());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMMedicalFacilitySchedule_CaseDataNotFound() {
        // given
        given(mMedicalFacilityScheduleRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mMedicalFacilityScheduleServiceImpl.updateMMedicalFacilitySchedule(mMedicalFacilitySchedule);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMMedicalFacilitySchedule_CaseDataFound() {
        // given
        given(mMedicalFacilityScheduleRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mMedicalFacilityScheduleRepository.save(mMedicalFacilitySchedule))
                .thenReturn(mMedicalFacilitySchedule);
        mMedicalFacilityScheduleServiceImpl.createMMedicalFacilitySchedule(mMedicalFacilitySchedule);

        // When
        mMedicalFacilityScheduleServiceImpl.deleteMMedicalFacilitySchedule(0L);

        try {
            mMedicalFacilityScheduleServiceImpl.getMMedicalFacilitySchedule(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMMedicalFacilitySchedule_CaseDataNotFound() {
        // given
        given(mMedicalFacilityScheduleRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mMedicalFacilityScheduleServiceImpl.deleteMMedicalFacilitySchedule(0L);

        try {
            mMedicalFacilityScheduleServiceImpl.getMMedicalFacilitySchedule(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
