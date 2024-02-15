package com.example.unittest;

import com.example.unittest.controllers.UserController;
import com.example.unittest.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class ControllerUnitTestApplicationTests {
    @Autowired
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoad() {
        assertThat(userController).isNotNull();
    }

    private User createUser() throws Exception {
        User user = new User();
        user.setSurname("Pi");
        user.setFirstName("Pietro");
        user.setLastName("Strano");

        return createUser(user);
    }

    private User createUser(User user) throws Exception {
        MvcResult result = createUserRequest(user);
        return objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
    }

    private MvcResult createUserRequest() throws Exception {
        User user = new User();
        user.setSurname("Pi");
        user.setFirstName("Pietro");
        user.setLastName("Strano");

        String userJSON = objectMapper.writeValueAsString(user);

        return this.mockMvc.perform(post("/v1/create/one")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    private MvcResult createUserRequest(User user) throws Exception {
        if (user != null) {
            String userJSON = objectMapper.writeValueAsString(user);

            return this.mockMvc.perform(post("/v1/create/one")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJSON)).andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        } else return null;
    }

    private User getUserFromId(Long id) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/v1/get/one/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        try {
            String userJSON = result.getResponse().getContentAsString();
            return objectMapper.readValue(userJSON, User.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    void createUserTest() throws Exception {
        User userFromResponse = createUser();
        assertThat(userFromResponse.getId()).isNotNull();
    }

    @Test
    void getUserListTest() throws Exception {
        createUserRequest();
        MvcResult result = this.mockMvc.perform(get("/v1/get/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<User> usersFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        System.out.println("The users in database are: " + usersFromResponse.size());
        assertThat(usersFromResponse.size()).isNotZero();
    }

    @Test
    void getUserTest() throws Exception {
        User user = createUser();
        assertThat(user.getId()).isNotNull();

        User userFromResponse = getUserFromId(user.getId());
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
    }

    @Test
    void changeSurnameTest() throws Exception {
        User user = createUser();
        assertThat(user.getId()).isNotNull();

        String newSurname = "Lu";
        user.setSurname(newSurname);

        String userJSON = objectMapper.writeValueAsString(user);

        MvcResult result = this.mockMvc.perform(patch("/v1/change/surname/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        //Check user from PUT
        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
        assertThat(userFromResponse.getSurname()).isEqualTo(newSurname);

    }

    @Test
    void deleteOne() throws Exception {
        User user = createUser();
        assertThat(user.getId()).isNotNull();

        this.mockMvc.perform(delete("/v1/delete/one/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponseGet = getUserFromId(user.getId());
        assertThat(userFromResponseGet).isNull();
    }

}
