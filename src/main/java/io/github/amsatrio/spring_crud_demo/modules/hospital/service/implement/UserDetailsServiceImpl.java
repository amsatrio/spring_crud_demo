package io.github.amsatrio.spring_crud_demo.modules.hospital.service.implement;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MUser;
import io.github.amsatrio.spring_crud_demo.modules.hospital.repository.MUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private MUserRepository mUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MUser mUser = mUserRepository.findByEmailAndIsDeleteFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(mUser);
    }

    @Transactional
    public UserDetails loadUserByUserId(Long id) {
        MUser mUser = mUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id.toString()));

        return UserDetailsImpl.build(mUser);
    }

}
