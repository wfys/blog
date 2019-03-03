package com.wfy.spring.boot.blog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wfy.spring.boot.blog.domain.Blog;
import com.wfy.spring.boot.blog.domain.Comment;
import com.wfy.spring.boot.blog.domain.User;
import com.wfy.spring.boot.blog.service.BlogService;
import com.wfy.spring.boot.blog.service.CommentService;
import com.wfy.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.wfy.spring.boot.blog.vo.Response;

/**
 * ���ۿ�����
 * @author wfy
 *
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CommentService commentService;
	
	/**
	 * ��ȡ�����б�
	 * @param blogId
	 * @param model
	 * @return
	 */
	@GetMapping
	public ModelAndView listComments(@RequestParam(value="blogId",required=true) Long blogId, Model model) {
		Blog blog = blogService.getBlogById(blogId);
		List<Comment> comments = blog.getComments();
		
		// �жϲ����û��Ƿ������۵�������
		String commentOwner = "";
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null) {
				commentOwner = principal.getUsername();
			} 
		}
		
		model.addAttribute("commentOwner", commentOwner);
		model.addAttribute("comments", comments);
		return new ModelAndView("/userspace/blog :: #mainContainerRepleace","userModel", model);
	}
	/**
	 * ��������
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // ָ����ɫȨ�޲��ܲ�������
	public ResponseEntity<Response> createComment(Long blogId, String commentContent) {
 
		try {
			blogService.createComment(blogId, commentContent);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "�����ɹ�", null));
	}
	
	/**
	 * ɾ������
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Response> deleteBlog(@PathVariable("id") Long id, Long blogId) {
		
		boolean isOwner = false;
		User user = commentService.getCommentById(id).getUser();
		
		// �жϲ����û��Ƿ��ǲ��͵�������
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			} 
		} 
		
		if (!isOwner) {
			return ResponseEntity.ok().body(new Response(false, "û�в���Ȩ��"));
		}
		
		try {
			blogService.removeComment(blogId, id);
			commentService.removeComment(id);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "�����ɹ�", null));
	}
}