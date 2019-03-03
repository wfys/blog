package com.wfy.spring.boot.blog.service;

import com.wfy.spring.boot.blog.domain.Comment;

/**
 *  Comment 服务接口.
 * @author wfy
 *
 */
public interface CommentService {
	/**
	 * 根据id获取 Comment
	 * @param id
	 * @return
	 */
	Comment getCommentById(Long id);
	/**
	 * 删除评论
	 * @param id
	 * @return
	 */
	void removeComment(Long id);
}
