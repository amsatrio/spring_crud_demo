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

import com.github.amsatrio.spring_hospital.model.entity.TDoctorOffice;
import com.github.amsatrio.spring_hospital.repository.TDoctorOfficeRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TDoctorOfficeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TDoctorOfficeServiceTests {

    @Mock
    private TDoctorOfficeRepository tDoctorOfficeRepository;

    @InjectMocks
    private TDoctorOfficeServiceImpl tDoctorOfficeServiceImpl;

    private TDoctorOffice tDoctorOffice = new TDoctorOffice();

    @BeforeEach
    public void setup() {
        tDoctorOffice.setId(0L);
        tDoctorOffice.setSpecialization("init");
        tDoctorOffice.setCreatedBy(0L);
        tDoctorOffice.setCreatedOn(new Date());
        tDoctorOffice.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTDoctorOfficeById_CaseDataFound() {
        // when
        when(tDoctorOfficeRepository.findById(0L))
                .thenReturn(Optional.of(tDoctorOffice));

        TDoctorOffice tDoctorOfficeDb = tDoctorOfficeServiceImpl.getTDoctorOffice(0L);

        // then
        assertEquals(tDoctorOfficeDb, tDoctorOffice);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTDoctorOfficeById_CaseDataNotFound() {
        // when
        when(tDoctorOfficeRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tDoctorOfficeServiceImpl.getTDoctorOffice(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTDoctorOffice_CaseDataNotFound() {
        // given
        given(tDoctorOfficeRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tDoctorOfficeRepository.save(tDoctorOffice))
                .thenReturn(tDoctorOffice);

        TDoctorOffice tDoctorOfficeDb = tDoctorOfficeServiceImpl.createTDoctorOffice(tDoctorOffice);

        // then
        assertEquals(tDoctorOfficeDb.getId(), tDoctorOffice.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTDoctorOffice_CaseDataFound() {
        // given
        given(tDoctorOfficeRepository.findById(0L))
                .willReturn(Optional.of(tDoctorOffice));

        try {
            tDoctorOfficeServiceImpl.createTDoctorOffice(tDoctorOffice);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTDoctorOffice_CaseDataFound() {
        // given
        given(tDoctorOfficeRepository.findById(0L))
                .willReturn(Optional.of(tDoctorOffice));

        // When
        tDoctorOffice.setSpecialization("update");
        when(tDoctorOfficeRepository.save(tDoctorOffice))
                .thenReturn(tDoctorOffice);

        TDoctorOffice tDoctorOfficeNew = tDoctorOfficeServiceImpl.updateTDoctorOffice(tDoctorOffice);

        // then
        assertEquals(tDoctorOffice.getSpecialization(), tDoctorOfficeNew.getSpecialization());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTDoctorOffice_CaseDataNotFound() {
        // given
        given(tDoctorOfficeRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tDoctorOfficeServiceImpl.updateTDoctorOffice(tDoctorOffice);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTDoctorOffice_CaseDataFound() {
        // given
        given(tDoctorOfficeRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tDoctorOfficeRepository.save(tDoctorOffice))
                .thenReturn(tDoctorOffice);
        tDoctorOfficeServiceImpl.createTDoctorOffice(tDoctorOffice);

        // When
        tDoctorOfficeServiceImpl.deleteTDoctorOffice(0L);

        try {
            tDoctorOfficeServiceImpl.getTDoctorOffice(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTDoctorOffice_CaseDataNotFound() {
        // given
        given(tDoctorOfficeRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tDoctorOfficeServiceImpl.deleteTDoctorOffice(0L);

        try {
            tDoctorOfficeServiceImpl.getTDoctorOffice(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
