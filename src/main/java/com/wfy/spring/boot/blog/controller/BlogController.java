package com.wfy.spring.boot.blog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wfy.spring.boot.blog.domain.Blog;
import com.wfy.spring.boot.blog.service.BlogService;

import com.wfy.spring.boot.blog.vo.TagVO;

/***
 * blog������
 * @author wfy
 *
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {
	@Autowired
	private BlogService blogService;
	@GetMapping
	public ModelAndView listEsBlogs(
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
			@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			Model model) {
 
		Page<Blog> page = null;
		List<Blog> lists= null;
		boolean isEmpty = true; // ϵͳ��ʼ��ʱ��û�в�������
		try {
			if (order.equals("hot")) { // ���Ȳ�ѯ
				Sort sort = new Sort(Direction.DESC, "readSize", "commentSize", "likeSize");
				Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
				page = blogService.listBlogsByTitleLikes(keyword, pageable);
				lists = page.getContent(); // ��ǰ����ҳ�������б�
			} else if (order.equals("new")) { // ���²�ѯ
				Pageable pageable = new PageRequest(pageIndex, pageSize);
				page = blogService.listBlogsByTitle(keyword, keyword, pageable);
				lists = page.getContent(); // ��ǰ����ҳ�������б�
			}
		}
		catch (Exception e) {
			page=null;
			lists = new ArrayList<>(); // ��ǰ����ҳ�������б�
		}  
		
		isEmpty = false;
		System.out.println(page);
		List<Blog> list=new ArrayList<>();
		for(Blog blogs:lists){
			list.add(blogService.getBlogById(blogs.getId()));
		}
 
		model.addAttribute("order", order);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		// �״η���ҳ��ż���
		if (!async && !isEmpty) {
			
			Page<Blog> pages = null;
			
			Sort sorts = new Sort(Direction.DESC, "readSize", "commentSize", "likeSize");
			Pageable pageables = new PageRequest(0, 5, sorts);
			pages = blogService.listBlogsByTitleLikes("", pageables);
			
			List<Blog> hotest = pages.getContent();
			System.out.println(pages.getContent());
			model.addAttribute("hotest", hotest);
			
			;
			pageables = new PageRequest(0, 5);
			pages = blogService.listBlogsByTitle("", "", pageables);
			List<Blog> newest = pages.getContent();
			model.addAttribute("newest", newest);
			System.out.println(3);
			System.out.println(hotest);
			System.out.println(newest);
			/*List<TagVO> tags = esBlogService.listTop30Tags();
			model.addAttribute("tags", tags);
			List<User> users = esBlogService.listTop12Users();
			model.addAttribute("users", users);*/
		}
		return new ModelAndView(async==true? "/index :: #mainContainerRepleace":"/index","userModel", model);
	}
 
	
}
