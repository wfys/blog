package com.wfy.spring.boot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wfy.spring.boot.blog.domain.Catalog;
import com.wfy.spring.boot.blog.domain.User;
import com.wfy.spring.boot.blog.service.CatalogService;
import com.wfy.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.wfy.spring.boot.blog.vo.CatalogVO;
import com.wfy.spring.boot.blog.vo.Response;

/**
 * ���� ������.
 * @author wfy
 *
 */
@Controller
@RequestMapping("/catalogs")
public class CatalogController {
	
	@Autowired
	private CatalogService catalogService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	/**
	 * ��ȡ�����б�
	 * @param blogId
	 * @param model
	 * @return
	 */
	@GetMapping
	public ModelAndView listComments(@RequestParam(value="username",required=true) String username, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);

		// �жϲ����û��Ƿ��Ƿ����������
		boolean isOwner = false;
		
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			} 
		} 
		
		model.addAttribute("isBlogOwner", isOwner);
		model.addAttribute("catalogs", catalogs);
		return new ModelAndView("/userspace/u :: #catalogRepleace","userModel", model);
	}
	/**
	 * �������
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	@PostMapping
	@PreAuthorize("authentication.name.equals(#catalogVO.username)")// ָ���û����ܲ�������
	public ResponseEntity<Response> create(@RequestBody CatalogVO catalogVO) {
		
		String username = catalogVO.getUsername();
		Catalog catalog = catalogVO.getCatalog();
		
		User user = (User)userDetailsService.loadUserByUsername(username);
		
		try {
			catalog.setUser(user);
			catalogService.saveCatalog(catalog);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "����ɹ�", null));
	}
	
	/**
	 * ɾ������
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("authentication.name.equals(#username)")  // ָ���û����ܲ�������
	public ResponseEntity<Response> delete(String username, @PathVariable("id") Long id) {
		try {
			catalogService.removeCatalog(id);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "����ɹ�", null));
	}
	
	/**
	 * ��ȡ����༭����
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit")
	public ModelAndView getCatalogEdit(Model model) {
		Catalog catalog = new Catalog(null, null);
		model.addAttribute("catalog",catalog);
		return new ModelAndView("/userspace/catalogedit","userModel", model);
	}
	/**
	 * ���� Id ��ȡ������Ϣ
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public ModelAndView getCatalogById(@PathVariable("id") Long id, Model model) {
		Catalog catalog = catalogService.getCatalogById(id);
		model.addAttribute("catalog",catalog);
		return new ModelAndView("/userspace/catalogedit","userModel", model);
	}
	
}
