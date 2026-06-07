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

import com.github.amsatrio.spring_hospital.model.entity.TDoctorTreatment;
import com.github.amsatrio.spring_hospital.repository.TDoctorTreatmentRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TDoctorTreatmentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TDoctorTreatmentServiceTests {

    @Mock
    private TDoctorTreatmentRepository tDoctorTreatmentRepository;

    @InjectMocks
    private TDoctorTreatmentServiceImpl tDoctorTreatmentServiceImpl;

    private TDoctorTreatment tDoctorTreatment = new TDoctorTreatment();

    @BeforeEach
    public void setup() {
        tDoctorTreatment.setId(0L);
        tDoctorTreatment.setName("init");
        tDoctorTreatment.setCreatedBy(0L);
        tDoctorTreatment.setCreatedOn(new Date());
        tDoctorTreatment.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTDoctorTreatmentById_CaseDataFound() {
        // when
        when(tDoctorTreatmentRepository.findById(0L))
                .thenReturn(Optional.of(tDoctorTreatment));

        TDoctorTreatment tDoctorTreatmentDb = tDoctorTreatmentServiceImpl.getTDoctorTreatment(0L);

        // then
        assertEquals(tDoctorTreatmentDb, tDoctorTreatment);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTDoctorTreatmentById_CaseDataNotFound() {
        // when
        when(tDoctorTreatmentRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tDoctorTreatmentServiceImpl.getTDoctorTreatment(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTDoctorTreatment_CaseDataNotFound() {
        // given
        given(tDoctorTreatmentRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tDoctorTreatmentRepository.save(tDoctorTreatment))
                .thenReturn(tDoctorTreatment);

        TDoctorTreatment tDoctorTreatmentDb = tDoctorTreatmentServiceImpl.createTDoctorTreatment(tDoctorTreatment);

        // then
        assertEquals(tDoctorTreatmentDb.getId(), tDoctorTreatment.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTDoctorTreatment_CaseDataFound() {
        // given
        given(tDoctorTreatmentRepository.findById(0L))
                .willReturn(Optional.of(tDoctorTreatment));

        try {
            tDoctorTreatmentServiceImpl.createTDoctorTreatment(tDoctorTreatment);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTDoctorTreatment_CaseDataFound() {
        // given
        given(tDoctorTreatmentRepository.findById(0L))
                .willReturn(Optional.of(tDoctorTreatment));

        // When
        tDoctorTreatment.setName("update");
        when(tDoctorTreatmentRepository.save(tDoctorTreatment))
                .thenReturn(tDoctorTreatment);

        TDoctorTreatment tDoctorTreatmentNew = tDoctorTreatmentServiceImpl.updateTDoctorTreatment(tDoctorTreatment);

        // then
        assertEquals(tDoctorTreatment.getName(), tDoctorTreatmentNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTDoctorTreatment_CaseDataNotFound() {
        // given
        given(tDoctorTreatmentRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tDoctorTreatmentServiceImpl.updateTDoctorTreatment(tDoctorTreatment);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTDoctorTreatment_CaseDataFound() {
        // given
        given(tDoctorTreatmentRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tDoctorTreatmentRepository.save(tDoctorTreatment))
                .thenReturn(tDoctorTreatment);
        tDoctorTreatmentServiceImpl.createTDoctorTreatment(tDoctorTreatment);

        // When
        tDoctorTreatmentServiceImpl.deleteTDoctorTreatment(0L);

        try {
            tDoctorTreatmentServiceImpl.getTDoctorTreatment(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTDoctorTreatment_CaseDataNotFound() {
        // given
        given(tDoctorTreatmentRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tDoctorTreatmentServiceImpl.deleteTDoctorTreatment(0L);

        try {
            tDoctorTreatmentServiceImpl.getTDoctorTreatment(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
