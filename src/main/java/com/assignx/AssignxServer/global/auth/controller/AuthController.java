package com.assignx.AssignxServer.global.auth.controller;

import com.assignx.AssignxServer.global.auth.dto.LoginReqDTO;
import com.assignx.AssignxServer.global.auth.dto.TokenReissueReqDTO;
import com.assignx.AssignxServer.global.auth.dto.TokenResDTO;
import com.assignx.AssignxServer.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResDTO> login(@RequestBody LoginReqDTO reqDTO) {
        TokenResDTO res = authService.login(reqDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResDTO> reissue(@RequestBody TokenReissueReqDTO reqDTO) {
        TokenResDTO res = authService.reissue(reqDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout("");
        return ResponseEntity.ok().build();
    }

}