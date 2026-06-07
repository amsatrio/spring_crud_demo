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

import com.github.amsatrio.spring_hospital.model.entity.MMedicalFacility;
import com.github.amsatrio.spring_hospital.repository.MMedicalFacilityRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MMedicalFacilityServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MMedicalFacilityServiceTests {

    @Mock
    private MMedicalFacilityRepository mMedicalFacilityRepository;

    @InjectMocks
    private MMedicalFacilityServiceImpl mMedicalFacilityServiceImpl;

    private MMedicalFacility mMedicalFacility = new MMedicalFacility();

    @BeforeEach
    public void setup() {
        mMedicalFacility.setId(0L);
        mMedicalFacility.setFax("init");
        mMedicalFacility.setCreatedBy(0L);
        mMedicalFacility.setCreatedOn(new Date());
        mMedicalFacility.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMMedicalFacilityById_CaseDataFound() {
        // when
        when(mMedicalFacilityRepository.findById(0L))
                .thenReturn(Optional.of(mMedicalFacility));

        MMedicalFacility mMedicalFacilityDb = mMedicalFacilityServiceImpl.getMMedicalFacility(0L);

        // then
        assertEquals(mMedicalFacilityDb, mMedicalFacility);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMMedicalFacilityById_CaseDataNotFound() {
        // when
        when(mMedicalFacilityRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mMedicalFacilityServiceImpl.getMMedicalFacility(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMMedicalFacility_CaseDataNotFound() {
        // given
        given(mMedicalFacilityRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mMedicalFacilityRepository.save(mMedicalFacility))
                .thenReturn(mMedicalFacility);

        MMedicalFacility mMedicalFacilityDb = mMedicalFacilityServiceImpl.createMMedicalFacility(mMedicalFacility);

        // then
        assertEquals(mMedicalFacilityDb.getId(), mMedicalFacility.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMMedicalFacility_CaseDataFound() {
        // given
        given(mMedicalFacilityRepository.findById(0L))
                .willReturn(Optional.of(mMedicalFacility));

        try {
            mMedicalFacilityServiceImpl.createMMedicalFacility(mMedicalFacility);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMMedicalFacility_CaseDataFound() {
        // given
        given(mMedicalFacilityRepository.findById(0L))
                .willReturn(Optional.of(mMedicalFacility));

        // When
        mMedicalFacility.setFax("update");
        when(mMedicalFacilityRepository.save(mMedicalFacility))
                .thenReturn(mMedicalFacility);

        MMedicalFacility mMedicalFacilityNew = mMedicalFacilityServiceImpl.updateMMedicalFacility(mMedicalFacility);

        // then
        assertEquals(mMedicalFacility.getFax(), mMedicalFacilityNew.getFax());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMMedicalFacility_CaseDataNotFound() {
        // given
        given(mMedicalFacilityRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mMedicalFacilityServiceImpl.updateMMedicalFacility(mMedicalFacility);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMMedicalFacility_CaseDataFound() {
        // given
        given(mMedicalFacilityRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mMedicalFacilityRepository.save(mMedicalFacility))
                .thenReturn(mMedicalFacility);
        mMedicalFacilityServiceImpl.createMMedicalFacility(mMedicalFacility);

        // When
        mMedicalFacilityServiceImpl.deleteMMedicalFacility(0L);

        try {
            mMedicalFacilityServiceImpl.getMMedicalFacility(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMMedicalFacility_CaseDataNotFound() {
        // given
        given(mMedicalFacilityRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mMedicalFacilityServiceImpl.deleteMMedicalFacility(0L);

        try {
            mMedicalFacilityServiceImpl.getMMedicalFacility(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
