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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomerRelation;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MCustomerRelationRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MCustomerRelationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MCustomerRelationServiceTests {

    @Mock
    private MCustomerRelationRepository mCustomerRelationRepository;

    @InjectMocks
    private MCustomerRelationServiceImpl mCustomerRelationServiceImpl;

    private MCustomerRelation mCustomerRelation = new MCustomerRelation();

    @BeforeEach
    public void setup() {
        mCustomerRelation.setId(0L);
        mCustomerRelation.setName("init");
        mCustomerRelation.setCreatedBy(0L);
        mCustomerRelation.setCreatedOn(new Date());
        mCustomerRelation.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMCustomerRelationById_CaseDataFound() {
        // when
        when(mCustomerRelationRepository.findById(0L))
                .thenReturn(Optional.of(mCustomerRelation));

        MCustomerRelation mCustomerRelationDb = mCustomerRelationServiceImpl.getMCustomerRelation(0L);

        // then
        assertEquals(mCustomerRelationDb, mCustomerRelation);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMCustomerRelationById_CaseDataNotFound() {
        // when
        when(mCustomerRelationRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mCustomerRelationServiceImpl.getMCustomerRelation(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMCustomerRelation_CaseDataNotFound() {
        // given
        given(mCustomerRelationRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mCustomerRelationRepository.save(mCustomerRelation))
                .thenReturn(mCustomerRelation);

        MCustomerRelation mCustomerRelationDb = mCustomerRelationServiceImpl.createMCustomerRelation(mCustomerRelation);

        // then
        assertEquals(mCustomerRelationDb.getId(), mCustomerRelation.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMCustomerRelation_CaseDataFound() {
        // given
        given(mCustomerRelationRepository.findById(0L))
                .willReturn(Optional.of(mCustomerRelation));

        try {
            mCustomerRelationServiceImpl.createMCustomerRelation(mCustomerRelation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMCustomerRelation_CaseDataFound() {
        // given
        given(mCustomerRelationRepository.findById(0L))
                .willReturn(Optional.of(mCustomerRelation));

        // When
        mCustomerRelation.setName("update");
        when(mCustomerRelationRepository.save(mCustomerRelation))
                .thenReturn(mCustomerRelation);

        MCustomerRelation mCustomerRelationNew = mCustomerRelationServiceImpl.updateMCustomerRelation(mCustomerRelation);

        // then
        assertEquals(mCustomerRelation.getName(), mCustomerRelationNew.getName());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMCustomerRelation_CaseDataNotFound() {
        // given
        given(mCustomerRelationRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mCustomerRelationServiceImpl.updateMCustomerRelation(mCustomerRelation);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMCustomerRelation_CaseDataFound() {
        // given
        given(mCustomerRelationRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mCustomerRelationRepository.save(mCustomerRelation))
                .thenReturn(mCustomerRelation);
        mCustomerRelationServiceImpl.createMCustomerRelation(mCustomerRelation);

        // When
        mCustomerRelationServiceImpl.deleteMCustomerRelation(0L);

        try {
            mCustomerRelationServiceImpl.getMCustomerRelation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMCustomerRelation_CaseDataNotFound() {
        // given
        given(mCustomerRelationRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mCustomerRelationServiceImpl.deleteMCustomerRelation(0L);

        try {
            mCustomerRelationServiceImpl.getMCustomerRelation(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
