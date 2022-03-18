package com.eshop.admin.user;

import com.eshop.common.entity.Role;
import com.eshop.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userNameP = new User("para@gmail.com", "pramod123", "Pramod", "Arachchige");
        userNameP.addRole(roleAdmin);

        User savedUser = repo.save(userNameP);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User userJohn = new User("john@gmail.com", "john123", "John", "Silva");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userJohn.addRole(roleEditor);
        userJohn.addRole(roleAssistant);

        User savedUser = repo.save(userJohn);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithThreeRoles() {
        User userRadev = new User("radev@gmail.com", "radev123", "Radev", "Devsahan");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        Role roleSalesperson = new Role(2);

        userRadev.addRole(roleEditor);
        userRadev.addRole(roleAssistant);
        userRadev.addRole(roleSalesperson);


        User savedUser = repo.save(userRadev);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        User userP = repo.findById(1).get();
        System.out.println(userP);
        assertThat(userP).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userP = repo.findById(4).get();
        userP.setEnabled(true);
        userP.setEmail("devsahan@gmail.com");

        repo.save(userP);
    }

    @Test
    public void testUpdateUserRoles() {
        User userJohn = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        userJohn.getRoles().remove(roleEditor);
        userJohn.addRole(roleSalesperson);

        repo.save(userJohn);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 3;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail() {
        String email = "devsahan@gmail.com";
        User user = repo.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id = 1;
        Long countById = repo.countById(id);

        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser() {
        Integer id = 1;
        repo.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser() {
        Integer id = 1;
        repo.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage() {
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "bruce";

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword ,pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(user -> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}
