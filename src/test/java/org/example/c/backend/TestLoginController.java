package org.example.c.backend;

import org.example.c.backend.controller.LoginController;
import org.example.c.backend.dto.LoginDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

@SpringBootTest
public class TestLoginController {
    @Autowired
    private LoginController loginController;

    @Test
    public void testLogin() {
        String username = System.getenv("TEST_LOGIN_USERNAME");
        String password = System.getenv("TEST_LOGIN_PASSWORD");
        Assumptions.assumeTrue(username != null && password != null,
                "未设置测试登录凭据，跳过登录集成测试");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);
        System.out.println(loginController.login(loginDTO, new MockHttpSession()));
    }
}
