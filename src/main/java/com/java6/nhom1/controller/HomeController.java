package com.java6.nhom1.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.java6.nhom1.model.Role;
import com.java6.nhom1.model.User;
import com.java6.nhom1.rest.utils.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping()
public class HomeController {

    private String usersUrl = "http://localhost:8080/api/users";

    @Autowired
    private RestService restService;

    @GetMapping({"home", "/"})
    public String homePage(Model model,
                           Authentication authentication){
        User user = (User) model.getAttribute("user");
        if(user == null)
            user = new User();
        if(user.getRole() == null){
            Role baseRole = new Role();
            baseRole.setRoleId("ROLE_USER");
            user.setRole(baseRole);
        }
        if(((User)authentication.getPrincipal()).getRole().getAuthority().equals("ROLE_ADMIN"))
        {
            try {
                List<User> users = (List<User>) model.getAttribute("users");
                if(users == null) {
                    ResponseEntity<List<User>> response = restService.get(usersUrl, authentication, User.class);
                    users = response.getBody();
                }
                model.addAttribute("users", users);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        model.addAttribute("user", user);
        return "pages/main";
    }

    @GetMapping("404")
    public String notFound(){
        return "not-authorized";
    }
}
