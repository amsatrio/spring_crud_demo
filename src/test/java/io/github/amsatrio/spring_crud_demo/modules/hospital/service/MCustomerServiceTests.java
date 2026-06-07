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

import com.github.amsatrio.spring_hospital.model.entity.MCustomer;
import com.github.amsatrio.spring_hospital.repository.MCustomerRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MCustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MCustomerServiceTests {

    @Mock
    private MCustomerRepository mCustomerRepository;

    @InjectMocks
    private MCustomerServiceImpl mCustomerServiceImpl;

    private MCustomer mCustomer = new MCustomer();

    @BeforeEach
    public void setup() {
        mCustomer.setId(0L);
        mCustomer.setWeight(0F);
        mCustomer.setCreatedBy(0L);
        mCustomer.setCreatedOn(new Date());
        mCustomer.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMCustomerById_CaseDataFound() {
        // when
        when(mCustomerRepository.findById(0L))
                .thenReturn(Optional.of(mCustomer));

        MCustomer mCustomerDb = mCustomerServiceImpl.getMCustomer(0L);

        // then
        assertEquals(mCustomerDb, mCustomer);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMCustomerById_CaseDataNotFound() {
        // when
        when(mCustomerRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mCustomerServiceImpl.getMCustomer(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMCustomer_CaseDataNotFound() {
        // given
        given(mCustomerRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mCustomerRepository.save(mCustomer))
                .thenReturn(mCustomer);

        MCustomer mCustomerDb = mCustomerServiceImpl.createMCustomer(mCustomer);

        // then
        assertEquals(mCustomerDb.getId(), mCustomer.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMCustomer_CaseDataFound() {
        // given
        given(mCustomerRepository.findById(0L))
                .willReturn(Optional.of(mCustomer));

        try {
            mCustomerServiceImpl.createMCustomer(mCustomer);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMCustomer_CaseDataFound() {
        // given
        given(mCustomerRepository.findById(0L))
                .willReturn(Optional.of(mCustomer));

        // When
        mCustomer.setWeight(1F);
        when(mCustomerRepository.save(mCustomer))
                .thenReturn(mCustomer);

        MCustomer mCustomerNew = mCustomerServiceImpl.updateMCustomer(mCustomer);

        // then
        assertEquals(mCustomer.getWeight(), mCustomerNew.getWeight());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMCustomer_CaseDataNotFound() {
        // given
        given(mCustomerRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mCustomerServiceImpl.updateMCustomer(mCustomer);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMCustomer_CaseDataFound() {
        // given
        given(mCustomerRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mCustomerRepository.save(mCustomer))
                .thenReturn(mCustomer);
        mCustomerServiceImpl.createMCustomer(mCustomer);

        // When
        mCustomerServiceImpl.deleteMCustomer(0L);

        try {
            mCustomerServiceImpl.getMCustomer(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMCustomer_CaseDataNotFound() {
        // given
        given(mCustomerRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mCustomerServiceImpl.deleteMCustomer(0L);

        try {
            mCustomerServiceImpl.getMCustomer(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
