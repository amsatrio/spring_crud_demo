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

import com.github.amsatrio.spring_hospital.model.entity.MMedicalItemCategory;
import com.github.amsatrio.spring_hospital.repository.MMedicalItemCategoryRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MMedicalItemCategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MMedicalItemCategoryServiceTests {

    @Mock
    private MMedicalItemCategoryRepository mMedicalItemCategoryRepository;

    @InjectMocks
    private MMedicalItemCategoryServiceImpl mMedicalItemCategoryServiceImpl;

    private MMedicalItemCategory mMedicalItemCategory = new MMedicalItemCategory();

    @BeforeEach
    public void setup() {
        mMedicalItemCategory.setId(0L);
        mMedicalItemCategory.setName("init");
        mMedicalItemCategory.setCreatedBy(0L);
        mMedicalItemCategory.setCreatedOn(new Date());
        mMedicalItemCategory.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMMedicalItemCategoryById_CaseDataFound() {
        // when
        when(mMedicalItemCategoryRepository.findById(0L))
                .thenReturn(Optional.of(mMedicalItemCategory));

        MMedicalItemCategory mMedicalItemCategoryDb = mMedicalItemCategoryServiceImpl.getMMedicalItemCategory(0L);

        // then
        assertEquals(mMedicalItemCategoryDb, mMedicalItemCategory);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMMedicalItemCategoryById_CaseDataNotFound() {
        // when
        when(mMedicalItemCategoryRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mMedicalItemCategoryServiceImpl.getMMedicalItemCategory(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMMedicalItemCategory_CaseDataNotFound() {
        // given
        given(mMedicalItemCategoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mMedicalItemCategoryRepository.save(mMedicalItemCategory))
                .thenReturn(mMedicalItemCategory);

        MMedicalItemCategory mMedicalItemCategoryDb = mMedicalItemCategoryServiceImpl.createMMedicalItemCategory(mMedicalItemCategory);

        // then
        assertEquals(mMedicalItemCategoryDb.getId(), mMedicalItemCategory.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMMedicalItemCategory_CaseDataFound() {
        // given
        given(mMedicalItemCategoryRepository.findById(0L))
                .willReturn(Optional.of(mMedicalItemCategory));

        try {
            mMedicalItemCategoryServiceImpl.createMMedicalItemCategory(mMedicalItemCategory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMMedicalItemCategory_CaseDataFound() {
        // given
        given(mMedicalItemCategoryRepository.findById(0L))
                .willReturn(Optional.of(mMedicalItemCategory));

        // When
        mMedicalItemCategory.setName("update");
        when(mMedicalItemCategoryRepository.save(mMedicalItemCategory))
                .thenReturn(mMedicalItemCategory);

        MMedicalItemCategory mMedicalItemCategoryNew = mMedicalItemCategoryServiceImpl.updateMMedicalItemCategory(mMedicalItemCategory);

        // then
        assertEquals(mMedicalItemCategory.getName(), mMedicalItemCategoryNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMMedicalItemCategory_CaseDataNotFound() {
        // given
        given(mMedicalItemCategoryRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mMedicalItemCategoryServiceImpl.updateMMedicalItemCategory(mMedicalItemCategory);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMMedicalItemCategory_CaseDataFound() {
        // given
        given(mMedicalItemCategoryRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mMedicalItemCategoryRepository.save(mMedicalItemCategory))
                .thenReturn(mMedicalItemCategory);
        mMedicalItemCategoryServiceImpl.createMMedicalItemCategory(mMedicalItemCategory);

        // When
        mMedicalItemCategoryServiceImpl.deleteMMedicalItemCategory(0L);

        try {
            mMedicalItemCategoryServiceImpl.getMMedicalItemCategory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMMedicalItemCategory_CaseDataNotFound() {
        // given
        given(mMedicalItemCategoryRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mMedicalItemCategoryServiceImpl.deleteMMedicalItemCategory(0L);

        try {
            mMedicalItemCategoryServiceImpl.getMMedicalItemCategory(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
