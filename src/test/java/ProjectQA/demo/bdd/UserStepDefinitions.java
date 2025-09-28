package ProjectQA.demo.bdd;

import ProjectQA.demo.model.User;
import ProjectQA.demo.service.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserStepDefinitions {

    @Autowired
    private UserService userService;

    private Exception signupException;
    private boolean loginResult;
    private User currentUser;

    // ---------------- GIVEN ----------------
    @Given("I have a new user with username {string} and email {string} and password {string}")
    public void i_have_a_new_user_with_username_and_email_and_password(String username, String email, String password) {
        currentUser = new User();
        currentUser.setUsername(username);
        currentUser.setEmail(email);
        currentUser.setPassword(password);
    }

    @Given("a user already exists with username {string} and email {string} and password {string}")
    public void a_user_already_exists(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        try {
            userService.signup(user);
        } catch (Exception e) {
            // Ignore: user might already exist
        }
    }

    // ---------------- WHEN ----------------
    @When("I attempt to signup")
    public void i_attempt_to_signup() {
        signupException = null;
        try {
            userService.signup(currentUser);
        } catch (Exception e) {
            signupException = e;
        }
    }

    @When("I attempt to login with username {string} and password {string}")
    public void i_attempt_to_login_with(String username, String password) {
        loginResult = userService.login(username, password);
    }

    @When("I try to signup with username {string} and email {string} and password {string}")
    public void i_try_to_signup_with(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        signupException = null;
        try {
            userService.signup(user);
        } catch (Exception e) {
            signupException = e;
        }
    }

    // ---------------- THEN ----------------


    @Then("the signup should fail with message {string}")
    public void the_signup_should_fail_with_message(String message) {
        assertNotNull(signupException, "Expected signup to fail but it succeeded");
        assertEquals(message, signupException.getMessage());
    }

    @Then("the login should be successful")
    public void the_login_should_be_successful() {
        assertTrue(loginResult, "Expected login to succeed but it failed");
    }

    @Then("the login should fail")
    public void the_login_should_fail() {
        assertFalse(loginResult, "Expected login to fail but it succeeded");
    }
}
