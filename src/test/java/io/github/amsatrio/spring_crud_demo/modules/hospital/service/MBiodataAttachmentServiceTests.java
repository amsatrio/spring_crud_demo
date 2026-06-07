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

import com.github.amsatrio.spring_hospital.model.entity.MBiodataAttachment;
import com.github.amsatrio.spring_hospital.repository.MBiodataAttachmentRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MBiodataAttachmentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MBiodataAttachmentServiceTests {

    @Mock
    private MBiodataAttachmentRepository mBiodataAttachmentRepository;

    @InjectMocks
    private MBiodataAttachmentServiceImpl mBiodataAttachmentServiceImpl;

    private MBiodataAttachment mBiodataAttachment = new MBiodataAttachment();

    @BeforeEach
    public void setup() {
        mBiodataAttachment.setId(0L);
        mBiodataAttachment.setFile("init".getBytes());
        mBiodataAttachment.setCreatedBy(0L);
        mBiodataAttachment.setCreatedOn(new Date());
        mBiodataAttachment.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMBiodataAttachmentById_CaseDataFound() {
        // when
        when(mBiodataAttachmentRepository.findById(0L))
                .thenReturn(Optional.of(mBiodataAttachment));

        MBiodataAttachment mBiodataAttachmentDb = mBiodataAttachmentServiceImpl.getMBiodataAttachment(0L);

        // then
        assertEquals(mBiodataAttachmentDb, mBiodataAttachment);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMBiodataAttachmentById_CaseDataNotFound() {
        // when
        when(mBiodataAttachmentRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mBiodataAttachmentServiceImpl.getMBiodataAttachment(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMBiodataAttachment_CaseDataNotFound() {
        // given
        given(mBiodataAttachmentRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mBiodataAttachmentRepository.save(mBiodataAttachment))
                .thenReturn(mBiodataAttachment);

        MBiodataAttachment mBiodataAttachmentDb = mBiodataAttachmentServiceImpl.createMBiodataAttachment(mBiodataAttachment);

        // then
        assertEquals(mBiodataAttachmentDb.getId(), mBiodataAttachment.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMBiodataAttachment_CaseDataFound() {
        // given
        given(mBiodataAttachmentRepository.findById(0L))
                .willReturn(Optional.of(mBiodataAttachment));

        try {
            mBiodataAttachmentServiceImpl.createMBiodataAttachment(mBiodataAttachment);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMBiodataAttachment_CaseDataFound() {
        // given
        given(mBiodataAttachmentRepository.findById(0L))
                .willReturn(Optional.of(mBiodataAttachment));

        // When
        mBiodataAttachment.setFile("update".getBytes());
        when(mBiodataAttachmentRepository.save(mBiodataAttachment))
                .thenReturn(mBiodataAttachment);

        MBiodataAttachment mBiodataAttachmentNew = mBiodataAttachmentServiceImpl.updateMBiodataAttachment(mBiodataAttachment);

        // then
        assertEquals(mBiodataAttachment.getFile(), mBiodataAttachmentNew.getFile());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMBiodataAttachment_CaseDataNotFound() {
        // given
        given(mBiodataAttachmentRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mBiodataAttachmentServiceImpl.updateMBiodataAttachment(mBiodataAttachment);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMBiodataAttachment_CaseDataFound() {
        // given
        given(mBiodataAttachmentRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mBiodataAttachmentRepository.save(mBiodataAttachment))
                .thenReturn(mBiodataAttachment);
        mBiodataAttachmentServiceImpl.createMBiodataAttachment(mBiodataAttachment);

        // When
        mBiodataAttachmentServiceImpl.deleteMBiodataAttachment(0L);

        try {
            mBiodataAttachmentServiceImpl.getMBiodataAttachment(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMBiodataAttachment_CaseDataNotFound() {
        // given
        given(mBiodataAttachmentRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mBiodataAttachmentServiceImpl.deleteMBiodataAttachment(0L);

        try {
            mBiodataAttachmentServiceImpl.getMBiodataAttachment(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
