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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MCustomerMember;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MCustomerMemberRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.MCustomerMemberServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MCustomerMemberServiceTests {

    @Mock
    private MCustomerMemberRepository mCustomerMemberRepository;

    @InjectMocks
    private MCustomerMemberServiceImpl mCustomerMemberServiceImpl;

    private MCustomerMember mCustomerMember = new MCustomerMember();

    @BeforeEach
    public void setup() {
        mCustomerMember.setId(0L);
        mCustomerMember.setCustomerRelationId(0L);
        mCustomerMember.setCreatedBy(0L);
        mCustomerMember.setCreatedOn(new Date());
        mCustomerMember.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getMCustomerMemberById_CaseDataFound() {
        // when
        when(mCustomerMemberRepository.findById(0L))
                .thenReturn(Optional.of(mCustomerMember));

        MCustomerMember mCustomerMemberDb = mCustomerMemberServiceImpl.getMCustomerMember(0L);

        // then
        assertEquals(mCustomerMemberDb, mCustomerMember);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getMCustomerMemberById_CaseDataNotFound() {
        // when
        when(mCustomerMemberRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            mCustomerMemberServiceImpl.getMCustomerMember(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createMCustomerMember_CaseDataNotFound() {
        // given
        given(mCustomerMemberRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(mCustomerMemberRepository.save(mCustomerMember))
                .thenReturn(mCustomerMember);

        MCustomerMember mCustomerMemberDb = mCustomerMemberServiceImpl.createMCustomerMember(mCustomerMember);

        // then
        assertEquals(mCustomerMemberDb.getId(), mCustomerMember.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createMCustomerMember_CaseDataFound() {
        // given
        given(mCustomerMemberRepository.findById(0L))
                .willReturn(Optional.of(mCustomerMember));

        try {
            mCustomerMemberServiceImpl.createMCustomerMember(mCustomerMember);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateMCustomerMember_CaseDataFound() {
        // given
        given(mCustomerMemberRepository.findById(0L))
                .willReturn(Optional.of(mCustomerMember));

        // When
        mCustomerMember.setCustomerRelationId(1L);
        when(mCustomerMemberRepository.save(mCustomerMember))
                .thenReturn(mCustomerMember);

        MCustomerMember mCustomerMemberNew = mCustomerMemberServiceImpl.updateMCustomerMember(mCustomerMember);

        // then
        assertEquals(mCustomerMember.getCustomerRelationId(), mCustomerMemberNew.getCustomerRelationId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateMCustomerMember_CaseDataNotFound() {
        // given
        given(mCustomerMemberRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            mCustomerMemberServiceImpl.updateMCustomerMember(mCustomerMember);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteMCustomerMember_CaseDataFound() {
        // given
        given(mCustomerMemberRepository.findById(0L))
                .willReturn(Optional.empty());
        when(mCustomerMemberRepository.save(mCustomerMember))
                .thenReturn(mCustomerMember);
        mCustomerMemberServiceImpl.createMCustomerMember(mCustomerMember);

        // When
        mCustomerMemberServiceImpl.deleteMCustomerMember(0L);

        try {
            mCustomerMemberServiceImpl.getMCustomerMember(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteMCustomerMember_CaseDataNotFound() {
        // given
        given(mCustomerMemberRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        mCustomerMemberServiceImpl.deleteMCustomerMember(0L);

        try {
            mCustomerMemberServiceImpl.getMCustomerMember(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
