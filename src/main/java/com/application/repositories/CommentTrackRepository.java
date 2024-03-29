package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.CommentTrackModel;

@Repository
public interface CommentTrackRepository extends JpaRepository<CommentTrackModel, Integer> {

	@Query(value = "SELECT c FROM CommentTrackModel c ORDER BY c.timestamp DESC")
	Page<CommentTrackModel> listAllComments(Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM user_track_comments WHERE track_id = :trackId ORDER BY timestamp DESC")
	Page<CommentTrackModel> listAllComments(int trackId, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM user_track_comments WHERE account_id = :userId ORDER BY timestamp DESC")
	Page<CommentTrackModel> listAllCommentsByUser(int userId, Pageable pageable);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE user_track_comments SET comment = :newComment WHERE track_comment_id = :id")
	void editComment(int id, String newComment);

}
