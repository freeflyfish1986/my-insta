package com.freeflyfish.MyInsta.repository;

import com.freeflyfish.MyInsta.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedDateDesc(Long postId);
    List<Comment> findByUserIdOrderByCreatedDateDesc(Long userId);
}