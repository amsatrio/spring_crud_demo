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

import com.github.amsatrio.spring_hospital.model.entity.MMedicalItemSegmentation;
import com.github.amsatrio.spring_hospital.repository.MMedicalItemSegmentationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MMedicalItemSegmentationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MMedicalItemSegmentationServiceTests {

    @Mock
    private MMedicalItemSegmentationRepository mMedicalItemSegmentationRepository;

    @InjectMocks
    private MMedicalItemSegmentationServiceImpl mMedicalItemSegmentationServiceImpl;

    private MMedicalItemSegmentation mMedicalItemSegmentation = new MMedicalItemSegmentation();

    @BeforeEach
    public void setup() {
        mMedicalItemSegmentation.setId(0L);
        mMedicalItemSegmentation.setName("init");
        mMedicalItemSegmentation.setCreatedBy(0L);
        mMedicalItemSegmentation.setCreatedOn(new Date());
        mMedicalItemSegmentation.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMMedicalItemSegmentationById_CaseDataFound() {
        // when
        when(mMedicalItemSegmentationRepository.findById(0L))
                .thenReturn(Optional.of(mMedicalItemSegmentation));

        MMedicalItemSegmentation mMedicalItemSegmentationDb = mMedicalItemSegmentationServiceImpl.getMMedicalItemSegmentation(0L);

        // then
        assertEquals(mMedicalItemSegmentationDb, mMedicalItemSegmentation);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMMedicalItemSegmentationById_CaseDataNotFound() {
        // when
        when(mMedicalItemSegmentationRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mMedicalItemSegmentationServiceImpl.getMMedicalItemSegmentation(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMMedicalItemSegmentation_CaseDataNotFound() {
        // given
        given(mMedicalItemSegmentationRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mMedicalItemSegmentationRepository.save(mMedicalItemSegmentation))
                .thenReturn(mMedicalItemSegmentation);

        MMedicalItemSegmentation mMedicalItemSegmentationDb = mMedicalItemSegmentationServiceImpl.createMMedicalItemSegmentation(mMedicalItemSegmentation);

        // then
        assertEquals(mMedicalItemSegmentationDb.getId(), mMedicalItemSegmentation.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMMedicalItemSegmentation_CaseDataFound() {
        // given
        given(mMedicalItemSegmentationRepository.findById(0L))
                .willReturn(Optional.of(mMedicalItemSegmentation));

        try {
            mMedicalItemSegmentationServiceImpl.createMMedicalItemSegmentation(mMedicalItemSegmentation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMMedicalItemSegmentation_CaseDataFound() {
        // given
        given(mMedicalItemSegmentationRepository.findById(0L))
                .willReturn(Optional.of(mMedicalItemSegmentation));

        // When
        mMedicalItemSegmentation.setName("update");
        when(mMedicalItemSegmentationRepository.save(mMedicalItemSegmentation))
                .thenReturn(mMedicalItemSegmentation);

        MMedicalItemSegmentation mMedicalItemSegmentationNew = mMedicalItemSegmentationServiceImpl.updateMMedicalItemSegmentation(mMedicalItemSegmentation);

        // then
        assertEquals(mMedicalItemSegmentation.getName(), mMedicalItemSegmentationNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMMedicalItemSegmentation_CaseDataNotFound() {
        // given
        given(mMedicalItemSegmentationRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mMedicalItemSegmentationServiceImpl.updateMMedicalItemSegmentation(mMedicalItemSegmentation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMMedicalItemSegmentation_CaseDataFound() {
        // given
        given(mMedicalItemSegmentationRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mMedicalItemSegmentationRepository.save(mMedicalItemSegmentation))
                .thenReturn(mMedicalItemSegmentation);
        mMedicalItemSegmentationServiceImpl.createMMedicalItemSegmentation(mMedicalItemSegmentation);

        // When
        mMedicalItemSegmentationServiceImpl.deleteMMedicalItemSegmentation(0L);

        try {
            mMedicalItemSegmentationServiceImpl.getMMedicalItemSegmentation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMMedicalItemSegmentation_CaseDataNotFound() {
        // given
        given(mMedicalItemSegmentationRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mMedicalItemSegmentationServiceImpl.deleteMMedicalItemSegmentation(0L);

        try {
            mMedicalItemSegmentationServiceImpl.getMMedicalItemSegmentation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
