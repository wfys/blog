package com.wfy.spring.boot.blog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.wfy.spring.boot.blog.domain.Authority;
import com.wfy.spring.boot.blog.domain.User;
import com.wfy.spring.boot.blog.service.AuthorityService;
import com.wfy.spring.boot.blog.service.UserService;

/***
 * ��ҳ������
 * @author wfy
 *
 */
@Controller
public class MainController {
	
	private static final Long ROLE_USER_AUTHORITY_ID = 2L;
	
	@Autowired
	private UserService userService;
	@Autowired
	private AuthorityService  authorityService;
	
	@GetMapping("/")
	public String root() {
		return "redirect:/index";
	}
	
	@GetMapping("/index")
	public String index() {
		return "redirect:/blogs";
	}

	/**
	 * ��ȡ��¼����
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		model.addAttribute("errorMsg", "��½ʧ�ܣ��˺Ż����������");
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	/**
	 * ע���û�
	 * @param user
	 * @param result
	 * @param redirect
	 * @return
	 */
	@PostMapping("/register")
	public String registerUser(User user) {
		List<Authority> authorities = new ArrayList<>();
		authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
		user.setAuthorities(authorities);
		userService.saveUser(user);
		return "redirect:/login";
	}
	
	@GetMapping("/search")
	public String search() {
		return "search";
	}
}
