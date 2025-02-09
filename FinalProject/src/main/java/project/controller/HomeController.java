package project.controller;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
	@GetMapping({"", "/"})
	public String home () {
	return "index";
	}
	@GetMapping("/contact")
	public String contact() {
	return "contact";
	}
	@GetMapping("/about")
	public String about() {
	return "contact";
	}
}
