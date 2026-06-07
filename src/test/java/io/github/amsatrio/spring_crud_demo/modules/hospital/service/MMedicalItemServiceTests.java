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

import com.github.amsatrio.spring_hospital.model.entity.MMedicalItem;
import com.github.amsatrio.spring_hospital.repository.MMedicalItemRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MMedicalItemServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MMedicalItemServiceTests {

    @Mock
    private MMedicalItemRepository mMedicalItemRepository;

    @InjectMocks
    private MMedicalItemServiceImpl mMedicalItemServiceImpl;

    private MMedicalItem mMedicalItem = new MMedicalItem();

    @BeforeEach
    public void setup() {
        mMedicalItem.setId(0L);
        mMedicalItem.setImagePath("init");
        mMedicalItem.setCreatedBy(0L);
        mMedicalItem.setCreatedOn(new Date());
        mMedicalItem.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMMedicalItemById_CaseDataFound() {
        // when
        when(mMedicalItemRepository.findById(0L))
                .thenReturn(Optional.of(mMedicalItem));

        MMedicalItem mMedicalItemDb = mMedicalItemServiceImpl.getMMedicalItem(0L);

        // then
        assertEquals(mMedicalItemDb, mMedicalItem);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMMedicalItemById_CaseDataNotFound() {
        // when
        when(mMedicalItemRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mMedicalItemServiceImpl.getMMedicalItem(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMMedicalItem_CaseDataNotFound() {
        // given
        given(mMedicalItemRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mMedicalItemRepository.save(mMedicalItem))
                .thenReturn(mMedicalItem);

        MMedicalItem mMedicalItemDb = mMedicalItemServiceImpl.createMMedicalItem(mMedicalItem);

        // then
        assertEquals(mMedicalItemDb.getId(), mMedicalItem.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMMedicalItem_CaseDataFound() {
        // given
        given(mMedicalItemRepository.findById(0L))
                .willReturn(Optional.of(mMedicalItem));

        try {
            mMedicalItemServiceImpl.createMMedicalItem(mMedicalItem);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMMedicalItem_CaseDataFound() {
        // given
        given(mMedicalItemRepository.findById(0L))
                .willReturn(Optional.of(mMedicalItem));

        // When
        mMedicalItem.setImagePath("update");
        when(mMedicalItemRepository.save(mMedicalItem))
                .thenReturn(mMedicalItem);

        MMedicalItem mMedicalItemNew = mMedicalItemServiceImpl.updateMMedicalItem(mMedicalItem);

        // then
        assertEquals(mMedicalItem.getImagePath(), mMedicalItemNew.getImagePath());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMMedicalItem_CaseDataNotFound() {
        // given
        given(mMedicalItemRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mMedicalItemServiceImpl.updateMMedicalItem(mMedicalItem);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMMedicalItem_CaseDataFound() {
        // given
        given(mMedicalItemRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mMedicalItemRepository.save(mMedicalItem))
                .thenReturn(mMedicalItem);
        mMedicalItemServiceImpl.createMMedicalItem(mMedicalItem);

        // When
        mMedicalItemServiceImpl.deleteMMedicalItem(0L);

        try {
            mMedicalItemServiceImpl.getMMedicalItem(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMMedicalItem_CaseDataNotFound() {
        // given
        given(mMedicalItemRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mMedicalItemServiceImpl.deleteMMedicalItem(0L);

        try {
            mMedicalItemServiceImpl.getMMedicalItem(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
