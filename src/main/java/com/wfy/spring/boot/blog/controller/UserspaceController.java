package com.wfy.spring.boot.blog.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wfy.spring.boot.blog.domain.Blog;
import com.wfy.spring.boot.blog.domain.Catalog;
import com.wfy.spring.boot.blog.domain.User;
import com.wfy.spring.boot.blog.domain.Vote;
import com.wfy.spring.boot.blog.vo.Response;
import com.wfy.spring.boot.blog.service.BlogService;
import com.wfy.spring.boot.blog.service.CatalogService;
import com.wfy.spring.boot.blog.service.UserService;
import com.wfy.spring.boot.blog.util.ConstraintViolationExceptionHandler;

/***
 * �û���ҳ������
 * @author wfy
 *
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CatalogService catalogService;
	
	private static AtomicLong counter = new AtomicLong();
	
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return "redirect:/u/" + username + "/blogs";
	}
	/**
	 * ��ȡ�������ý���
	 */
	@GetMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ModelAndView profile(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/profile", "userModel", model);
	}
 
	/**
	 * �����������
	 * @param user
	 * @param result
	 * @param redirect
	 * @return
	 */
	@PostMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") 
	public String saveProfile(@PathVariable("username") String username,User user) {
		User originalUser = userService.getUserById(user.getId());
		originalUser.setEmail(user.getEmail());
		originalUser.setName(user.getName());
		System.out.println(originalUser);
		
		// �ж������Ƿ����˱��
		String rawPassword = originalUser.getPassword();
		PasswordEncoder  encoder = new BCryptPasswordEncoder();
		String encodePasswd = encoder.encode(user.getPassword());
		boolean isMatch = encoder.matches(rawPassword, encodePasswd);
		if (!isMatch) {
			originalUser.setEncodePassword(user.getPassword());
		}
		
		userService.saveUser(originalUser);
		return "redirect:/u/" + username + "/profile";
	}
	
	/**
	 * ��ȡ�༭ͷ��Ľ���
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView avatar(@PathVariable("username") String username, Model model) {
		User  user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/avatar", "userModel", model);
	}
	
	
	/**
	 * ����ͷ��
	 * @param username
	 * @param model
	 * @return
	 */
	@PostMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestParam("file") MultipartFile file) {
		if(!file.isEmpty()) {
			// ��ȡ�ļ�����,������׺			
			String fileName = file.getOriginalFilename();
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			fileName=username+"."+suffix;
			// ��������·���£���·���Ǹù���Ŀ¼�µ�static�ļ��£�(ע�����ļ�������Ҫ�Լ�����)
			// ����static�µ�ԭ���ǣ���ŵ��Ǿ�̬�ļ���Դ����ͨ����������뱾�ط�������ַ�����ļ���ʱ�ǿ��Է��ʵ���
			String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/imgs/avatar/";
			try {

				File targetfile = new File(path);		
				if(!targetfile.exists()) {			
					targetfile.mkdirs();		
				}				
				//��������д��				
				FileOutputStream out = new FileOutputStream(path+fileName);	    
				out.write(file.getBytes());	  
				out.flush();	   
				out.close();
				System.out.println(path);
				User  user = (User)userDetailsService.loadUserByUsername(username);
				user.setAvater("/imgs/avatar/"+fileName);
				userService.saveUser(user);
				System.out.println(user.getAvater());
				return ResponseEntity.ok().body(new Response(true, "����ɹ�", user.getAvater()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ResponseEntity.ok().body(new Response(false, "�ļ�Ϊ��"));
			}

		}else{
			return ResponseEntity.ok().body(new Response(false, "�ļ�Ϊ��"));
		}

	}
	/**
	 * �����б�
	 */
	@GetMapping("/{username}/blogs")
	public ModelAndView listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="catalog",required=false ) Long catalogId,
			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
			@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			Model model) {
		
		User  user = (User)userDetailsService.loadUserByUsername(username);
		
		Page<Blog> page = null;
		System.out.println(catalogId);
		if (catalogId != null && catalogId > 0) { // �����ѯ
			Catalog catalog = catalogService.getCatalogById(catalogId);
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByCatalog(catalog, pageable);
			order = "";
		} else if (order.equals("hot")) { // ���Ȳ�ѯ
			Sort sort = new Sort(Direction.DESC,"readSize","commentSize","likeSize"); 
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByTitleLikeAndSort(user, keyword, pageable);
		}
		else if (order.equals("new")) { // ���²�ѯ
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByTitleLike(user, keyword, keyword, pageable);
		}
		boolean isBlogOwner = false;
		// �жϲ����û��Ƿ��ǲ��͵�������
				if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
						 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
					User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
					if (principal !=null && username.equals(principal.getUsername())) {
						isBlogOwner = true;
					} 
				}
		List<Blog> list = page.getContent();	// ��ǰ����ҳ�������б�
		model.addAttribute("order", order);
		model.addAttribute("catalogId", catalogId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("blogList", list);
		model.addAttribute("user", user);
		model.addAttribute("isBlogOwner", isBlogOwner);
		model.addAttribute("catalogs", null);
		model.addAttribute("page", page);
		return new ModelAndView(async==true?"/userspace/u :: #mainContainerRepleace":"/userspace/u","userModel", model);
	}
	
	/**
	 * ��ȡ����չʾ����
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/{id}")
	public ModelAndView getBlogById(@PathVariable("username") String username,@PathVariable("id") Long id, Model model) {
		// ÿ�ζ�ȡ���򵥵Ŀ�����Ϊ�Ķ�������1��
		blogService.readingIncrease(id);
		User principal = null;
		Blog blog = blogService.getBlogById(id);
		boolean isBlogOwner = false;
		
		// �жϲ����û��Ƿ��ǲ��͵�������
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && username.equals(principal.getUsername())) {
				isBlogOwner = true;
			} 
		}
		// �жϲ����û��ĵ������
				List<Vote> votes = blog.getVotes();
				Vote currentVote = null; // ��ǰ�û��ĵ������
				
				if (principal !=null) {
					for (Vote vote : votes) {
						if(vote.getUser().getUsername().equals(principal.getUsername()))
						    currentVote = vote;
						break;
					}
				}
		
		model.addAttribute("isBlogOwner", isBlogOwner);
		model.addAttribute("blogModel",blogService.getBlogById(id));
		model.addAttribute("user",principal);
		model.addAttribute("comments",null);
		model.addAttribute("currentVote",currentVote);
		
		return new ModelAndView("/userspace/blog","userModel", model);
	}
	
	
	/**
	 * ɾ������
	 * @param id
	 * @param model
	 * @return
	 */
	@DeleteMapping("/{username}/blogs/{id}")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,@PathVariable("id") Long id) {
		
		try {
			blogService.removeBlog(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs";
		return ResponseEntity.ok().body(new Response(true, "����ɹ�", redirectUrl));
	}
	
	/**
	 * ��ȡ�������͵Ľ���
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit")
	public ModelAndView createBlog(@PathVariable("username") String username,Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		
		model.addAttribute("blog", new Blog(null, null, null));
		model.addAttribute("catalogs", catalogs);
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * ��ȡ�༭���͵Ľ���
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit/{id}")
	public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		// ��ȡ�û������б�
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		
		model.addAttribute("blog", blogService.getBlogById(id));
		model.addAttribute("catalogs", catalogs);
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}
	
	/**
	 * ���沩��
	 * @param username
	 * @param blog
	 * @return
	 */
	@PostMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)") 
	public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
		// �� Catalog ���пմ���
		if (blog.getCatalog().getId() == null) {
				return ResponseEntity.ok().body(new Response(false,"δѡ�����"));
		}
		try {
			// �ж����޸Ļ�������
			if (blog.getId() != null) {
				Blog orignalBlog = blogService.getBlogById(blog.getId());
				orignalBlog.setTitle(blog.getTitle());
				orignalBlog.setContent(blog.getContent());
				orignalBlog.setSummary(blog.getSummary());
				orignalBlog.setCatalog(blog.getCatalog());
				orignalBlog.setTags(blog.getTags());
				blogService.saveBlog(orignalBlog);
			} else {
				User user = (User) userDetailsService.loadUserByUsername(username);
				blog.setUser(user);
				blogService.saveBlog(blog);
			}
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
		return ResponseEntity.ok().body(new Response(true, "����ɹ�", redirectUrl));
	}
	/*
	 * ���沩���е�ͼƬ
	 */
	@PostMapping("/{username}/blogs/photo")
	public ResponseEntity<Response> saveBlogPhoto(@PathVariable("username") String username, @RequestParam("file") MultipartFile file) {
		if(!file.isEmpty()) {
			// ��ȡ�ļ�����,������׺			
			String fileName = file.getOriginalFilename();
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			fileName=username+counter.incrementAndGet()+"."+suffix;
			// ��������·���£���·���Ǹù���Ŀ¼�µ�static�ļ��£�(ע�����ļ�������Ҫ�Լ�����)
			// ����static�µ�ԭ���ǣ���ŵ��Ǿ�̬�ļ���Դ����ͨ����������뱾�ط�������ַ�����ļ���ʱ�ǿ��Է��ʵ���
			String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/imgs/blog/";
			try {

				File targetfile = new File(path);		
				if(!targetfile.exists()) {			
					targetfile.mkdirs();		
				}				
				//��������д��				
				FileOutputStream out = new FileOutputStream(path+fileName);	    
				out.write(file.getBytes());	  
				out.flush();	   
				out.close();
				System.out.println(path);
				return ResponseEntity.ok().body(new Response(true, "����ɹ�","/imgs/blog/"+fileName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ResponseEntity.ok().body(new Response(false, "�ļ�Ϊ��"));
			}
		}else{
			return ResponseEntity.ok().body(new Response(false, "�ļ�Ϊ��"));
		}

	}
}
