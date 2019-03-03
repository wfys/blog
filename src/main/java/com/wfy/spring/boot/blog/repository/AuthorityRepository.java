package com.wfy.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wfy.spring.boot.blog.domain.Authority;
/**
 * Authority ²Ö¿â.
 * @author wfy
 *
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
