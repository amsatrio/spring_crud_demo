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

import com.github.amsatrio.spring_hospital.model.entity.MBiodataAddress;
import com.github.amsatrio.spring_hospital.repository.MBiodataAddressRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MBiodataAddressServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MBiodataAddressServiceTests {

    @Mock
    private MBiodataAddressRepository mBiodataAddressRepository;

    @InjectMocks
    private MBiodataAddressServiceImpl mBiodataAddressServiceImpl;

    private MBiodataAddress mBiodataAddress = new MBiodataAddress();

    @BeforeEach
    public void setup() {
        mBiodataAddress.setId(0L);
        mBiodataAddress.setAddress("init");
        mBiodataAddress.setCreatedBy(0L);
        mBiodataAddress.setCreatedOn(new Date());
        mBiodataAddress.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMBiodataAddressById_CaseDataFound() {
        // when
        when(mBiodataAddressRepository.findById(0L))
                .thenReturn(Optional.of(mBiodataAddress));

        MBiodataAddress mBiodataAddressDb = mBiodataAddressServiceImpl.getMBiodataAddress(0L);

        // then
        assertEquals(mBiodataAddressDb, mBiodataAddress);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMBiodataAddressById_CaseDataNotFound() {
        // when
        when(mBiodataAddressRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mBiodataAddressServiceImpl.getMBiodataAddress(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMBiodataAddress_CaseDataNotFound() {
        // given
        given(mBiodataAddressRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mBiodataAddressRepository.save(mBiodataAddress))
                .thenReturn(mBiodataAddress);

        MBiodataAddress mBiodataAddressDb = mBiodataAddressServiceImpl.createMBiodataAddress(mBiodataAddress);

        // then
        assertEquals(mBiodataAddressDb.getId(), mBiodataAddress.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMBiodataAddress_CaseDataFound() {
        // given
        given(mBiodataAddressRepository.findById(0L))
                .willReturn(Optional.of(mBiodataAddress));

        try {
            mBiodataAddressServiceImpl.createMBiodataAddress(mBiodataAddress);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMBiodataAddress_CaseDataFound() {
        // given
        given(mBiodataAddressRepository.findById(0L))
                .willReturn(Optional.of(mBiodataAddress));

        // When
        mBiodataAddress.setAddress("update");
        when(mBiodataAddressRepository.save(mBiodataAddress))
                .thenReturn(mBiodataAddress);

        MBiodataAddress mBiodataAddressNew = mBiodataAddressServiceImpl.updateMBiodataAddress(mBiodataAddress);

        // then
        assertEquals(mBiodataAddress.getAddress(), mBiodataAddressNew.getAddress());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMBiodataAddress_CaseDataNotFound() {
        // given
        given(mBiodataAddressRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mBiodataAddressServiceImpl.updateMBiodataAddress(mBiodataAddress);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMBiodataAddress_CaseDataFound() {
        // given
        given(mBiodataAddressRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mBiodataAddressRepository.save(mBiodataAddress))
                .thenReturn(mBiodataAddress);
        mBiodataAddressServiceImpl.createMBiodataAddress(mBiodataAddress);

        // When
        mBiodataAddressServiceImpl.deleteMBiodataAddress(0L);

        try {
            mBiodataAddressServiceImpl.getMBiodataAddress(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMBiodataAddress_CaseDataNotFound() {
        // given
        given(mBiodataAddressRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mBiodataAddressServiceImpl.deleteMBiodataAddress(0L);

        try {
            mBiodataAddressServiceImpl.getMBiodataAddress(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
