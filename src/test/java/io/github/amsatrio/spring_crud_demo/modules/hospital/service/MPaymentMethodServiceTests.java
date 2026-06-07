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

import com.github.amsatrio.spring_hospital.model.entity.MPaymentMethod;
import com.github.amsatrio.spring_hospital.repository.MPaymentMethodRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MPaymentMethodServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MPaymentMethodServiceTests {

    @Mock
    private MPaymentMethodRepository mPaymentMethodRepository;

    @InjectMocks
    private MPaymentMethodServiceImpl mPaymentMethodServiceImpl;

    private MPaymentMethod mPaymentMethod = new MPaymentMethod();

    @BeforeEach
    public void setup() {
        mPaymentMethod.setId(0L);
        mPaymentMethod.setName("init");
        mPaymentMethod.setCreatedBy(0L);
        mPaymentMethod.setCreatedOn(new Date());
        mPaymentMethod.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMPaymentMethodById_CaseDataFound() {
        // when
        when(mPaymentMethodRepository.findById(0L))
                .thenReturn(Optional.of(mPaymentMethod));

        MPaymentMethod mPaymentMethodDb = mPaymentMethodServiceImpl.getMPaymentMethod(0L);

        // then
        assertEquals(mPaymentMethodDb, mPaymentMethod);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMPaymentMethodById_CaseDataNotFound() {
        // when
        when(mPaymentMethodRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mPaymentMethodServiceImpl.getMPaymentMethod(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMPaymentMethod_CaseDataNotFound() {
        // given
        given(mPaymentMethodRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mPaymentMethodRepository.save(mPaymentMethod))
                .thenReturn(mPaymentMethod);

        MPaymentMethod mPaymentMethodDb = mPaymentMethodServiceImpl.createMPaymentMethod(mPaymentMethod);

        // then
        assertEquals(mPaymentMethodDb.getId(), mPaymentMethod.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMPaymentMethod_CaseDataFound() {
        // given
        given(mPaymentMethodRepository.findById(0L))
                .willReturn(Optional.of(mPaymentMethod));

        try {
            mPaymentMethodServiceImpl.createMPaymentMethod(mPaymentMethod);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMPaymentMethod_CaseDataFound() {
        // given
        given(mPaymentMethodRepository.findById(0L))
                .willReturn(Optional.of(mPaymentMethod));

        // When
        mPaymentMethod.setName("update");
        when(mPaymentMethodRepository.save(mPaymentMethod))
                .thenReturn(mPaymentMethod);

        MPaymentMethod mPaymentMethodNew = mPaymentMethodServiceImpl.updateMPaymentMethod(mPaymentMethod);

        // then
        assertEquals(mPaymentMethod.getName(), mPaymentMethodNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMPaymentMethod_CaseDataNotFound() {
        // given
        given(mPaymentMethodRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mPaymentMethodServiceImpl.updateMPaymentMethod(mPaymentMethod);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMPaymentMethod_CaseDataFound() {
        // given
        given(mPaymentMethodRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mPaymentMethodRepository.save(mPaymentMethod))
                .thenReturn(mPaymentMethod);
        mPaymentMethodServiceImpl.createMPaymentMethod(mPaymentMethod);

        // When
        mPaymentMethodServiceImpl.deleteMPaymentMethod(0L);

        try {
            mPaymentMethodServiceImpl.getMPaymentMethod(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMPaymentMethod_CaseDataNotFound() {
        // given
        given(mPaymentMethodRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mPaymentMethodServiceImpl.deleteMPaymentMethod(0L);

        try {
            mPaymentMethodServiceImpl.getMPaymentMethod(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
