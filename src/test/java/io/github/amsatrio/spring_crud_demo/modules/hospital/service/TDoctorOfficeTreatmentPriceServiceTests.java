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

import com.github.amsatrio.spring_hospital.model.entity.TDoctorOfficeTreatmentPrice;
import com.github.amsatrio.spring_hospital.repository.TDoctorOfficeTreatmentPriceRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement.TDoctorOfficeTreatmentPriceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TDoctorOfficeTreatmentPriceServiceTests {

    @Mock
    private TDoctorOfficeTreatmentPriceRepository tDoctorOfficeTreatmentPriceRepository;

    @InjectMocks
    private TDoctorOfficeTreatmentPriceServiceImpl tDoctorOfficeTreatmentPriceServiceImpl;

    private TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPrice = new TDoctorOfficeTreatmentPrice();

    @BeforeEach
    public void setup() {
        tDoctorOfficeTreatmentPrice.setId(0L);
        tDoctorOfficeTreatmentPrice.setPriceUntilFrom(0F);
        tDoctorOfficeTreatmentPrice.setCreatedBy(0L);
        tDoctorOfficeTreatmentPrice.setCreatedOn(new Date());
        tDoctorOfficeTreatmentPrice.setIsDelete(false);

        // Set up the security context with anonymous authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new AnonymousAuthenticationToken("key", "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @DisplayName("Test for get {table_name_snake_case} by id")
    @Test
    void getTDoctorOfficeTreatmentPriceById_CaseDataFound() {
        // when
        when(tDoctorOfficeTreatmentPriceRepository.findById(0L))
                .thenReturn(Optional.of(tDoctorOfficeTreatmentPrice));

        TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPriceDb = tDoctorOfficeTreatmentPriceServiceImpl.getTDoctorOfficeTreatmentPrice(0L);

        // then
        assertEquals(tDoctorOfficeTreatmentPriceDb, tDoctorOfficeTreatmentPrice);
    }

    @DisplayName("Test for get {table_name_snake_case} by id when data not found")
    @Test
    void getTDoctorOfficeTreatmentPriceById_CaseDataNotFound() {
        // when
        when(tDoctorOfficeTreatmentPriceRepository.findById(0L))
                .thenReturn(Optional.empty());

        try {
            tDoctorOfficeTreatmentPriceServiceImpl.getTDoctorOfficeTreatmentPrice(0L);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for create {table_name_snake_case} when data not found")
    @Test
    void createTDoctorOfficeTreatmentPrice_CaseDataNotFound() {
        // given
        given(tDoctorOfficeTreatmentPriceRepository.findById(0L))
                .willReturn(Optional.empty());

        // when
        when(tDoctorOfficeTreatmentPriceRepository.save(tDoctorOfficeTreatmentPrice))
                .thenReturn(tDoctorOfficeTreatmentPrice);

        TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPriceDb = tDoctorOfficeTreatmentPriceServiceImpl.createTDoctorOfficeTreatmentPrice(tDoctorOfficeTreatmentPrice);

        // then
        assertEquals(tDoctorOfficeTreatmentPriceDb.getId(), tDoctorOfficeTreatmentPrice.getId());
    }

    @DisplayName("Test for create {table_name_snake_case} when data found")
    @Test
    void createTDoctorOfficeTreatmentPrice_CaseDataFound() {
        // given
        given(tDoctorOfficeTreatmentPriceRepository.findById(0L))
                .willReturn(Optional.of(tDoctorOfficeTreatmentPrice));

        try {
            tDoctorOfficeTreatmentPriceServiceImpl.createTDoctorOfficeTreatmentPrice(tDoctorOfficeTreatmentPrice);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for update {table_name_snake_case} when data found")
    @Test
    void updateTDoctorOfficeTreatmentPrice_CaseDataFound() {
        // given
        given(tDoctorOfficeTreatmentPriceRepository.findById(0L))
                .willReturn(Optional.of(tDoctorOfficeTreatmentPrice));

        // When
        tDoctorOfficeTreatmentPrice.setPriceUntilFrom(1F);
        when(tDoctorOfficeTreatmentPriceRepository.save(tDoctorOfficeTreatmentPrice))
                .thenReturn(tDoctorOfficeTreatmentPrice);

        TDoctorOfficeTreatmentPrice tDoctorOfficeTreatmentPriceNew = tDoctorOfficeTreatmentPriceServiceImpl.updateTDoctorOfficeTreatmentPrice(tDoctorOfficeTreatmentPrice);

        // then
        assertEquals(tDoctorOfficeTreatmentPrice.getPriceUntilFrom(), tDoctorOfficeTreatmentPriceNew.getPriceUntilFrom());
    }

    @DisplayName("Test for update {table_name_snake_case} when data not found")
    @Test
    void updateTDoctorOfficeTreatmentPrice_CaseDataNotFound() {
        // given
        given(tDoctorOfficeTreatmentPriceRepository.findById(0L))
                .willReturn(Optional.empty());

        try {
            tDoctorOfficeTreatmentPriceServiceImpl.updateTDoctorOfficeTreatmentPrice(tDoctorOfficeTreatmentPrice);
        } catch (Exception exception) {
            assertTrue(exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data found")
    @Test
    void deleteTDoctorOfficeTreatmentPrice_CaseDataFound() {
        // given
        given(tDoctorOfficeTreatmentPriceRepository.findById(0L))
                .willReturn(Optional.empty());
        when(tDoctorOfficeTreatmentPriceRepository.save(tDoctorOfficeTreatmentPrice))
                .thenReturn(tDoctorOfficeTreatmentPrice);
        tDoctorOfficeTreatmentPriceServiceImpl.createTDoctorOfficeTreatmentPrice(tDoctorOfficeTreatmentPrice);

        // When
        tDoctorOfficeTreatmentPriceServiceImpl.deleteTDoctorOfficeTreatmentPrice(0L);

        try {
            tDoctorOfficeTreatmentPriceServiceImpl.getTDoctorOfficeTreatmentPrice(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }

    @DisplayName("Test for delete {table_name_snake_case} when data not found")
    @Test
    void deleteTDoctorOfficeTreatmentPrice_CaseDataNotFound() {
        // given
        given(tDoctorOfficeTreatmentPriceRepository.findById(0L))
                .willReturn(Optional.empty());

        // When
        tDoctorOfficeTreatmentPriceServiceImpl.deleteTDoctorOfficeTreatmentPrice(0L);

        try {
            tDoctorOfficeTreatmentPriceServiceImpl.getTDoctorOfficeTreatmentPrice(0L);
        } catch (Exception exception) {
            // then
            assertEquals(true, exception instanceof HttpClientErrorException);
        }
    }
}
