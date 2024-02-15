package com.example.unittest;

import com.example.unittest.entities.User;
import com.example.unittest.repositories.UserRepository;
import com.example.unittest.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
public class ServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User createUser(){
        User user = new User();
        user.setSurname("Pi");
        user.setFirstName("Pietro");
        user.setLastName("Strano");
        return userRepository.saveAndFlush(user);
    }

    @Test
    void addNewUserTest()
    {
        User user = createUser();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
    }

    @Test
    void getUserListServiceTest()
    {
        this.createUser();
        List<User> userList = userService.getAll();
        assertThat(userList.isEmpty()).isEqualTo(false);
    }

    @Test
    void getUserByIDTest()
    {
        User user = this.createUser();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        User userFromService = userService.getOne(user.getId());
        assertThat(userFromService).isNotNull();
        assertThat(userFromService.getId()).isNotNull();
    }

    @Test
    void updateUserByIDTest()
    {
        User user = this.createUser();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        User userFromService = userService.getOne(user.getId());
        assertThat(userFromService).isNotNull();
        assertThat(userFromService.getId()).isNotNull();

        User userBodyRequest = this.createUser();
        userBodyRequest.setId(userFromService.getId());
        String newSurname = "Pietrino";
        userBodyRequest.setSurname(newSurname);

        User userFromUpdate = userService.changeSurname(userFromService.getId(), userBodyRequest);
        assertThat(userFromUpdate).isNotNull();
        assertThat(userFromUpdate.getId()).isNotNull();
        assertThat(userFromUpdate.getId()).isEqualTo(userBodyRequest.getId());
        assertThat(userFromUpdate.getSurname()).isEqualTo(userBodyRequest.getSurname());
        assertThat(userFromUpdate.getFirstName()).isEqualTo(userBodyRequest.getFirstName());
        assertThat(userFromUpdate.getLastName()).isEqualTo(userBodyRequest.getLastName());
        assertThat(userFromUpdate.getSurname()).isEqualTo(newSurname);
    }

    @Test
    void deleteUserById()
    {
        User user = this.createUser();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        User userFromService = userService.getOne(user.getId());
        assertThat(userFromService).isNotNull();
        assertThat(userFromService.getId()).isNotNull();

        userService.deleteOne(userFromService.getId());

        User userAfterDelete = userService.getOne(user.getId());
        assertThat(userAfterDelete).isNull();
    }

}