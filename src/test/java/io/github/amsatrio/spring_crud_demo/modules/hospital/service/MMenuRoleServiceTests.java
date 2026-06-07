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

import com.github.amsatrio.spring_hospital.model.entity.MMenuRole;
import com.github.amsatrio.spring_hospital.repository.MMenuRoleRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MMenuRoleServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MMenuRoleServiceTests {

    @Mock
    private MMenuRoleRepository mMenuRoleRepository;

    @InjectMocks
    private MMenuRoleServiceImpl mMenuRoleServiceImpl;

    private MMenuRole mMenuRole = new MMenuRole();

    @BeforeEach
    public void setup() {
        mMenuRole.setId(0L);
        mMenuRole.setRoleId(0L);
        mMenuRole.setCreatedBy(0L);
        mMenuRole.setCreatedOn(new Date());
        mMenuRole.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMMenuRoleById_CaseDataFound() {
        // when
        when(mMenuRoleRepository.findById(0L))
                .thenReturn(Optional.of(mMenuRole));

        MMenuRole mMenuRoleDb = mMenuRoleServiceImpl.getMMenuRole(0L);

        // then
        assertEquals(mMenuRoleDb, mMenuRole);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMMenuRoleById_CaseDataNotFound() {
        // when
        when(mMenuRoleRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mMenuRoleServiceImpl.getMMenuRole(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMMenuRole_CaseDataNotFound() {
        // given
        given(mMenuRoleRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mMenuRoleRepository.save(mMenuRole))
                .thenReturn(mMenuRole);

        MMenuRole mMenuRoleDb = mMenuRoleServiceImpl.createMMenuRole(mMenuRole);

        // then
        assertEquals(mMenuRoleDb.getId(), mMenuRole.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMMenuRole_CaseDataFound() {
        // given
        given(mMenuRoleRepository.findById(0L))
                .willReturn(Optional.of(mMenuRole));

        try {
            mMenuRoleServiceImpl.createMMenuRole(mMenuRole);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMMenuRole_CaseDataFound() {
        // given
        given(mMenuRoleRepository.findById(0L))
                .willReturn(Optional.of(mMenuRole));

        // When
        mMenuRole.setRoleId(1L);
        when(mMenuRoleRepository.save(mMenuRole))
                .thenReturn(mMenuRole);

        MMenuRole mMenuRoleNew = mMenuRoleServiceImpl.updateMMenuRole(mMenuRole);

        // then
        assertEquals(mMenuRole.getRoleId(), mMenuRoleNew.getRoleId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMMenuRole_CaseDataNotFound() {
        // given
        given(mMenuRoleRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mMenuRoleServiceImpl.updateMMenuRole(mMenuRole);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMMenuRole_CaseDataFound() {
        // given
        given(mMenuRoleRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mMenuRoleRepository.save(mMenuRole))
                .thenReturn(mMenuRole);
        mMenuRoleServiceImpl.createMMenuRole(mMenuRole);

        // When
        mMenuRoleServiceImpl.deleteMMenuRole(0L);

        try {
            mMenuRoleServiceImpl.getMMenuRole(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMMenuRole_CaseDataNotFound() {
        // given
        given(mMenuRoleRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mMenuRoleServiceImpl.deleteMMenuRole(0L);

        try {
            mMenuRoleServiceImpl.getMMenuRole(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
