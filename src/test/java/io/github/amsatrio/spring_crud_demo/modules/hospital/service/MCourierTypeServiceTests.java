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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCourierType;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MCourierTypeRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MCourierTypeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MCourierTypeServiceTests {

    @Mock
    private MCourierTypeRepository mCourierTypeRepository;

    @InjectMocks
    private MCourierTypeServiceImpl mCourierTypeServiceImpl;

    private MCourierType mCourierType = new MCourierType();

    @BeforeEach
    public void setup() {
        mCourierType.setId(0L);
        mCourierType.setName("init");
        mCourierType.setCreatedBy(0L);
        mCourierType.setCreatedOn(new Date());
        mCourierType.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMCourierTypeById_CaseDataFound() {
        // when
        when(mCourierTypeRepository.findById(0L))
                .thenReturn(Optional.of(mCourierType));

        MCourierType mCourierTypeDb = mCourierTypeServiceImpl.getMCourierType(0L);

        // then
        assertEquals(mCourierTypeDb, mCourierType);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMCourierTypeById_CaseDataNotFound() {
        // when
        when(mCourierTypeRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mCourierTypeServiceImpl.getMCourierType(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMCourierType_CaseDataNotFound() {
        // given
        given(mCourierTypeRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mCourierTypeRepository.save(mCourierType))
                .thenReturn(mCourierType);

        MCourierType mCourierTypeDb = mCourierTypeServiceImpl.createMCourierType(mCourierType);

        // then
        assertEquals(mCourierTypeDb.getId(), mCourierType.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMCourierType_CaseDataFound() {
        // given
        given(mCourierTypeRepository.findById(0L))
                .willReturn(Optional.of(mCourierType));

        try {
            mCourierTypeServiceImpl.createMCourierType(mCourierType);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMCourierType_CaseDataFound() {
        // given
        given(mCourierTypeRepository.findById(0L))
                .willReturn(Optional.of(mCourierType));

        // When
        mCourierType.setName("update");
        when(mCourierTypeRepository.save(mCourierType))
                .thenReturn(mCourierType);

        MCourierType mCourierTypeNew = mCourierTypeServiceImpl.updateMCourierType(mCourierType);

        // then
        assertEquals(mCourierType.getName(), mCourierTypeNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMCourierType_CaseDataNotFound() {
        // given
        given(mCourierTypeRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mCourierTypeServiceImpl.updateMCourierType(mCourierType);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMCourierType_CaseDataFound() {
        // given
        given(mCourierTypeRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mCourierTypeRepository.save(mCourierType))
                .thenReturn(mCourierType);
        mCourierTypeServiceImpl.createMCourierType(mCourierType);

        // When
        mCourierTypeServiceImpl.deleteMCourierType(0L);

        try {
            mCourierTypeServiceImpl.getMCourierType(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMCourierType_CaseDataNotFound() {
        // given
        given(mCourierTypeRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mCourierTypeServiceImpl.deleteMCourierType(0L);

        try {
            mCourierTypeServiceImpl.getMCourierType(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
