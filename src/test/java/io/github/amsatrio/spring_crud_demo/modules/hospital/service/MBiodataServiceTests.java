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

import com.github.amsatrio.spring_hospital.model.entity.MBiodata;
import com.github.amsatrio.spring_hospital.repository.MBiodataRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MBiodataServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MBiodataServiceTests {

    @Mock
    private MBiodataRepository mBiodataRepository;

    @InjectMocks
    private MBiodataServiceImpl mBiodataServiceImpl;

    private MBiodata mBiodata = new MBiodata();

    @BeforeEach
    public void setup() {
        mBiodata.setId(0L);
        mBiodata.setImagePath("init");
        mBiodata.setCreatedBy(0L);
        mBiodata.setCreatedOn(new Date());
        mBiodata.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMBiodataById_CaseDataFound() {
        // when
        when(mBiodataRepository.findById(0L))
                .thenReturn(Optional.of(mBiodata));

        MBiodata mBiodataDb = mBiodataServiceImpl.getMBiodata(0L);

        // then
        assertEquals(mBiodataDb, mBiodata);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMBiodataById_CaseDataNotFound() {
        // when
        when(mBiodataRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mBiodataServiceImpl.getMBiodata(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMBiodata_CaseDataNotFound() {
        // given
        given(mBiodataRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mBiodataRepository.save(mBiodata))
                .thenReturn(mBiodata);

        MBiodata mBiodataDb = mBiodataServiceImpl.createMBiodata(mBiodata);

        // then
        assertEquals(mBiodataDb.getId(), mBiodata.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMBiodata_CaseDataFound() {
        // given
        given(mBiodataRepository.findById(0L))
                .willReturn(Optional.of(mBiodata));

        try {
            mBiodataServiceImpl.createMBiodata(mBiodata);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMBiodata_CaseDataFound() {
        // given
        given(mBiodataRepository.findById(0L))
                .willReturn(Optional.of(mBiodata));

        // When
        mBiodata.setImagePath("update");
        when(mBiodataRepository.save(mBiodata))
                .thenReturn(mBiodata);

        MBiodata mBiodataNew = mBiodataServiceImpl.updateMBiodata(mBiodata);

        // then
        assertEquals(mBiodata.getImagePath(), mBiodataNew.getImagePath());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMBiodata_CaseDataNotFound() {
        // given
        given(mBiodataRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mBiodataServiceImpl.updateMBiodata(mBiodata);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMBiodata_CaseDataFound() {
        // given
        given(mBiodataRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mBiodataRepository.save(mBiodata))
                .thenReturn(mBiodata);
        mBiodataServiceImpl.createMBiodata(mBiodata);

        // When
        mBiodataServiceImpl.deleteMBiodata(0L);

        try {
            mBiodataServiceImpl.getMBiodata(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMBiodata_CaseDataNotFound() {
        // given
        given(mBiodataRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mBiodataServiceImpl.deleteMBiodata(0L);

        try {
            mBiodataServiceImpl.getMBiodata(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
