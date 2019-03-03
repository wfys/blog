package com.wfy.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wfy.spring.boot.blog.domain.Comment;

/**
 * Comment ²Ö¿â.
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{
 
}
