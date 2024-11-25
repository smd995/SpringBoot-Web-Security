package org.zerock.boardex_1.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.boardex_1.domain.Member;
import org.zerock.boardex_1.domain.MemberRole;
import org.zerock.boardex_1.repository.MemberRepository;
import org.zerock.boardex_1.security.dto.MemberSecurityDTO;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest.....");
        log.info(userRequest);

        log.info("oauth2 user.................................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("NAME: " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String nickname = null;

        switch (clientName) {
            case "kakao":
                nickname = getKakaoNickName(paramMap);
        }

        log.info("------------------------------");
        log.info(nickname);
        log.info("--------------------------------");

        return generateDTO(nickname, paramMap);
    }

    private MemberSecurityDTO generateDTO(String nickname, Map<String, Object> params) {

        Optional<Member> result = memberRepository.findByMid(nickname);

        // 데이터베이스에 해당 이메일을 사용자가 없다면
        if (result.isEmpty()) {
            // 회원 추가 -- mid는 nickname / 패스워드는 1111
            Member member = Member.builder()
                    .mid(nickname)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(nickname + "@example.com")
                    .social(true)
                    .build();

            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            // MemberSecurityDTO 구성 및 반환
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(nickname, "1111", nickname + "@example.com", false, true, Arrays
                            .asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;
        } else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO =
                    new MemberSecurityDTO(
                            member.getMid(),
                            member.getMpw(),
                            member.getEmail(),
                            member.isDel(),
                            member.isSocial(),
                            member.getRoleSet()
                                    .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                                    .collect(Collectors.toList())
                    );

            return memberSecurityDTO;
        }
    }

    private String getKakaoNickName(Map<String, Object> paramMap) {
        log.info("KAKAO----------------------------------------");

        Object value = paramMap.get("kakao_account");
        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        // profile을 가져옵니다.
        LinkedHashMap profileMap = (LinkedHashMap) accountMap.get("profile");

        // profile에서 nickname을 추출합니다.
        String nickname = (String) profileMap.get("nickname");

        log.info("nickname: " + nickname);

        return nickname;
    }


}
