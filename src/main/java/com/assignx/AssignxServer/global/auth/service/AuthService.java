package com.assignx.AssignxServer.global.auth.service;

import com.assignx.AssignxServer.domain.member.entity.Member;
import com.assignx.AssignxServer.domain.member.exception.MemberExceptionUtils;
import com.assignx.AssignxServer.domain.member.repository.MemberRepository;
import com.assignx.AssignxServer.global.auth.dto.LoginReqDTO;
import com.assignx.AssignxServer.global.auth.dto.LoginResDTO;
import com.assignx.AssignxServer.global.auth.dto.TokenReissueReqDTO;
import com.assignx.AssignxServer.global.auth.dto.TokenReissueResDTO;
import com.assignx.AssignxServer.global.auth.exception.AuthExceptionUtils;
import com.assignx.AssignxServer.global.auth.jwt.JwtAuthProvider;
import com.assignx.AssignxServer.global.auth.redis.RedisService;
import io.jsonwebtoken.Claims;
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

    public LoginResDTO login(LoginReqDTO reqDTO) {
        Member member = memberRepository.findByIdNumber(reqDTO.idNumber())
                .orElseThrow(MemberExceptionUtils::MemberNotFound);

        if (!passwordEncoder.matches(reqDTO.password(), member.getPassword())) {
            throw AuthExceptionUtils.PasswordNotMatch();
        }

        String accessToken = jwtAuthProvider.generateAccessToken(member.getIdNumber(), member.getRole());
        String refreshToken = jwtAuthProvider.generateRefreshToken(member.getIdNumber());
        redisService.saveRefreshToken(member.getIdNumber(), refreshToken);
        return LoginResDTO.fromEntity(member, accessToken, refreshToken);
    }

    public TokenReissueResDTO reissue(TokenReissueReqDTO reqDTO) {
        Claims claims = jwtAuthProvider.extractClaims(reqDTO.accessToken());
        String idNumber = claims.get("idNumber").toString();
        Member member = memberRepository.findByIdNumber(idNumber)
                .orElseThrow(MemberExceptionUtils::MemberNotFound);

        String storedRefreshToken = redisService.getRefreshToken(member.getIdNumber());

        if (storedRefreshToken == null || !storedRefreshToken.equals(reqDTO.refreshToken())) {
            throw AuthExceptionUtils.InvalidRefreshToken();
        }

        String newAccessToken = jwtAuthProvider.generateAccessToken(member.getIdNumber(), member.getRole());
        String newRefreshToken = jwtAuthProvider.generateRefreshToken(member.getIdNumber());
        redisService.saveRefreshToken(idNumber, newRefreshToken);
        return new TokenReissueResDTO(newAccessToken, newRefreshToken);
    }

    public void logout(String accessToken) {
        redisService.deleteRefreshToken(accessToken);
    }

}