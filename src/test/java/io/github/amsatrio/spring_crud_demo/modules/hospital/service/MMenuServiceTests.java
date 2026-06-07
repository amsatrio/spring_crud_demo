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

import com.github.amsatrio.spring_hospital.model.entity.MMenu;
import com.github.amsatrio.spring_hospital.repository.MMenuRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MMenuServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MMenuServiceTests {

    @Mock
    private MMenuRepository mMenuRepository;

    @InjectMocks
    private MMenuServiceImpl mMenuServiceImpl;

    private MMenu mMenu = new MMenu();

    @BeforeEach
    public void setup() {
        mMenu.setId(0L);
        mMenu.setSmallIcon("init");
        mMenu.setCreatedBy(0L);
        mMenu.setCreatedOn(new Date());
        mMenu.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMMenuById_CaseDataFound() {
        // when
        when(mMenuRepository.findById(0L))
                .thenReturn(Optional.of(mMenu));

        MMenu mMenuDb = mMenuServiceImpl.getMMenu(0L);

        // then
        assertEquals(mMenuDb, mMenu);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMMenuById_CaseDataNotFound() {
        // when
        when(mMenuRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mMenuServiceImpl.getMMenu(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMMenu_CaseDataNotFound() {
        // given
        given(mMenuRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mMenuRepository.save(mMenu))
                .thenReturn(mMenu);

        MMenu mMenuDb = mMenuServiceImpl.createMMenu(mMenu);

        // then
        assertEquals(mMenuDb.getId(), mMenu.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMMenu_CaseDataFound() {
        // given
        given(mMenuRepository.findById(0L))
                .willReturn(Optional.of(mMenu));

        try {
            mMenuServiceImpl.createMMenu(mMenu);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMMenu_CaseDataFound() {
        // given
        given(mMenuRepository.findById(0L))
                .willReturn(Optional.of(mMenu));

        // When
        mMenu.setSmallIcon("update");
        when(mMenuRepository.save(mMenu))
                .thenReturn(mMenu);

        MMenu mMenuNew = mMenuServiceImpl.updateMMenu(mMenu);

        // then
        assertEquals(mMenu.getSmallIcon(), mMenuNew.getSmallIcon());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMMenu_CaseDataNotFound() {
        // given
        given(mMenuRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mMenuServiceImpl.updateMMenu(mMenu);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMMenu_CaseDataFound() {
        // given
        given(mMenuRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mMenuRepository.save(mMenu))
                .thenReturn(mMenu);
        mMenuServiceImpl.createMMenu(mMenu);

        // When
        mMenuServiceImpl.deleteMMenu(0L);

        try {
            mMenuServiceImpl.getMMenu(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMMenu_CaseDataNotFound() {
        // given
        given(mMenuRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mMenuServiceImpl.deleteMMenu(0L);

        try {
            mMenuServiceImpl.getMMenu(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
