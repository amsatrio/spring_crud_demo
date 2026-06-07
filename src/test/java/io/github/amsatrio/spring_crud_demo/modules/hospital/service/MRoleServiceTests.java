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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MRole;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MRoleRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MRoleServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MRoleServiceTests {

    @Mock
    private MRoleRepository mRoleRepository;

    @InjectMocks
    private MRoleServiceImpl mRoleServiceImpl;

    private MRole mRole = new MRole();

    @BeforeEach
    public void setup() {
        mRole.setId(0L);
        mRole.setCode("init");
        mRole.setCreatedBy(0L);
        mRole.setCreatedOn(new Date());
        mRole.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMRoleById_CaseDataFound() {
        // when
        when(mRoleRepository.findById(0L))
                .thenReturn(Optional.of(mRole));

        MRole mRoleDb = mRoleServiceImpl.getMRole(0L);

        // then
        assertEquals(mRoleDb, mRole);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMRoleById_CaseDataNotFound() {
        // when
        when(mRoleRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mRoleServiceImpl.getMRole(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMRole_CaseDataNotFound() {
        // given
        given(mRoleRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mRoleRepository.save(mRole))
                .thenReturn(mRole);

        MRole mRoleDb = mRoleServiceImpl.createMRole(mRole);

        // then
        assertEquals(mRoleDb.getId(), mRole.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMRole_CaseDataFound() {
        // given
        given(mRoleRepository.findById(0L))
                .willReturn(Optional.of(mRole));

        try {
            mRoleServiceImpl.createMRole(mRole);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMRole_CaseDataFound() {
        // given
        given(mRoleRepository.findById(0L))
                .willReturn(Optional.of(mRole));

        // When
        mRole.setCode("update");
        when(mRoleRepository.save(mRole))
                .thenReturn(mRole);

        MRole mRoleNew = mRoleServiceImpl.updateMRole(mRole);

        // then
        assertEquals(mRole.getCode(), mRoleNew.getCode());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMRole_CaseDataNotFound() {
        // given
        given(mRoleRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mRoleServiceImpl.updateMRole(mRole);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMRole_CaseDataFound() {
        // given
        given(mRoleRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mRoleRepository.save(mRole))
                .thenReturn(mRole);
        mRoleServiceImpl.createMRole(mRole);

        // When
        mRoleServiceImpl.deleteMRole(0L);

        try {
            mRoleServiceImpl.getMRole(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMRole_CaseDataNotFound() {
        // given
        given(mRoleRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mRoleServiceImpl.deleteMRole(0L);

        try {
            mRoleServiceImpl.getMRole(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
