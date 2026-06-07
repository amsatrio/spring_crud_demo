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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBloodGroup;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MBloodGroupRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MBloodGroupServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MBloodGroupServiceTests {

    @Mock
    private MBloodGroupRepository mBloodGroupRepository;

    @InjectMocks
    private MBloodGroupServiceImpl mBloodGroupServiceImpl;

    private MBloodGroup mBloodGroup = new MBloodGroup();

    @BeforeEach
    public void setup() {
        mBloodGroup.setId(0L);
        mBloodGroup.setDescrtiption("init");
        mBloodGroup.setCreatedBy(0L);
        mBloodGroup.setCreatedOn(new Date());
        mBloodGroup.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMBloodGroupById_CaseDataFound() {
        // when
        when(mBloodGroupRepository.findById(0L))
                .thenReturn(Optional.of(mBloodGroup));

        MBloodGroup mBloodGroupDb = mBloodGroupServiceImpl.getMBloodGroup(0L);

        // then
        assertEquals(mBloodGroupDb, mBloodGroup);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMBloodGroupById_CaseDataNotFound() {
        // when
        when(mBloodGroupRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mBloodGroupServiceImpl.getMBloodGroup(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMBloodGroup_CaseDataNotFound() {
        // given
        given(mBloodGroupRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mBloodGroupRepository.save(mBloodGroup))
                .thenReturn(mBloodGroup);

        MBloodGroup mBloodGroupDb = mBloodGroupServiceImpl.createMBloodGroup(mBloodGroup);

        // then
        assertEquals(mBloodGroupDb.getId(), mBloodGroup.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMBloodGroup_CaseDataFound() {
        // given
        given(mBloodGroupRepository.findById(0L))
                .willReturn(Optional.of(mBloodGroup));

        try {
            mBloodGroupServiceImpl.createMBloodGroup(mBloodGroup);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMBloodGroup_CaseDataFound() {
        // given
        given(mBloodGroupRepository.findById(0L))
                .willReturn(Optional.of(mBloodGroup));

        // When
        mBloodGroup.setDescrtiption("update");
        when(mBloodGroupRepository.save(mBloodGroup))
                .thenReturn(mBloodGroup);

        MBloodGroup mBloodGroupNew = mBloodGroupServiceImpl.updateMBloodGroup(mBloodGroup);

        // then
        assertEquals(mBloodGroup.getDescrtiption(), mBloodGroupNew.getDescrtiption());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMBloodGroup_CaseDataNotFound() {
        // given
        given(mBloodGroupRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mBloodGroupServiceImpl.updateMBloodGroup(mBloodGroup);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMBloodGroup_CaseDataFound() {
        // given
        given(mBloodGroupRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mBloodGroupRepository.save(mBloodGroup))
                .thenReturn(mBloodGroup);
        mBloodGroupServiceImpl.createMBloodGroup(mBloodGroup);

        // When
        mBloodGroupServiceImpl.deleteMBloodGroup(0L);

        try {
            mBloodGroupServiceImpl.getMBloodGroup(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMBloodGroup_CaseDataNotFound() {
        // given
        given(mBloodGroupRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mBloodGroupServiceImpl.deleteMBloodGroup(0L);

        try {
            mBloodGroupServiceImpl.getMBloodGroup(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
