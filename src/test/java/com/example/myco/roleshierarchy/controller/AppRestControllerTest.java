package com.example.myco.roleshierarchy.controller;

import com.example.myco.config.WebAppSecurityConfig;
import com.example.myco.controller.AppRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AppRestController.class)
public class AppRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppRestController appRestController;

    private PasswordEncoder passwordEncoder = WebAppSecurityConfig.passwordEncoder();

    @Test
    public void shouldAllowAccessForAnyUser_getUserEpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rest/user/ep"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("The user endpoint, accessible to all", result.getResponse().getContentAsString());
    }

    @Test
    public void shouldAllowForAppropriateAdminUser_adminEpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rest/admin/ep")
                .with(user("user1").password("password").roles("ADMIN")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("The user endpoint, accessible to Admin ONLY", response);
    }

    @Test
    public void shouldNotAllowAccessToUserWithIncorrectRole_adminEpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rest/admin/ep")
                .with(user("user2").password("password").roles("ENGINEER")))
                .andExpect(status().isForbidden())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("", response);
    }

    @Test
    public void shouldAllowAdminToAccessEngineerFunctions_enggEpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rest/engineer/ep")
                .with(user("user1").password("password").roles("ADMIN")))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("The user endpoint, accessible to engineer & above ONLY", response);
    }

    @Test
    public void shouldNotAllowLowerUserToAccessHigherUserApi_enggEp() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/rest/engineer/ep")
                .with(user("user4").password("password").roles("VIEWER")))
                .andExpect(status().isForbidden())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("", response);
    }
}
