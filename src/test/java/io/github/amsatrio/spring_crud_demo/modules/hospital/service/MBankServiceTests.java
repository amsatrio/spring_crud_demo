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

import com.github.amsatrio.spring_hospital.model.entity.MBank;
import com.github.amsatrio.spring_hospital.repository.MBankRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MBankServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MBankServiceTests {

    @Mock
    private MBankRepository mBankRepository;

    @InjectMocks
    private MBankServiceImpl mBankServiceImpl;

    private MBank mBank = new MBank();

    @BeforeEach
    public void setup() {
        mBank.setId(0L);
        mBank.setVaCode("init");
        mBank.setCreatedBy(0L);
        mBank.setCreatedOn(new Date());
        mBank.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMBankById_CaseDataFound() {
        // when
        when(mBankRepository.findById(0L))
                .thenReturn(Optional.of(mBank));

        MBank mBankDb = mBankServiceImpl.getMBank(0L);

        // then
        assertEquals(mBankDb, mBank);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMBankById_CaseDataNotFound() {
        // when
        when(mBankRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mBankServiceImpl.getMBank(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMBank_CaseDataNotFound() {
        // given
        given(mBankRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mBankRepository.save(mBank))
                .thenReturn(mBank);

        MBank mBankDb = mBankServiceImpl.createMBank(mBank);

        // then
        assertEquals(mBankDb.getId(), mBank.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMBank_CaseDataFound() {
        // given
        given(mBankRepository.findById(0L))
                .willReturn(Optional.of(mBank));

        try {
            mBankServiceImpl.createMBank(mBank);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMBank_CaseDataFound() {
        // given
        given(mBankRepository.findById(0L))
                .willReturn(Optional.of(mBank));

        // When
        mBank.setVaCode("update");
        when(mBankRepository.save(mBank))
                .thenReturn(mBank);

        MBank mBankNew = mBankServiceImpl.updateMBank(mBank);

        // then
        assertEquals(mBank.getVaCode(), mBankNew.getVaCode());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMBank_CaseDataNotFound() {
        // given
        given(mBankRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mBankServiceImpl.updateMBank(mBank);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMBank_CaseDataFound() {
        // given
        given(mBankRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mBankRepository.save(mBank))
                .thenReturn(mBank);
        mBankServiceImpl.createMBank(mBank);

        // When
        mBankServiceImpl.deleteMBank(0L);

        try {
            mBankServiceImpl.getMBank(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMBank_CaseDataNotFound() {
        // given
        given(mBankRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mBankServiceImpl.deleteMBank(0L);

        try {
            mBankServiceImpl.getMBank(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
