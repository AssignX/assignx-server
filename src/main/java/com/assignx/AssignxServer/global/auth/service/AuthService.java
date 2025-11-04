package com.assignx.AssignxServer.global.auth.service;

import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.exception.MemberExceptionUtils;
import com.assignx.AssignxServer.domain.member.repository.MemberRepository;
import com.assignx.AssignxServer.global.auth.dto.LoginReqDTO;
import com.assignx.AssignxServer.global.auth.dto.TokenReissueReqDTO;
import com.assignx.AssignxServer.global.auth.dto.TokenResDTO;
import com.assignx.AssignxServer.global.auth.exception.AuthExceptionUtils;
import com.assignx.AssignxServer.global.auth.jwt.JwtAuthProvider;
import com.assignx.AssignxServer.global.auth.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthProvider jwtAuthProvider;
    private final RedisService redisService;

    public TokenResDTO login(LoginReqDTO reqDTO) {
        Member member = memberRepository.findByIdNumber(reqDTO.idNumber())
                .orElseThrow(MemberExceptionUtils::MemberNotFound);

        if (!passwordEncoder.matches(reqDTO.password(), member.getPassword())) {
            throw AuthExceptionUtils.PasswordNotMatch();
        }

        String accessToken = jwtAuthProvider.generateAccessToken(member.getIdNumber());
        String refreshToken = jwtAuthProvider.generateRefreshToken(member.getIdNumber());
        redisService.saveRefreshToken(member.getIdNumber(), refreshToken);
        return new TokenResDTO(accessToken, refreshToken);
    }

    public TokenResDTO reissue(TokenReissueReqDTO reqDTO) {
        String idNumber = jwtAuthProvider.getMemberIdNumber(reqDTO.accessToken());
        String storedRefreshToken = redisService.getRefreshToken(idNumber);

        if (storedRefreshToken == null || !storedRefreshToken.equals(reqDTO.refreshToken())) {
            throw AuthExceptionUtils.InvalidRefreshToken();
        }

        String newAccessToken = jwtAuthProvider.generateAccessToken(idNumber);
        String newRefreshToken = jwtAuthProvider.generateRefreshToken(idNumber);
        redisService.saveRefreshToken(idNumber, newRefreshToken);
        return new TokenResDTO(newAccessToken, newRefreshToken);
    }

    public void logout(String accessToken) {
        redisService.deleteRefreshToken(accessToken);
    }

}