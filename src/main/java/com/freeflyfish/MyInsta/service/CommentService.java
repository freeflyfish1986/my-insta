package com.freeflyfish.MyInsta.service;

import com.freeflyfish.MyInsta.entity.Comment;
import com.freeflyfish.MyInsta.entity.Post;
import com.freeflyfish.MyInsta.entity.User;
import com.freeflyfish.MyInsta.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Создание нового комментария
     */
    public Comment createComment(User user, Post post, String message) {
        Comment comment = new Comment();
        comment.setMessage(message);
        comment.setUser(user);
        comment.setPost(post);

        return commentRepository.save(comment);
    }

    /**
     * Получение всех комментариев для поста
     * Сортировка по дате - новые первыми
     */
    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPostIdOrderByCreatedDateDesc(post.getId());
    }

    /**
     * Получение комментариев пользователя
     */
    public List<Comment> getCommentsByUser(User user) {
        return commentRepository.findByUserIdOrderByCreatedDateDesc(user.getId());
    }

    /**
     * Удаление комментария
     */
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}