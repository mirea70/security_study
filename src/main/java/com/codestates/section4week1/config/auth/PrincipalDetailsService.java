package com.codestates.section4week1.config.auth;

import com.codestates.section4week1.model.Member;
import com.codestates.section4week1.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByUsername(username);
        System.out.println("username : " + username);
        if(memberEntity != null) {
            return new PrincipalDetails(memberEntity);
        }
        return null;
    }

}
