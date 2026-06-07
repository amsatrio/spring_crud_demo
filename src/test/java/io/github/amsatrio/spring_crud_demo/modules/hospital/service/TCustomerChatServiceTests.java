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

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.TCustomerChat;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.TCustomerChatRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TCustomerChatServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TCustomerChatServiceTests {

    @Mock
    private TCustomerChatRepository tCustomerChatRepository;

    @InjectMocks
    private TCustomerChatServiceImpl tCustomerChatServiceImpl;

    private TCustomerChat tCustomerChat = new TCustomerChat();

    @BeforeEach
    public void setup() {
        tCustomerChat.setId(0L);
        tCustomerChat.setDoctorId(0L);
        tCustomerChat.setCreatedBy(0L);
        tCustomerChat.setCreatedOn(new Date());
        tCustomerChat.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTCustomerChatById_CaseDataFound() {
        // when
        when(tCustomerChatRepository.findById(0L))
                .thenReturn(Optional.of(tCustomerChat));

        TCustomerChat tCustomerChatDb = tCustomerChatServiceImpl.getTCustomerChat(0L);

        // then
        assertEquals(tCustomerChatDb, tCustomerChat);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTCustomerChatById_CaseDataNotFound() {
        // when
        when(tCustomerChatRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tCustomerChatServiceImpl.getTCustomerChat(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTCustomerChat_CaseDataNotFound() {
        // given
        given(tCustomerChatRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tCustomerChatRepository.save(tCustomerChat))
                .thenReturn(tCustomerChat);

        TCustomerChat tCustomerChatDb = tCustomerChatServiceImpl.createTCustomerChat(tCustomerChat);

        // then
        assertEquals(tCustomerChatDb.getId(), tCustomerChat.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTCustomerChat_CaseDataFound() {
        // given
        given(tCustomerChatRepository.findById(0L))
                .willReturn(Optional.of(tCustomerChat));

        try {
            tCustomerChatServiceImpl.createTCustomerChat(tCustomerChat);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTCustomerChat_CaseDataFound() {
        // given
        given(tCustomerChatRepository.findById(0L))
                .willReturn(Optional.of(tCustomerChat));

        // When
        tCustomerChat.setDoctorId(1L);
        when(tCustomerChatRepository.save(tCustomerChat))
                .thenReturn(tCustomerChat);

        TCustomerChat tCustomerChatNew = tCustomerChatServiceImpl.updateTCustomerChat(tCustomerChat);

        // then
        assertEquals(tCustomerChat.getDoctorId(), tCustomerChatNew.getDoctorId());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTCustomerChat_CaseDataNotFound() {
        // given
        given(tCustomerChatRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tCustomerChatServiceImpl.updateTCustomerChat(tCustomerChat);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTCustomerChat_CaseDataFound() {
        // given
        given(tCustomerChatRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tCustomerChatRepository.save(tCustomerChat))
                .thenReturn(tCustomerChat);
        tCustomerChatServiceImpl.createTCustomerChat(tCustomerChat);

        // When
        tCustomerChatServiceImpl.deleteTCustomerChat(0L);

        try {
            tCustomerChatServiceImpl.getTCustomerChat(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTCustomerChat_CaseDataNotFound() {
        // given
        given(tCustomerChatRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tCustomerChatServiceImpl.deleteTCustomerChat(0L);

        try {
            tCustomerChatServiceImpl.getTCustomerChat(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
