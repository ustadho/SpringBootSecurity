package com.mitrais.bootcamp.sb;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserSecuredControllerWebMvcIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @WithMockUser(value = "spring")
    @Test
    public void givenAuthRequestOnPrivateService_ShouldSuccessWith200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
