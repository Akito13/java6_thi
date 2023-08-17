package com.java6.nhom1.controller;

import com.java6.nhom1.model.Role;
import com.java6.nhom1.model.User;
import com.java6.nhom1.repository.UserRepository;
import com.java6.nhom1.rest.utils.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private RestService restService;

    private String USER_URL = "http://localhost:8080/api/user";

    @PostMapping("user/create")
    public String createUser(@ModelAttribute("user") User user,
                             BindingResult result,
                             RedirectAttributes params,
                             Authentication auth){
        try {
            if(result.hasErrors()){
                params.addFlashAttribute("error", "Thông tin chưa hợp lệ");
            } else {
                ResponseEntity<List<User>> response = restService.post(USER_URL, user, auth, User.class);
                if(response.getStatusCode().isSameCodeAs(HttpStatus.CREATED)){
                    params.addFlashAttribute("message", "Thêm User mới thành công");
                } else {
                    params.addFlashAttribute("error", "Thêm thất bại");
                }
            }
        } catch (Exception ex) {
            params.addFlashAttribute("error", "Xảy ra lỗi khi thêm");
        }
        params.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
        params.addFlashAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("user/edit/{userId}")
    public String editUser(@PathVariable Optional<String> userId,
                           RedirectAttributes params,
                           Authentication auth){
        if(userId.isEmpty()){
            params.addFlashAttribute("error", "Vui lòng cung cấp User Id");
            return "redirect:/home";
        }
        try {
            ResponseEntity<List<User>> response = restService.get(USER_URL + "/" + userId.get(), auth, User.class);
            if(response.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)){
                params.addFlashAttribute("error", "Không tìm thấy User với id " + userId.get());
            } else {
                params.addFlashAttribute("user", response.getBody().get(0));
            }
        } catch (Exception ex){
            params.addFlashAttribute("error", "Xảy ra lỗi khi tải User");
        }
        return "redirect:/home";
    }

    @GetMapping("user/delete/{userId}")
    public String removeUser(@PathVariable Optional<String> userId,
                           RedirectAttributes params,
                           Authentication auth){
        if(userId.isEmpty()){
            params.addFlashAttribute("error", "Vui lòng cung cấp User Id");
            return "redirect:/home";
        }
        try {
            ResponseEntity<List<User>> response = restService.delete(USER_URL + "/" + userId.get(), auth, User.class);
            if(response.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)){
                params.addFlashAttribute("error", "Không tìm thấy User với id " + userId.get());
            } else {
                params.addFlashAttribute("user", null);
            }
        } catch (Exception ex){
            params.addFlashAttribute("error", "Xảy ra lỗi khi xóa User");
        }
        return "redirect:/home";
    }

    @PostMapping("user/delete")
    public String removeUserFromForm(@Validated @ModelAttribute("user") User user,
                                     BindingResult result,
                                     Authentication auth,
                                     RedirectAttributes params){
        try {
            if(result.hasErrors()){
                params.addFlashAttribute("error", "Thông tin chưa hợp lệ");
                params.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            }
            ResponseEntity<List<User>> response = restService.delete(USER_URL + "/" + user.getId(), auth, User.class);
            if(response.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)){
                params.addFlashAttribute("error", "Không tìm thấy User với id " + user.getId());
            } else {
                params.addFlashAttribute("user", null);
            }
        } catch (Exception ex){
            params.addFlashAttribute("error", "Xảy ra lỗi khi xóa User");
        }
        return "redirect:/home";
    }

    @PostMapping("user/update")
    public String updateUser(@Validated @ModelAttribute("user") User user,
                             BindingResult result,
                             Authentication auth,
                             RedirectAttributes params){
        try {
            if(result.hasErrors()){
                params.addFlashAttribute("error", "Thông tin chưa hợp lệ");
            } else {
                ResponseEntity<List<User>> response = restService.put(USER_URL, user, auth, User.class);
                if(response.getStatusCode().isSameCodeAs(HttpStatus.CREATED)){
                    params.addFlashAttribute("message", "Cập nhật User thành công");
                } else if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)){
                    params.addFlashAttribute("message", "Cập nhật User thành công");
                } else {
                    params.addFlashAttribute("error", "Cập nhật thất bại");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            params.addFlashAttribute("error", "Xảy ra lỗi khi cập nhật");
        }
        params.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
        params.addFlashAttribute("user", user);
        return "redirect:/home";
    }
    @PostMapping("user/reset")
    public String resetForm(){
        return "redirect:/home";
    }

    @GetMapping("user/search")
    public String searchUser(@RequestParam("id") Optional<String> id,
                             Authentication auth,
                             Model model){
        try {
            System.out.println("AdminController: " + id);
            ResponseEntity<List<User>> response = restService.get(USER_URL + "/search?id=" + id.orElse(""), auth, User.class);
            List<User> users = response.getBody();
            model.addAttribute("users", users);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        User user = new User();
        Role role = new Role();
        role.setRoleId("ROLE_USER");
        user.setRole(role);
        model.addAttribute("user", user);
        return "pages/main";
    }
}
