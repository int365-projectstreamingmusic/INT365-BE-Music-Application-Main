package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.CommentPlaylistModel;

@Repository
public interface CommentPlaylistRepository extends JpaRepository<CommentPlaylistModel, Integer> {

	@Query(value = "SELECT c FROM CommentPlaylistModel c ORDER BY c.timestamp DESC")
	Page<CommentPlaylistModel> listAllComments(Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM user_playlist_comments WHERE playlist_id = :playlistId ORDER BY timestamp DESC")
	Page<CommentPlaylistModel> listAllComments(int playlistId, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM user_playlist_comments WHERE account_id = :userId ORDER BY timestamp DESC")
	Page<CommentPlaylistModel> listAllCommentsByUser(int userId, Pageable pageable);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE user_playlist_comments SET comment = :newComment WHERE playlist_comment_id = :id")
	void editComment(int id, String newComment);

}
