package com.github.hasanmirzae.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hasanmirzae.demo.controllers.UserController;
import com.github.hasanmirzae.demo.exceptions.UserNotFoundException;
import com.github.hasanmirzae.demo.models.Profile;
import com.github.hasanmirzae.demo.models.User;
import com.github.hasanmirzae.demo.repository.UserRepository;
import com.github.hasanmirzae.demo.services.UserService;
import com.sun.deploy.security.ruleset.ExceptionRule;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration()
@SpringBootTest
public class ApplicationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Before public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }



    @Test public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());

    }


    @Test
    public void testCreateUser() throws Exception {
        MvcResult result = mockMvc.perform(post("/users")
                .content(createTestUser())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        User user = jsonToUser(result.getResponse().getContentAsString());
        assertNotNull(user);
        assertNotNull(user.getId());
    }


    @Test
    public void testFullUpdateUser() throws Exception {
        // create a user instance
        User origin = createTestUserObject();
        // save it into repo
        userRepository.save(origin);
        // clone the user instance
        User updatingUser = jsonToUser(userToJson(origin));
        // update one of the field
        updatingUser.setName("John");
        // perform update by PUT request
        MvcResult result = mockMvc.perform(put("/users")
                .content(userToJson(updatingUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
                .andReturn();
        // get updated user
        User updatedUser = jsonToUser(result.getResponse().getContentAsString());
        // assert
        assertNotNull(updatedUser);
        assertEquals("John",updatedUser.getName());
        assertEquals(origin.getId(),updatedUser.getId());
        assertEquals(origin.getLastName(),updatedUser.getLastName());

    }


    @Test
    public void testPartialUpdateUser() throws Exception {
        // create a user instance
        User origin = createTestUserObject();
        origin.setId(100);
        // save it into repo
        userRepository.save(origin);
        // create a new instance
        User updatingUser = new User();
        updatingUser.setId(100);
        updatingUser.setName("John");
        updatingUser.setLastName(null);
        // set field to be updated
        updatingUser.setUpdatingFields(Arrays.asList("name","lastName"));
        // perform update by PUT request
        MvcResult result = mockMvc.perform(patch("/users")
                .content(userToJson(updatingUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                                  .andExpect(status().isOk())
                                  .andReturn();
        // get updated user
        User updatedUser = jsonToUser(result.getResponse().getContentAsString());
        // assert partial update
        assertNotNull(updatedUser);
        assertEquals(origin.getId(),updatedUser.getId());
        assertNull(updatedUser.getLastName());
        assertNotNull(updatedUser.getProfile());
        assertEquals(origin.getId(),updatedUser.getId());
        assertEquals(origin.getLastName(),updatedUser.getLastName());

    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenIdNotDefined() throws Exception {
        thrown.expectCause(IsInstanceOf.instanceOf(UserNotFoundException.class));
        thrown.expect(UserNotFoundException.class);
        // create a user instance
        User origin = new User();
        // perform update by PUT request
            mockMvc.perform(patch("/users")
                    .content(userToJson(origin))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));
    }



    private User jsonToUser(String json) throws IOException {
        return new ObjectMapper().readValue(json,User.class);
    }
    private String userToJson(User user) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(user);
    }

    private User createTestUserObject(){
        User user = new User();
        user.setName("David");
        user.setLastName("Simpson");
        user.setProfile(new Profile("Somewhere","+1234567890","david@sample.com"));
        return user;
    }

    private String createTestUser() throws JsonProcessingException {
        return userToJson(createTestUserObject());
    }


}
