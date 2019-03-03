package com.wfy.spring.boot.blog.service;

import com.wfy.spring.boot.blog.domain.Vote;

/**
 *  Vote ����ӿ�.
 * @author wfy
 *
 */
public interface VoteService {
	/**
	 * ����id��ȡ Vote
	 * @param id
	 * @return
	 */
	Vote getVoteById(Long id);
	/**
	 * ɾ��Vote
	 * @param id
	 * @return
	 */
	void removeVote(Long id);
}
