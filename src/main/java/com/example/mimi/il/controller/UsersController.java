package com.example.mimi.il.controller;////////package com.example.mimi.il.controller;

import com.example.mimi.il.Service.UsersServiceImpl;
import com.example.mimi.il.dto.*;
import com.example.mimi.il.entity.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersController {
    private final UsersServiceImpl service;
    private final Environment environment;

    @PostMapping("/login")
    public UserResponseDTO login(@RequestBody UserResponseDTO user,
                                 HttpServletResponse response) {
        UserResponseDTO login = service.login(user);
        if (login != null) {  // 인증이 성공
            String mytoken = Jwts.builder()
                    // 사용자정의 클레임
                    .addClaims(Map.of("email",login.getEmail(),"password",login.getPassword(),"userName",login.getUserName(),"role",login.getRole()))
                    .setExpiration(new Date(System.currentTimeMillis() +
                            Long.parseLong(environment.getProperty("jwt.token-valid-in-millisecond"))))
                    .signWith(SignatureAlgorithm.HS512, environment.getProperty("jwt.secret"))
                    .compact(); // 위의 정보들을 이용해서 토큰을 생성
            // response의 해더에 셋팅
            response.setHeader("Authorization", mytoken);
            response.setHeader("userId", String.valueOf(login.getUserId()));
            response.setHeader("role", String.valueOf(login.getRole()));

        }

        return login;
    }


    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody UserInsertReqDTO req) {
        service.insert(req);
        return new ResponseEntity<>(req, HttpStatus.OK);
    }

    @PostMapping("/findbyid")
    public ResponseEntity<User> findById(@RequestHeader("userId") String userId) {
        Optional<User> userOptional = service.findById(userId);
        if (userOptional.isPresent()) {
            // Optional이 값이 존재하면 그 값을 반환
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            // Optional이 비어있으면 404 Not Found를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<User> users = service.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/update")
    public void update(@RequestBody UserDTO dto ){
        service.update(dto);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody UserDeleteDTO dto) {
        service.delete(dto);
    }

    @PostMapping("/role")
    public void role(@RequestBody UserRoleDTO dto) {
        service.updateRole(dto);
    }


/*
    @PostMapping("/delete")
    public void delete(@RequestParam Long mgmNum) {
        EquipmentEntity equipment= equipmentService.findByMgmNum(mgmNum);
        equipmentService.delete(mgmNum);

    }*/


}
