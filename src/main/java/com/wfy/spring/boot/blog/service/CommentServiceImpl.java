/**
 * 
 */
package com.wfy.spring.boot.blog.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wfy.spring.boot.blog.domain.Comment;
import com.wfy.spring.boot.blog.repository.CommentRepository;

/**
 * Comment ·þÎñ.
 * @author wfy
 *
 */
@Service
public class CommentServiceImpl implements CommentService {

	
	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	@Transactional
	public void removeComment(Long id) {
		commentRepository.deleteById(id);
	}
	@Override
	public Comment getCommentById(Long id) {
		return commentRepository.getOne(id);
	}

}
