package com.java6.nhom1.controller;

import com.java6.nhom1.model.Role;
import com.java6.nhom1.model.User;
import com.java6.nhom1.repository.UserRepository;
import com.java6.nhom1.rest.utils.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("account")
public class UserController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    @InitBinder({"signupUser", "confirmPassword"})
    public void init(WebDataBinder binder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("loginpage")
    public String loginPage(Model model){
        User user = (User) model.getAttribute("user");
        User signupUser = (User) model.getAttribute("signupUser");
        if(user == null)
            user = new User();
        if(signupUser == null)
            signupUser = new User();

        model.addAttribute("active", "");
        model.addAttribute("user", user);
        model.addAttribute("signupUser", signupUser);
        return "pages/sign-in-up";
    }

    @GetMapping("signup")
    public String singupPage(Model model){
        User user = (User) model.getAttribute("user");
        User signupUser = (User) model.getAttribute("signupUser");
        if(user == null)
            user = new User();
        if(signupUser == null)
            signupUser = new User();
        model.addAttribute("active", "active");
        model.addAttribute("user", user);
        model.addAttribute("signupUser", signupUser);
        return "pages/sign-in-up";
    }

    @PostMapping("signup")
    public String signUp(@Validated(User.Basic.class) @ModelAttribute("signupUser") User user,
                         BindingResult result,
                         RedirectAttributes params,
                         @RequestParam("confirmPassword")Optional<String> cfp)
    {
        userRepo.findByEmailEquals(user.getEmail())
                .ifPresent(u ->
                        result.rejectValue("email",
                                       "dup.user.email",
                                   "Email đã tồn tại"));
        cfp.ifPresentOrElse(
                s -> {
                    if(!s.equals(user.getPassword()))
                        result.rejectValue("password", "mismatch.user.password", "Xác nhận mật khẩu không khớp");
                },
                () -> result.rejectValue("password", "emptyConfirm.user.password", "Vui lòng nhập xác nhận mật khẩu")
        );
        params.addFlashAttribute("org.springframework.validation.BindingResult.signupUser", result);
        params.addFlashAttribute("signupUser", user);
        if(result.hasErrors()){
            params.addFlashAttribute("signupError", "Thông tin đăng ký lỗi");
            return "redirect:/account/signup";
        } else {
            params.addFlashAttribute("signupMsg", "Đăng ký thành công");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(new Role());
            user.getRole().setRoleId("ROLE_GUEST");
            userRepo.save(user);
            return "redirect:/account/loginpage";
        }
    }
}
