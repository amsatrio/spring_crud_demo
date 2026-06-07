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

import com.github.amsatrio.spring_hospital.model.entity.TCustomerRegisteredCard;
import com.github.amsatrio.spring_hospital.repository.TCustomerRegisteredCardRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerRegisteredCardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerRegisteredCardServiceTests {

    @Mock
    private TCustomerRegisteredCardRepository tCustomerRegisteredCardRepository;

    @InjectMocks
    private TCustomerRegisteredCardServiceImpl tCustomerRegisteredCardServiceImpl;

    private TCustomerRegisteredCard tCustomerRegisteredCard = new TCustomerRegisteredCard();

    @BeforeEach
    public void setup() {
        tCustomerRegisteredCard.setId(0L);
        tCustomerRegisteredCard.setCvv("init");
        tCustomerRegisteredCard.setCreatedBy(0L);
        tCustomerRegisteredCard.setCreatedOn(new Date());
        tCustomerRegisteredCard.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerRegisteredCardById_CaseDataFound() {
        // when
        when(tCustomerRegisteredCardRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerRegisteredCard));

        TCustomerRegisteredCard tCustomerRegisteredCardDb = tCustomerRegisteredCardServiceImpl.getTCustomerRegisteredCard(0L);

        // then
        assertEquals(tCustomerRegisteredCardDb, tCustomerRegisteredCard);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerRegisteredCardById_CaseDataNotFound() {
        // when
        when(tCustomerRegisteredCardRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerRegisteredCardServiceImpl.getTCustomerRegisteredCard(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerRegisteredCard_CaseDataNotFound() {
        // given
        given(tCustomerRegisteredCardRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerRegisteredCardRepository.save(tCustomerRegisteredCard))
                .thenReturn(tCustomerRegisteredCard);

        TCustomerRegisteredCard tCustomerRegisteredCardDb = tCustomerRegisteredCardServiceImpl.createTCustomerRegisteredCard(tCustomerRegisteredCard);

        // then
        assertEquals(tCustomerRegisteredCardDb.getId(), tCustomerRegisteredCard.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerRegisteredCard_CaseDataFound() {
        // given
        given(tCustomerRegisteredCardRepository.findById(0L))
                .willReturn(Optional.of(tCustomerRegisteredCard));

        try {
            tCustomerRegisteredCardServiceImpl.createTCustomerRegisteredCard(tCustomerRegisteredCard);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerRegisteredCard_CaseDataFound() {
        // given
        given(tCustomerRegisteredCardRepository.findById(0L))
                .willReturn(Optional.of(tCustomerRegisteredCard));

        // When
        tCustomerRegisteredCard.setCvv("update");
        when(tCustomerRegisteredCardRepository.save(tCustomerRegisteredCard))
                .thenReturn(tCustomerRegisteredCard);

        TCustomerRegisteredCard tCustomerRegisteredCardNew = tCustomerRegisteredCardServiceImpl.updateTCustomerRegisteredCard(tCustomerRegisteredCard);

        // then
        assertEquals(tCustomerRegisteredCard.getCvv(), tCustomerRegisteredCardNew.getCvv());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerRegisteredCard_CaseDataNotFound() {
        // given
        given(tCustomerRegisteredCardRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerRegisteredCardServiceImpl.updateTCustomerRegisteredCard(tCustomerRegisteredCard);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerRegisteredCard_CaseDataFound() {
        // given
        given(tCustomerRegisteredCardRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerRegisteredCardRepository.save(tCustomerRegisteredCard))
                .thenReturn(tCustomerRegisteredCard);
        tCustomerRegisteredCardServiceImpl.createTCustomerRegisteredCard(tCustomerRegisteredCard);

        // When
        tCustomerRegisteredCardServiceImpl.deleteTCustomerRegisteredCard(0L);

        try {
            tCustomerRegisteredCardServiceImpl.getTCustomerRegisteredCard(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerRegisteredCard_CaseDataNotFound() {
        // given
        given(tCustomerRegisteredCardRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerRegisteredCardServiceImpl.deleteTCustomerRegisteredCard(0L);

        try {
            tCustomerRegisteredCardServiceImpl.getTCustomerRegisteredCard(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
