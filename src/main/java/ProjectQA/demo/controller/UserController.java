package ProjectQA.demo.controller;

import ProjectQA.demo.model.User;
import ProjectQA.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5175")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            String msg = userService.signup(user);
            response.put("success", true);
            response.put("message", msg);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        boolean success = userService.login(user.getUsername(), user.getPassword());
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "Login successful" : "Invalid username or password");
        return response;
    }
}
