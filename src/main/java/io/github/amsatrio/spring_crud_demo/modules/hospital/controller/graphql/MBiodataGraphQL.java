package io.github.amsatrio.spring_crud_demo.modules.hospital.controller.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.github.amsatrio.spring_crud_demo.dto.exception.CustomGraphQLException;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodata;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MBiodataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "biodata")
@RequiredArgsConstructor
public class MBiodataGraphQL {
    private final MBiodataService mBiodataService;

    @QueryMapping
    public MBiodata getMBiodataById(@Argument long id) {
        try {
            return mBiodataService.getMBiodata(id);
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error("getMBiodataById error:", httpClientErrorException);
            throw new CustomGraphQLException(httpClientErrorException.getStatusCode().value(),
                    httpClientErrorException.getMessage());
        } catch (Exception exception) {
            log.error("getMBiodataById error:", exception);
            throw new CustomGraphQLException(400, exception.getMessage());
        }
    }

    @MutationMapping
    public Boolean createMBiodata(@Argument(value = "mBiodataInput") MBiodata mBiodata) {
        try {
            mBiodataService.createMBiodata(mBiodata);
            return true;
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error("createMBiodata error:", httpClientErrorException);
            throw new CustomGraphQLException(httpClientErrorException.getStatusCode().value(),
                    httpClientErrorException.getMessage());
        } catch (Exception exception) {
            log.error("createMBiodata error:", exception);
            throw new CustomGraphQLException(400, exception.getMessage());
        }
    }

    @MutationMapping
    public Boolean updateMBiodata(@Argument(value = "id") Long id,
            @Argument(value = "mBiodataInput") MBiodata mBiodata) {
        try {
            mBiodata.setId(id);
            mBiodataService.updateMBiodata(mBiodata);
            return true;
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error("updateMBiodata error:", httpClientErrorException);
            throw new CustomGraphQLException(httpClientErrorException.getStatusCode().value(),
                    httpClientErrorException.getMessage());
        } catch (Exception exception) {
            log.error("updateMBiodata error:", exception);
            throw new CustomGraphQLException(400, exception.getMessage());
        }
    }

    @MutationMapping
    public Boolean deleteMBiodata(@Argument(value = "id") Long id) {
        try {
            mBiodataService.deleteMBiodata(id);
            return true;
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error("deleteMBiodata error:", httpClientErrorException);
            throw new CustomGraphQLException(httpClientErrorException.getStatusCode().value(),
                    httpClientErrorException.getMessage());
        } catch (Exception exception) {
            log.error("deleteMBiodata error:", exception);
            throw new CustomGraphQLException(400, exception.getMessage());
        }
    }
}
