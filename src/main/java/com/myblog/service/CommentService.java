package com.myblog.service;

import com.myblog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    public CommentDto createComment(long postId, CommentDto commentDto);

    public void deleteCommentById(long postId ,long commentId);

    public List<CommentDto> getCommentsByPostId(long posId);

    CommentDto updateComment(long commentId, CommentDto commentDto);
}
