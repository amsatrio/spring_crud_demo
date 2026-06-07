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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TMedicalItemPurchaseDetail;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TMedicalItemPurchaseDetailRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TMedicalItemPurchaseDetailServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TMedicalItemPurchaseDetailServiceTests {

    @Mock
    private TMedicalItemPurchaseDetailRepository tMedicalItemPurchaseDetailRepository;

    @InjectMocks
    private TMedicalItemPurchaseDetailServiceImpl tMedicalItemPurchaseDetailServiceImpl;

    private TMedicalItemPurchaseDetail tMedicalItemPurchaseDetail = new TMedicalItemPurchaseDetail();

    @BeforeEach
    public void setup() {
        tMedicalItemPurchaseDetail.setId(0L);
        tMedicalItemPurchaseDetail.setSubTotal(0F);
        tMedicalItemPurchaseDetail.setCreatedBy(0L);
        tMedicalItemPurchaseDetail.setCreatedOn(new Date());
        tMedicalItemPurchaseDetail.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTMedicalItemPurchaseDetailById_CaseDataFound() {
        // when
        when(tMedicalItemPurchaseDetailRepository.findById(0L))
                .thenReturn(Optional.of(tMedicalItemPurchaseDetail));

        TMedicalItemPurchaseDetail tMedicalItemPurchaseDetailDb = tMedicalItemPurchaseDetailServiceImpl.getTMedicalItemPurchaseDetail(0L);

        // then
        assertEquals(tMedicalItemPurchaseDetailDb, tMedicalItemPurchaseDetail);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTMedicalItemPurchaseDetailById_CaseDataNotFound() {
        // when
        when(tMedicalItemPurchaseDetailRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tMedicalItemPurchaseDetailServiceImpl.getTMedicalItemPurchaseDetail(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTMedicalItemPurchaseDetail_CaseDataNotFound() {
        // given
        given(tMedicalItemPurchaseDetailRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tMedicalItemPurchaseDetailRepository.save(tMedicalItemPurchaseDetail))
                .thenReturn(tMedicalItemPurchaseDetail);

        TMedicalItemPurchaseDetail tMedicalItemPurchaseDetailDb = tMedicalItemPurchaseDetailServiceImpl.createTMedicalItemPurchaseDetail(tMedicalItemPurchaseDetail);

        // then
        assertEquals(tMedicalItemPurchaseDetailDb.getId(), tMedicalItemPurchaseDetail.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTMedicalItemPurchaseDetail_CaseDataFound() {
        // given
        given(tMedicalItemPurchaseDetailRepository.findById(0L))
                .willReturn(Optional.of(tMedicalItemPurchaseDetail));

        try {
            tMedicalItemPurchaseDetailServiceImpl.createTMedicalItemPurchaseDetail(tMedicalItemPurchaseDetail);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTMedicalItemPurchaseDetail_CaseDataFound() {
        // given
        given(tMedicalItemPurchaseDetailRepository.findById(0L))
                .willReturn(Optional.of(tMedicalItemPurchaseDetail));

        // When
        tMedicalItemPurchaseDetail.setSubTotal(1F);
        when(tMedicalItemPurchaseDetailRepository.save(tMedicalItemPurchaseDetail))
                .thenReturn(tMedicalItemPurchaseDetail);

        TMedicalItemPurchaseDetail tMedicalItemPurchaseDetailNew = tMedicalItemPurchaseDetailServiceImpl.updateTMedicalItemPurchaseDetail(tMedicalItemPurchaseDetail);

        // then
        assertEquals(tMedicalItemPurchaseDetail.getSubTotal(), tMedicalItemPurchaseDetailNew.getSubTotal());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTMedicalItemPurchaseDetail_CaseDataNotFound() {
        // given
        given(tMedicalItemPurchaseDetailRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tMedicalItemPurchaseDetailServiceImpl.updateTMedicalItemPurchaseDetail(tMedicalItemPurchaseDetail);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTMedicalItemPurchaseDetail_CaseDataFound() {
        // given
        given(tMedicalItemPurchaseDetailRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tMedicalItemPurchaseDetailRepository.save(tMedicalItemPurchaseDetail))
                .thenReturn(tMedicalItemPurchaseDetail);
        tMedicalItemPurchaseDetailServiceImpl.createTMedicalItemPurchaseDetail(tMedicalItemPurchaseDetail);

        // When
        tMedicalItemPurchaseDetailServiceImpl.deleteTMedicalItemPurchaseDetail(0L);

        try {
            tMedicalItemPurchaseDetailServiceImpl.getTMedicalItemPurchaseDetail(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTMedicalItemPurchaseDetail_CaseDataNotFound() {
        // given
        given(tMedicalItemPurchaseDetailRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tMedicalItemPurchaseDetailServiceImpl.deleteTMedicalItemPurchaseDetail(0L);

        try {
            tMedicalItemPurchaseDetailServiceImpl.getTMedicalItemPurchaseDetail(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
