package com.guohenry.matchapp.controller;


import com.guohenry.matchapp.dao.MemberDao;
import com.guohenry.matchapp.dto.LoginRequest;
import com.guohenry.matchapp.model.Member;
import com.guohenry.matchapp.service.MemberService;
import com.guohenry.matchapp.service.RedisService;
import com.guohenry.matchapp.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDao memberDao;

    // 會員登入，驗證成功則回傳 JWT
    @PostMapping("/login")
    @Operation(summary = "會員登入", description = "登入成功會回傳 JWT Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登入成功"),
            @ApiResponse(responseCode = "400", description = "帳號或密碼錯誤")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest ){

        // 認證帳號與密碼
        Member member = memberService.login(loginRequest.getEmail(),loginRequest .getPassword());

        if(member == null){
            return ResponseEntity.badRequest().body("帳號或密碼錯誤!");
        }

        // 登入成功，產生 JWT token
        String token = jwtUtil.generateToken(member.getEmail());

        //存入redis,設定1天過期
        redisService.save("member:" + member.getEmail(), member, 86400);

        // 不回傳密碼給前端
        member.setPassword(null);
        // 回傳 token 給前端
        return ResponseEntity.ok().body(token);

    }

    // 註冊功能
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Member member){

        if (memberDao.existsByEmail(member.getEmail())) {
            return ResponseEntity.badRequest().body("Email 已被註冊!");
        }

        Member saved = memberService.register(member);
        return ResponseEntity.ok(saved);
    }

}
