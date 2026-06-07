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

import com.github.amsatrio.spring_hospital.model.entity.TCourierDiscount;
import com.github.amsatrio.spring_hospital.repository.TCourierDiscountRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCourierDiscountServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCourierDiscountServiceTests {

    @Mock
    private TCourierDiscountRepository tCourierDiscountRepository;

    @InjectMocks
    private TCourierDiscountServiceImpl tCourierDiscountServiceImpl;

    private TCourierDiscount tCourierDiscount = new TCourierDiscount();

    @BeforeEach
    public void setup() {
        tCourierDiscount.setId(0L);
        tCourierDiscount.setValue(0F);
        tCourierDiscount.setCreatedBy(0L);
        tCourierDiscount.setCreatedOn(new Date());
        tCourierDiscount.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCourierDiscountById_CaseDataFound() {
        // when
        when(tCourierDiscountRepository.findById(0L))
                .thenReturn(Optional.of(tCourierDiscount));

        TCourierDiscount tCourierDiscountDb = tCourierDiscountServiceImpl.getTCourierDiscount(0L);

        // then
        assertEquals(tCourierDiscountDb, tCourierDiscount);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCourierDiscountById_CaseDataNotFound() {
        // when
        when(tCourierDiscountRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCourierDiscountServiceImpl.getTCourierDiscount(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCourierDiscount_CaseDataNotFound() {
        // given
        given(tCourierDiscountRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCourierDiscountRepository.save(tCourierDiscount))
                .thenReturn(tCourierDiscount);

        TCourierDiscount tCourierDiscountDb = tCourierDiscountServiceImpl.createTCourierDiscount(tCourierDiscount);

        // then
        assertEquals(tCourierDiscountDb.getId(), tCourierDiscount.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCourierDiscount_CaseDataFound() {
        // given
        given(tCourierDiscountRepository.findById(0L))
                .willReturn(Optional.of(tCourierDiscount));

        try {
            tCourierDiscountServiceImpl.createTCourierDiscount(tCourierDiscount);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCourierDiscount_CaseDataFound() {
        // given
        given(tCourierDiscountRepository.findById(0L))
                .willReturn(Optional.of(tCourierDiscount));

        // When
        tCourierDiscount.setValue(1F);
        when(tCourierDiscountRepository.save(tCourierDiscount))
                .thenReturn(tCourierDiscount);

        TCourierDiscount tCourierDiscountNew = tCourierDiscountServiceImpl.updateTCourierDiscount(tCourierDiscount);

        // then
        assertEquals(tCourierDiscount.getValue(), tCourierDiscountNew.getValue());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCourierDiscount_CaseDataNotFound() {
        // given
        given(tCourierDiscountRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCourierDiscountServiceImpl.updateTCourierDiscount(tCourierDiscount);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCourierDiscount_CaseDataFound() {
        // given
        given(tCourierDiscountRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCourierDiscountRepository.save(tCourierDiscount))
                .thenReturn(tCourierDiscount);
        tCourierDiscountServiceImpl.createTCourierDiscount(tCourierDiscount);

        // When
        tCourierDiscountServiceImpl.deleteTCourierDiscount(0L);

        try {
            tCourierDiscountServiceImpl.getTCourierDiscount(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCourierDiscount_CaseDataNotFound() {
        // given
        given(tCourierDiscountRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCourierDiscountServiceImpl.deleteTCourierDiscount(0L);

        try {
            tCourierDiscountServiceImpl.getTCourierDiscount(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
