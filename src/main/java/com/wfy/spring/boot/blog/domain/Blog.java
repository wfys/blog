package com.wfy.spring.boot.blog.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.github.rjeschke.txtmark.Processor;




/**
 * Blog ʵ��
 */
@Entity // ʵ��
public class Blog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id // ����
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ����������
	private Long id; // �û���Ψһ��ʶ
	
	@NotEmpty(message = "���ⲻ��Ϊ��")
	@Size(min=2, max=50)
	@Column(nullable = false, length = 50) // ӳ��Ϊ�ֶΣ�ֵ����Ϊ��
	private String title;
	
	@NotEmpty(message = "ժҪ����Ϊ��")
	@Size(min=2, max=300)
	@Column(nullable = false) // ӳ��Ϊ�ֶΣ�ֵ����Ϊ��
	private String summary;

	@Lob  // �����ӳ�� MySQL �� Long Text ����
	@Basic(fetch=FetchType.LAZY) // ������
	@NotEmpty(message = "���ݲ���Ϊ��")
	@Size(min=2)
	@Column(nullable = false) // ӳ��Ϊ�ֶΣ�ֵ����Ϊ��
	private String content;
	
	@Lob  // �����ӳ�� MySQL �� Long Text ����
	@Basic(fetch=FetchType.LAZY) // ������
	@NotEmpty(message = "���ݲ���Ϊ��")
	@Size(min=2)
	@Column(nullable = false) // ӳ��Ϊ�ֶΣ�ֵ����Ϊ��
	private String htmlContent; // �� md תΪ html


	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(nullable = false) // ӳ��Ϊ�ֶΣ�ֵ����Ϊ��
	@org.hibernate.annotations.CreationTimestamp  // �����ݿ��Զ�����ʱ��
	private Timestamp createTime;

	@Column(name="readSize")
	private Integer readSize = 0; // ���������Ķ���
	 
	@Column(name="commentSize")
	private Integer commentSize = 0;  // ������

	@Column(name="likeSize")
	private Integer likeSize = 0;  // ������
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
	private List<Comment> comments;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "blog_vote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"))
	private List<Vote> votes;
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="catalog_id")
	private Catalog catalog;
	
	@Column(name="tags", length = 100) 
	private String tags;  // ��ǩ
	
	protected Blog() {
		// TODO Auto-generated constructor stub
	}
	public Blog(String title, String summary,String content) {
		this.title = title;
		this.summary = summary;
		this.content = content;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.htmlContent = Processor.process(content);
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
 
	public Timestamp getCreateTime() {
		return createTime;
	}
	
	public String getHtmlContent() {
		return htmlContent;
	}
 
	public Integer getReadSize() {
		return readSize;
	}
	public void setReadSize(Integer readSize) {
		this.readSize = readSize;
	}
	public Integer getCommentSize() {
		return commentSize;
	}
	public void setCommentSize(Integer commentSize) {
		this.commentSize = commentSize;
	}
	public Integer getLikeSize() {
		return likeSize;
	}
	public void setLikeSize(Integer likeSize) {
		this.likeSize = likeSize;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
		this.commentSize = this.comments.size();
	}
 
	/**
	 * ��������
	 * @param comment
	 */
	public void addComment(Comment comment) {
		this.comments.add(comment);
		this.commentSize = this.comments.size();
	}
	/**
	 * ɾ������
	 * @param comment
	 */
	public void removeComment(Long commentId) {
		for (int index=0; index < this.comments.size(); index ++ ) {
			if (comments.get(index).getId() == commentId) {
				this.comments.remove(index);
				break;
			}
		}
		
		this.commentSize = this.comments.size();
	}

	/**
	 * ����
	 * @param vote
	 * @return
	 */
	public boolean addVote(Vote vote) {
		boolean isExist = false;
		// �ж��ظ�
		for (int index=0; index < this.votes.size(); index ++ ) {
			if (this.votes.get(index).getUser().getId() == vote.getUser().getId()) {
				isExist = true;
				break;
			}
		}
		
		if (!isExist) {
			this.votes.add(vote);
			this.likeSize = this.votes.size();
		}

		return isExist;
	}
	/**
	 * ȡ������
	 * @param voteId
	 */
	public void removeVote(Long voteId) {
		for (int index=0; index < this.votes.size(); index ++ ) {
			if (this.votes.get(index).getId() == voteId) {
				this.votes.remove(index);
				break;
			}
		}
		
		this.likeSize = this.votes.size();
	}
	public List<Vote> getVotes() {
		return votes;
	}
	public void setVotes(List<Vote> votes) {
		this.votes = votes;
		this.likeSize = this.votes.size();
	}
	public Catalog getCatalog() {
		return catalog;
	}
	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
 
}