package io.github.amsatrio.spring_crud_demo.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodata;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MRole;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MUser;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MBiodataRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MRoleRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MUserRepository;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MBiodataService;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MRoleService;
import io.github.amsatrio.spring_crud_demo.util.Generator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DummyDataGenerator implements CommandLineRunner {

    @Autowired
    private MBiodataRepository mBiodataRepository;
    @Autowired
    private MRoleRepository mRoleRepository;
    @Autowired
    private MUserRepository mUserRepository;

    @Override
    public void run(String... args) throws Exception {
        Generator generator = new Generator();

        // // generate m-role
        // List<MRole> mRoles = new ArrayList<>();
        // for (int i = 0; i < 4; i++) {
        //     MRole mRole = new MRole();
        //     mRole.setId((long) (i + 1));
        //     mRole.setName(i == 0 ? "ADMIN" : i == 1 ? "DOKTER" : i == 2 ? "PASIEN" : "FASKES");
        //     mRole.setCode(i == 0 ? "ROLE_ADMIN" : i == 1 ? "ROLE_DOKTER" : i == 2 ? "ROLE_PASIEN" : "ROLE_FASKES");
        //     mRole.setCreatedOn(new Date());
        //     mRole.setCreatedBy(0L);
        //     mRoles.add(mRole);
        // }
        // mRoleRepository.saveAll(mRoles);

        // // generate m-biodata
        // List<MBiodata> mBiodatas = new ArrayList<>();
        // for (int i = 0; i < 1000; i++) {
        //     MBiodata mBiodata = new MBiodata();
        //     mBiodata.setId((long) (i + 1));
        //     mBiodata.setFullname(generator.generateFullName());
        //     mBiodata.setMobilePhone(generator.generatePhoneNumber());
        //     mBiodata.setCreatedBy(0L);
        //     mBiodata.setCreatedOn(new Date());
        //     mBiodata.setIsDelete(false);
        //     mBiodatas.add(mBiodata);
        // }
        // mBiodataRepository.saveAll(mBiodatas);

        // // generate m-user
        // List<MUser> mUsers = new ArrayList<>();
        // for (int i = 0; i < 1000; i++) {
        //     MUser mUser = new MUser();
        //     mUser.setId((long) (i + 1));
        //     mUser.setBiodataId((long) (i + 1));
        //     mUser.setEmail(generator.generateDummyEmail());
        //     mUser.setPassword("P@ssw0rd");
        //     mUser.setCreatedOn(new Date());
        //     mUser.setCreatedBy(0L);
        //     mUser.setIsDelete(false);

        //     Optional<MRole> optionalMRole = mRoleRepository.findById((long) ((i % 4) + 1));
        //     if (optionalMRole.isPresent()) {
        //         mUser.setMRole(optionalMRole.get());
        //         mUser.setRoleId(mUser.getMRole().getId());
        //     }

        //     Optional<MBiodata> optionalMBiodata = mBiodataRepository.findById((long) i);
        //     if (optionalMBiodata.isPresent()) {
        //         mUser.setMBiodata(optionalMBiodata.get());
        //         mUser.setBiodataId(optionalMBiodata.get().getId());
        //     }

        //     mUsers.add(mUser);
        // }
        // mUserRepository.saveAll(mUsers);
    }
}
