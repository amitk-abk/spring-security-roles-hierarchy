package com.example.myco.roleshierarchy.controller;

import com.example.myco.controller.AppRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.myco.common.Role.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AppRestController.class)
public class AppRestControllerTest {

    private final String openEndpoint = "/rest/user/ep";
    private final String adminEndPoint = "/rest/admin/ep";
    private final String enggEndPoint = "/rest/engineer/ep";
    private final String endPoint = "/getA";
    private final String authEndPoint = "/ws/ep";
    private final String expected = "";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppRestController appRestController;

    @Test
    public void shouldAllowAccessForAnyUser_getUserEpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(openEndpoint))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("The user endpoint, accessible to all, without login as well",
                result.getResponse().getContentAsString());
    }

    @Test
    public void shouldAllowForAppropriateAdminUser_adminEpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(adminEndPoint)
                .with(user("user1").password("password").roles(ADMIN.name())))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("The user endpoint, accessible to Admin ONLY", response);
    }

    @Test
    public void shouldNotAllowAccessToUserWithIncorrectRole_adminEpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(adminEndPoint)
                .with(user("user2").password("password").roles(ENGINEER.name())))
                .andExpect(status().isForbidden())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals(expected, response);
    }

    @Test
    public void shouldAllowAdminToAccessEngineerFunctions_enggEpTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(enggEndPoint)
                .with(user("user1").password("password").roles(ADMIN.name())))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("The user endpoint, accessible to engineer & above ONLY", response);
    }

    @Test
    public void shouldNotAllowLowerUserToAccessHigherUserApi_enggEp() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(enggEndPoint)
                .with(user("user4").password("password").roles(VIEWER.name())))
                .andExpect(status().isForbidden())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals(expected, response);
    }

    @Test
    public void shouldBeAccessibleToAllUsers() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(endPoint))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("GetA", response);
    }

    @Test
    public void shouldBeAccessibleToAnyLoggedInUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(authEndPoint)
                .with(user("user4").password("password").roles(VIEWER.name())))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals("The user endpoint, accessible to any authenticated user", response);
    }

    @Test
    public void shouldNotBeAvailableToUnauthorizedUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(authEndPoint))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertEquals(expected, response);
    }
}
