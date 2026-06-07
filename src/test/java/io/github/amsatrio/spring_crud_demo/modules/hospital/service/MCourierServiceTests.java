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

import com.github.amsatrio.spring_hospital.model.entity.MCourier;
import com.github.amsatrio.spring_hospital.repository.MCourierRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MCourierServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MCourierServiceTests {

    @Mock
    private MCourierRepository mCourierRepository;

    @InjectMocks
    private MCourierServiceImpl mCourierServiceImpl;

    private MCourier mCourier = new MCourier();

    @BeforeEach
    public void setup() {
        mCourier.setId(0L);
        mCourier.setName("init");
        mCourier.setCreatedBy(0L);
        mCourier.setCreatedOn(new Date());
        mCourier.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMCourierById_CaseDataFound() {
        // when
        when(mCourierRepository.findById(0L))
                .thenReturn(Optional.of(mCourier));

        MCourier mCourierDb = mCourierServiceImpl.getMCourier(0L);

        // then
        assertEquals(mCourierDb, mCourier);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMCourierById_CaseDataNotFound() {
        // when
        when(mCourierRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mCourierServiceImpl.getMCourier(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMCourier_CaseDataNotFound() {
        // given
        given(mCourierRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mCourierRepository.save(mCourier))
                .thenReturn(mCourier);

        MCourier mCourierDb = mCourierServiceImpl.createMCourier(mCourier);

        // then
        assertEquals(mCourierDb.getId(), mCourier.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMCourier_CaseDataFound() {
        // given
        given(mCourierRepository.findById(0L))
                .willReturn(Optional.of(mCourier));

        try {
            mCourierServiceImpl.createMCourier(mCourier);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMCourier_CaseDataFound() {
        // given
        given(mCourierRepository.findById(0L))
                .willReturn(Optional.of(mCourier));

        // When
        mCourier.setName("update");
        when(mCourierRepository.save(mCourier))
                .thenReturn(mCourier);

        MCourier mCourierNew = mCourierServiceImpl.updateMCourier(mCourier);

        // then
        assertEquals(mCourier.getName(), mCourierNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMCourier_CaseDataNotFound() {
        // given
        given(mCourierRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mCourierServiceImpl.updateMCourier(mCourier);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMCourier_CaseDataFound() {
        // given
        given(mCourierRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mCourierRepository.save(mCourier))
                .thenReturn(mCourier);
        mCourierServiceImpl.createMCourier(mCourier);

        // When
        mCourierServiceImpl.deleteMCourier(0L);

        try {
            mCourierServiceImpl.getMCourier(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMCourier_CaseDataNotFound() {
        // given
        given(mCourierRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mCourierServiceImpl.deleteMCourier(0L);

        try {
            mCourierServiceImpl.getMCourier(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
