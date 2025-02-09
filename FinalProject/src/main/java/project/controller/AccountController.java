package project.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import project.dto.RegisterDto;
import project.models.AppUser;
import project.repositories.AppUserRepository;
import project.services.AppUserService;


@Controller
public class AccountController {

    @Autowired
    private AppUserRepository repo;

    @Autowired
    private AppUserService appUserService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute("registerDto", registerDto);
        model.addAttribute("success", false);
        return "register";
    }

    // Single PostMapping for registration
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute RegisterDto registerDto, BindingResult result, Model model) {
        // Validate password match
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            result.addError(new FieldError("registerDto", "confirmPassword", "Password and Confirm Password do not match"));
        }

        // Check if the email already exists in the system
        AppUser appUser = (repo.findByEmail(registerDto.getEmail());
        if (appUser != null) { 
            result.addError(new FieldError("registerDto", "email", "Email address is already used"));
        }


        // If there are validation errors, return to the register form
        if (result.hasErrors()) {
            return "register";
        }

        try {
            // Create new account
            var bCryptEncoder = new BCryptPasswordEncoder();
            AppUser newUser = new AppUser();
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPhone(registerDto.getPhone());
            newUser.setAddress(registerDto.getAddress());
            newUser.setRole("client");  // You can adjust this based on your application needs (e.g., admin, user, etc.)
            newUser.setCreatedAt(new Date());
            newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));

            // Save the user to the database
            repo.save(newUser);

            // Add a success message to the model
            model.addAttribute("success", true);
            model.addAttribute("registerDto", new RegisterDto());  // Reset form fields
        } 
        catch (Exception ex) {
            // If there's an error, display the error message
            result.addError(new FieldError("registerDto", "firstName", ex.getMessage()));
            return "register";
        }

        return "redirect:/login?success";
    }

	public AppUserService getAppUserService() {
		return appUserService;
	}

	public void setAppUserService(AppUserService appUserService) {
		this.appUserService = appUserService;
	}
}