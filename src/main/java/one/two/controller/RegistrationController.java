package one.two.controller;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import one.two.domain.User;
import one.two.domain.dto.CaptchaResponseDto;
import one.two.service.UserService;

@Controller
public class RegistrationController {
	private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${recaptcha.secret}")
	private String secret;
	
	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}
	
	@PostMapping("/registration")
	public String addUser(
			@RequestParam("password2") String password2,
			@RequestParam("g-recaptcha-response") String captchaResponse,
			@Valid User user, 
			BindingResult bindingResult, 
			Model model) {
		String url = String.format(CAPTCHA_URL, secret, captchaResponse);
		CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
		
		if(!response.isSucces()) {
			model.addAttribute("captchaError", "Fill captcha");
		}
		
		boolean isPassword2Empty = StringUtils.isEmpty(password2);
		if(isPassword2Empty) {
			model.addAttribute("password2Error", "Password confirmation");
		}
		if (user.getPassword() != null && !user.getPassword().equals(password2)) {
            model.addAttribute("passwordError", "Passwords are different!");
        }

        if (isPassword2Empty || bindingResult.hasErrors() || !response.isSucces()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "registration";
        }
		if(!userService.addUser(user)) {
			model.addAttribute("message", "User exists!");
			return "registration";
		}
		
		return "redirect:/login";
	}
	
	@GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "succes");
            model.addAttribute("message", "User successfully activated");
        } else {
        	model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }

}
