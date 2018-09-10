package ru.gazprom_neft.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This is how we handle our custom login screen. It has nothing to do with
 * Vaadin at all.
 */
@Controller
public class LoginController {

	@Autowired
	ServerProperties serverProperties;

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logged-out", required = false) String loggedOut, Model model) {
		// ModelAndView modelAndView = new ModelAndView();
		if (error != null) {
			model.addAttribute("error", true);
		}
		if (loggedOut != null) {
			model.addAttribute("loggedOut", true);
		}
		model.addAttribute("contextPath", serverProperties.getServlet().getContextPath() + "/login");
		// modelAndView.setViewName("login");
		return "login";
	}
}