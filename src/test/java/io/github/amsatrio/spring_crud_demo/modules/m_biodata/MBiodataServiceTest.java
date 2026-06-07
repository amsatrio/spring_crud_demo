package io.github.amsatrio.spring_crud_demo.modules.m_biodata;

import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterDataType;
import io.github.amsatrio.spring_crud_demo.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.spring_crud_demo.dto.request.FilterRequest;
import io.github.amsatrio.spring_crud_demo.dto.request.SortRequest;
import io.github.amsatrio.spring_crud_demo.modules.auth.UserDetailsImpl;
import io.github.amsatrio.spring_crud_demo.modules.hospital.m_biodata.MBiodata;
import io.github.amsatrio.spring_crud_demo.modules.hospital.m_biodata.MBiodataRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.m_biodata.MBiodataService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MBiodataServiceTest {

    @Mock
    private MBiodataRepository mBiodataRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MBiodataService mBiodataService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should return MBiodata when ID exists")
    void getMBiodata_Success() {
        // Arrange
        Long id = 1L;
        MBiodata mockData = new MBiodata();
        mockData.setId(id);
        when(mBiodataRepository.findById(id)).thenReturn(Optional.of(mockData));

        // Act
        MBiodata result = mBiodataService.getMBiodata(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(mBiodataRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw 404 when MBiodata ID is not found")
    void getMBiodata_NotFound() {
        // Arrange
        when(mBiodataRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            mBiodataService.getMBiodata(1L);
        });
    }

    @Test
    @DisplayName("Should create MBiodata with audit fields")
    void createMBiodata_Success() {
        // Arrange
        MBiodata input = new MBiodata();
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        
        when(userDetails.getId()).thenReturn(100L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(mBiodataRepository.save(any(MBiodata.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        MBiodata created = mBiodataService.createMBiodata(input);

        // Assert
        assertEquals(100L, created.getCreatedBy());
        assertNotNull(created.getCreatedOn());
        assertFalse(created.getIsDelete());
        verify(mBiodataRepository).save(input);
    }

    @Test
    @DisplayName("Create: Throw 400 when ID already exists")
    void createMBiodata_Conflict() {
        MBiodata input = new MBiodata();
        input.setId(1L);
        when(mBiodataRepository.findById(1L)).thenReturn(Optional.of(new MBiodata()));

        assertThrows(HttpClientErrorException.class, () -> mBiodataService.createMBiodata(input));
    }

    @Test
    @DisplayName("Audit: Anonymous User fallback to 0L")
    void createMBiodata_Anonymous() {
        MBiodata input = new MBiodata();
        AnonymousAuthenticationToken anon = mock(AnonymousAuthenticationToken.class);
        when(securityContext.getAuthentication()).thenReturn(anon);

        mBiodataService.createMBiodata(input);

        assertEquals(0L, input.getCreatedBy());
    }

    @Test
    @DisplayName("Should update MBiodata and set modified audit fields")
    void updateMBiodata_Success() {
        // Arrange
        Long id = 1L;
        MBiodata existing = new MBiodata();
        existing.setId(id);
        existing.setCreatedBy(50L);

        MBiodata updateInfo = new MBiodata();
        updateInfo.setId(id);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(100L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        
        when(mBiodataRepository.findById(id)).thenReturn(Optional.of(existing));
        when(mBiodataRepository.save(any(MBiodata.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        MBiodata result = mBiodataService.updateMBiodata(updateInfo);

        // Assert
        assertEquals(50L, result.getCreatedBy(), "Should preserve original creator");
        assertEquals(100L, result.getModifiedBy(), "Should set current user as modifier");
        assertNotNull(result.getModifiedOn());
    }

    @Test
    @DisplayName("Update: Handle Soft Delete Logic")
    void updateMBiodata_WithSoftDelete() {
        Long id = 1L;
        MBiodata existing = new MBiodata();
        existing.setId(id);
        
        MBiodata update = new MBiodata();
        update.setId(id);
        update.setIsDelete(true);

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(100L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(mBiodataRepository.findById(id)).thenReturn(Optional.of(existing));

        mBiodataService.updateMBiodata(update);

        assertEquals(100L, update.getDeletedBy());
        assertNotNull(update.getDeletedOn());
    }

    @Test
    @DisplayName("Update: Throw 404 when ID not found")
    void updateMBiodata_NotFound() {
        MBiodata input = new MBiodata();
        input.setId(999L);
        when(mBiodataRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(HttpClientErrorException.class, () -> mBiodataService.updateMBiodata(input));
    }

    @Test
    @DisplayName("Should fetch a page with empty filters and sorts")
    void getPageMBiodata_SimpleFetch() {
        // Arrange
        Page<MBiodata> mockPage = new PageImpl<>(Collections.emptyList());
        when(mBiodataRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(mockPage);

        // Act
        Page<MBiodata> result = mBiodataService.getPageMBiodata(
                0, 10, new ArrayList<>(), new ArrayList<>(), "");

        // Assert
        assertNotNull(result);
        verify(mBiodataRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Complex Page Request: Global Filter, Sorting, and Negative Size")
    void getPageMBiodata_Complex() {
        // Arrange
        SortRequest sort = new SortRequest();
        sort.setId("name");
        sort.setDesc(true);

        // Act
        // size < 0 triggers size = Integer.MAX_VALUE
        mBiodataService.getPageMBiodata(0, -1, List.of(sort), new ArrayList<>(), "search_term");

        // Assert
        verify(mBiodataRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    // --- CUSTOM FILTERING BRANCHES ---

    @Test
    @DisplayName("Filter: BETWEEN Date")
    void getPageMBiodata_FilterBetweenDate() {
        FilterRequest filter = new FilterRequest();
        filter.setId("createdOn");
        filter.setMatchMode(FilterMatchMode.BETWEEN);
        filter.setDataType(FilterDataType.DATE);
        // This triggers the JSON list parsing logic in your service
        filter.setValue("[\"2023-01-01 00:00:00\", \"2023-12-31 23:59:59\"]"); 
        // Note: Your service code actually splits by " - " for Date BETWEEN, let's trigger that:
        filter.setValue("2023-01-01 00:00:00 - 2023-12-31 23:59:59");

        mBiodataService.getPageMBiodata(0, 10, new ArrayList<>(), List.of(filter), "");
        verify(mBiodataRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Filter: EQUALS Boolean and Number")
    void getPageMBiodata_FilterEqualsMisc() {
        FilterRequest f1 = new FilterRequest();
        f1.setId("isDelete");
        f1.setMatchMode(FilterMatchMode.EQUALS);
        f1.setDataType(FilterDataType.BOOLEAN);
        f1.setValue("true");

        FilterRequest f2 = new FilterRequest();
        f2.setId("age");
        f2.setMatchMode(FilterMatchMode.EQUALS);
        f2.setDataType(FilterDataType.NUMBER);
        f2.setValue(25);

        mBiodataService.getPageMBiodata(0, 10, new ArrayList<>(), List.of(f1, f2), "");
        verify(mBiodataRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle deleteById call")
    void deleteMBiodata_Success() {
        // Act
        mBiodataService.deleteMBiodata(1L);

        // Assert
        verify(mBiodataRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete List Success")
    void deleteAllMBiodata_Success() {
        List<MBiodata> list = List.of(new MBiodata());
        mBiodataService.deleteAllMBiodata(list);
        verify(mBiodataRepository).deleteAll(list);
    }
}