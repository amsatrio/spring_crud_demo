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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MMedicalFacilityCategory;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MMedicalFacilityCategoryRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MMedicalFacilityCategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MMedicalFacilityCategoryServiceTests {

    @Mock
    private MMedicalFacilityCategoryRepository mMedicalFacilityCategoryRepository;

    @InjectMocks
    private MMedicalFacilityCategoryServiceImpl mMedicalFacilityCategoryServiceImpl;

    private MMedicalFacilityCategory mMedicalFacilityCategory = new MMedicalFacilityCategory();

    @BeforeEach
    public void setup() {
        mMedicalFacilityCategory.setId(0L);
        mMedicalFacilityCategory.setName("init");
        mMedicalFacilityCategory.setCreatedBy(0L);
        mMedicalFacilityCategory.setCreatedOn(new Date());
        mMedicalFacilityCategory.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMMedicalFacilityCategoryById_CaseDataFound() {
        // when
        when(mMedicalFacilityCategoryRepository.findById(0L))
                .thenReturn(Optional.of(mMedicalFacilityCategory));

        MMedicalFacilityCategory mMedicalFacilityCategoryDb = mMedicalFacilityCategoryServiceImpl.getMMedicalFacilityCategory(0L);

        // then
        assertEquals(mMedicalFacilityCategoryDb, mMedicalFacilityCategory);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMMedicalFacilityCategoryById_CaseDataNotFound() {
        // when
        when(mMedicalFacilityCategoryRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mMedicalFacilityCategoryServiceImpl.getMMedicalFacilityCategory(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMMedicalFacilityCategory_CaseDataNotFound() {
        // given
        given(mMedicalFacilityCategoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mMedicalFacilityCategoryRepository.save(mMedicalFacilityCategory))
                .thenReturn(mMedicalFacilityCategory);

        MMedicalFacilityCategory mMedicalFacilityCategoryDb = mMedicalFacilityCategoryServiceImpl.createMMedicalFacilityCategory(mMedicalFacilityCategory);

        // then
        assertEquals(mMedicalFacilityCategoryDb.getId(), mMedicalFacilityCategory.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMMedicalFacilityCategory_CaseDataFound() {
        // given
        given(mMedicalFacilityCategoryRepository.findById(0L))
                .willReturn(Optional.of(mMedicalFacilityCategory));

        try {
            mMedicalFacilityCategoryServiceImpl.createMMedicalFacilityCategory(mMedicalFacilityCategory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMMedicalFacilityCategory_CaseDataFound() {
        // given
        given(mMedicalFacilityCategoryRepository.findById(0L))
                .willReturn(Optional.of(mMedicalFacilityCategory));

        // When
        mMedicalFacilityCategory.setName("update");
        when(mMedicalFacilityCategoryRepository.save(mMedicalFacilityCategory))
                .thenReturn(mMedicalFacilityCategory);

        MMedicalFacilityCategory mMedicalFacilityCategoryNew = mMedicalFacilityCategoryServiceImpl.updateMMedicalFacilityCategory(mMedicalFacilityCategory);

        // then
        assertEquals(mMedicalFacilityCategory.getName(), mMedicalFacilityCategoryNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMMedicalFacilityCategory_CaseDataNotFound() {
        // given
        given(mMedicalFacilityCategoryRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mMedicalFacilityCategoryServiceImpl.updateMMedicalFacilityCategory(mMedicalFacilityCategory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMMedicalFacilityCategory_CaseDataFound() {
        // given
        given(mMedicalFacilityCategoryRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mMedicalFacilityCategoryRepository.save(mMedicalFacilityCategory))
                .thenReturn(mMedicalFacilityCategory);
        mMedicalFacilityCategoryServiceImpl.createMMedicalFacilityCategory(mMedicalFacilityCategory);

        // When
        mMedicalFacilityCategoryServiceImpl.deleteMMedicalFacilityCategory(0L);

        try {
            mMedicalFacilityCategoryServiceImpl.getMMedicalFacilityCategory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMMedicalFacilityCategory_CaseDataNotFound() {
        // given
        given(mMedicalFacilityCategoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mMedicalFacilityCategoryServiceImpl.deleteMMedicalFacilityCategory(0L);

        try {
            mMedicalFacilityCategoryServiceImpl.getMMedicalFacilityCategory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
