package com.guohenry.matchapp.controller;


import com.guohenry.matchapp.dao.MemberDao;
import com.guohenry.matchapp.dto.LoginRequest;
import com.guohenry.matchapp.model.Member;
import com.guohenry.matchapp.service.MemberService;
import com.guohenry.matchapp.service.RedisService;
import com.guohenry.matchapp.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@Tag(name = "會員 API", description = "會員登入 / 註冊")
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
        // 存入 Redis
        redisService.saveMember(member);
        // 清除密碼
        member.setPassword(null);
        // 回傳 token 給前端
        return ResponseEntity.ok(token);

    }

    @Operation(summary = "會員註冊", description = "提供 name, email, password 進行註冊")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Member member){

        if (memberDao.existsByEmail(member.getEmail())) {
            return ResponseEntity.badRequest().body("Email 已被註冊!");
        }

        Member saved = memberService.register(member);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrenMember(@RequestHeader("Authorization") String authHeader){
        // 確認 Authorization 開頭是 Bearer
        if(!authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(401).body("無效的授權");
        }
        // 擷取 JWT token
        String token = authHeader.substring(7);
        // 解析出 email（subject）
        String email = jwtUtil.extractEmail(token);
        // 從 Redis 拿取該 email 對應的會員資訊
        Member member = redisService.getMember(email);

        if(member == null){
            return ResponseEntity.status(401).body("找不到會員資料");
        }

        return ResponseEntity.ok(member);
    }

}
