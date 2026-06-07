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

import com.github.amsatrio.spring_hospital.model.entity.TDoctorOfficeTreatment;
import com.github.amsatrio.spring_hospital.repository.TDoctorOfficeTreatmentRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TDoctorOfficeTreatmentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TDoctorOfficeTreatmentServiceTests {

    @Mock
    private TDoctorOfficeTreatmentRepository tDoctorOfficeTreatmentRepository;

    @InjectMocks
    private TDoctorOfficeTreatmentServiceImpl tDoctorOfficeTreatmentServiceImpl;

    private TDoctorOfficeTreatment tDoctorOfficeTreatment = new TDoctorOfficeTreatment();

    @BeforeEach
    public void setup() {
        tDoctorOfficeTreatment.setId(0L);
        tDoctorOfficeTreatment.setDoctorOfficeId(0L);
        tDoctorOfficeTreatment.setCreatedBy(0L);
        tDoctorOfficeTreatment.setCreatedOn(new Date());
        tDoctorOfficeTreatment.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTDoctorOfficeTreatmentById_CaseDataFound() {
        // when
        when(tDoctorOfficeTreatmentRepository.findById(0L))
                .thenReturn(Optional.of(tDoctorOfficeTreatment));

        TDoctorOfficeTreatment tDoctorOfficeTreatmentDb = tDoctorOfficeTreatmentServiceImpl.getTDoctorOfficeTreatment(0L);

        // then
        assertEquals(tDoctorOfficeTreatmentDb, tDoctorOfficeTreatment);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTDoctorOfficeTreatmentById_CaseDataNotFound() {
        // when
        when(tDoctorOfficeTreatmentRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tDoctorOfficeTreatmentServiceImpl.getTDoctorOfficeTreatment(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTDoctorOfficeTreatment_CaseDataNotFound() {
        // given
        given(tDoctorOfficeTreatmentRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tDoctorOfficeTreatmentRepository.save(tDoctorOfficeTreatment))
                .thenReturn(tDoctorOfficeTreatment);

        TDoctorOfficeTreatment tDoctorOfficeTreatmentDb = tDoctorOfficeTreatmentServiceImpl.createTDoctorOfficeTreatment(tDoctorOfficeTreatment);

        // then
        assertEquals(tDoctorOfficeTreatmentDb.getId(), tDoctorOfficeTreatment.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTDoctorOfficeTreatment_CaseDataFound() {
        // given
        given(tDoctorOfficeTreatmentRepository.findById(0L))
                .willReturn(Optional.of(tDoctorOfficeTreatment));

        try {
            tDoctorOfficeTreatmentServiceImpl.createTDoctorOfficeTreatment(tDoctorOfficeTreatment);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTDoctorOfficeTreatment_CaseDataFound() {
        // given
        given(tDoctorOfficeTreatmentRepository.findById(0L))
                .willReturn(Optional.of(tDoctorOfficeTreatment));

        // When
        tDoctorOfficeTreatment.setDoctorOfficeId(1L);
        when(tDoctorOfficeTreatmentRepository.save(tDoctorOfficeTreatment))
                .thenReturn(tDoctorOfficeTreatment);

        TDoctorOfficeTreatment tDoctorOfficeTreatmentNew = tDoctorOfficeTreatmentServiceImpl.updateTDoctorOfficeTreatment(tDoctorOfficeTreatment);

        // then
        assertEquals(tDoctorOfficeTreatment.getDoctorOfficeId(), tDoctorOfficeTreatmentNew.getDoctorOfficeId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTDoctorOfficeTreatment_CaseDataNotFound() {
        // given
        given(tDoctorOfficeTreatmentRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tDoctorOfficeTreatmentServiceImpl.updateTDoctorOfficeTreatment(tDoctorOfficeTreatment);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTDoctorOfficeTreatment_CaseDataFound() {
        // given
        given(tDoctorOfficeTreatmentRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tDoctorOfficeTreatmentRepository.save(tDoctorOfficeTreatment))
                .thenReturn(tDoctorOfficeTreatment);
        tDoctorOfficeTreatmentServiceImpl.createTDoctorOfficeTreatment(tDoctorOfficeTreatment);

        // When
        tDoctorOfficeTreatmentServiceImpl.deleteTDoctorOfficeTreatment(0L);

        try {
            tDoctorOfficeTreatmentServiceImpl.getTDoctorOfficeTreatment(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTDoctorOfficeTreatment_CaseDataNotFound() {
        // given
        given(tDoctorOfficeTreatmentRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tDoctorOfficeTreatmentServiceImpl.deleteTDoctorOfficeTreatment(0L);

        try {
            tDoctorOfficeTreatmentServiceImpl.getTDoctorOfficeTreatment(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
