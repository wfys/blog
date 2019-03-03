package com.wfy.spring.boot.blog.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wfy.spring.boot.blog.domain.Blog;
import com.wfy.spring.boot.blog.domain.Catalog;
import com.wfy.spring.boot.blog.domain.Comment;
import com.wfy.spring.boot.blog.domain.User;
import com.wfy.spring.boot.blog.domain.Vote;
import com.wfy.spring.boot.blog.repository.BlogRepository;

/**
 * Blog 服务.
 */
@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;
	
	@Transactional
	@Override
	public Blog saveBlog(Blog blog) {
		Blog returnBlog = blogRepository.save(blog);
		return returnBlog;
	}

	
	@Transactional
	@Override
	public void removeBlog(Long id) {
		blogRepository.deleteById(id);
		
	}

	
	@Transactional
	@Override
	public Blog updateBlog(Blog blog) {
		return blogRepository.save(blog);
	}

	
	@Override
	public Blog getBlogById(Long id) {
		return blogRepository.getOne(id);
	}

	@Override
	public Page<Blog> listBlogsByTitleLike(User user, String title,String tags, Pageable pageable) {
		title = "%" + title + "%";
		Page<Blog> blogs = blogRepository.findByUserAndTitleLikeOrTagsLikeOrderByCreateTimeDesc(user,title, tags, pageable);
		return blogs;
	}

	@Override
	public Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable) {
		// 模糊查询
		title = "%" + title + "%";
		Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
		return blogs;
	}

	@Override
	public void readingIncrease(Long id) {
		Blog blog = blogRepository.getOne(id);
		blog.setReadSize(blog.getReadSize()+1);
		blogRepository.save(blog);
	}

	@Override
	public Blog createComment(Long blogId, String commentContent) {
		Blog originalBlog = blogRepository.getOne(blogId);
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		Comment comment = new Comment(user, commentContent);
		originalBlog.addComment(comment);
		return blogRepository.save(originalBlog);
	}

	@Override
	public void removeComment(Long blogId, Long commentId) {
		Blog originalBlog = blogRepository.getOne(blogId);
		originalBlog.removeComment(commentId);
		blogRepository.save(originalBlog);
	}

	@Override
	public Blog createVote(Long blogId) {
		Blog originalBlog = blogRepository.getOne(blogId);
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
		Vote vote = new Vote(user);
		boolean isExist = originalBlog.addVote(vote);
		if (isExist) {
			throw new IllegalArgumentException("该用户已经点过赞了");
		}
		return blogRepository.save(originalBlog);
	}

	@Override
	public void removeVote(Long blogId, Long voteId) {
		Blog originalBlog = blogRepository.getOne(blogId);
		originalBlog.removeVote(voteId);
		blogRepository.save(originalBlog);
	}

	@Override
	public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
		Page<Blog> blogs = blogRepository.findByCatalog(catalog, pageable);
		return blogs;
	}


	@Override
	public Page<Blog> listBlogsByTitle(String title, String tags, Pageable pageable) {
		// TODO Auto-generated method stub
		title = "%" + title + "%";
		Page<Blog> blogs = blogRepository.findByTitleLikeOrTagsLikeOrderByCreateTimeDesc(title, tags, pageable);
		return blogs;
	}


	@Override
	public Page<Blog> listBlogsByTitleLikes(String title, Pageable pageable) {
		// 模糊查询
				title = "%" + title + "%";
				Page<Blog> blogs = blogRepository.findByTitleLike(title, pageable);
				return blogs;
	}

	

}
