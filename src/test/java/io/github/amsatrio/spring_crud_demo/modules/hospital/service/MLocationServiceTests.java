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

import com.github.amsatrio.spring_hospital.model.entity.MLocation;
import com.github.amsatrio.spring_hospital.repository.MLocationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MLocationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MLocationServiceTests {

    @Mock
    private MLocationRepository mLocationRepository;

    @InjectMocks
    private MLocationServiceImpl mLocationServiceImpl;

    private MLocation mLocation = new MLocation();

    @BeforeEach
    public void setup() {
        mLocation.setId(0L);
        mLocation.setLocationLevelId(0L);
        mLocation.setCreatedBy(0L);
        mLocation.setCreatedOn(new Date());
        mLocation.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMLocationById_CaseDataFound() {
        // when
        when(mLocationRepository.findById(0L))
                .thenReturn(Optional.of(mLocation));

        MLocation mLocationDb = mLocationServiceImpl.getMLocation(0L);

        // then
        assertEquals(mLocationDb, mLocation);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMLocationById_CaseDataNotFound() {
        // when
        when(mLocationRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mLocationServiceImpl.getMLocation(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMLocation_CaseDataNotFound() {
        // given
        given(mLocationRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mLocationRepository.save(mLocation))
                .thenReturn(mLocation);

        MLocation mLocationDb = mLocationServiceImpl.createMLocation(mLocation);

        // then
        assertEquals(mLocationDb.getId(), mLocation.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMLocation_CaseDataFound() {
        // given
        given(mLocationRepository.findById(0L))
                .willReturn(Optional.of(mLocation));

        try {
            mLocationServiceImpl.createMLocation(mLocation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMLocation_CaseDataFound() {
        // given
        given(mLocationRepository.findById(0L))
                .willReturn(Optional.of(mLocation));

        // When
        mLocation.setLocationLevelId(1L);
        when(mLocationRepository.save(mLocation))
                .thenReturn(mLocation);

        MLocation mLocationNew = mLocationServiceImpl.updateMLocation(mLocation);

        // then
        assertEquals(mLocation.getLocationLevelId(), mLocationNew.getLocationLevelId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMLocation_CaseDataNotFound() {
        // given
        given(mLocationRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mLocationServiceImpl.updateMLocation(mLocation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMLocation_CaseDataFound() {
        // given
        given(mLocationRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mLocationRepository.save(mLocation))
                .thenReturn(mLocation);
        mLocationServiceImpl.createMLocation(mLocation);

        // When
        mLocationServiceImpl.deleteMLocation(0L);

        try {
            mLocationServiceImpl.getMLocation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMLocation_CaseDataNotFound() {
        // given
        given(mLocationRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mLocationServiceImpl.deleteMLocation(0L);

        try {
            mLocationServiceImpl.getMLocation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
