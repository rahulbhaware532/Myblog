package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFound;
import com.myblog.payload.CommentDto;
import com.myblog.repository.CommentRepository;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found with id ;" + postId)
        );

        Comment comment = mapToEntity(commentDto);

        comment.setPost(post);

        Comment c = commentRepository.save(comment);

        return mapToDto(c);

    }

    @Override
    public void deleteCommentById(long postId, long commentId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post Not Found with id ;" + postId)
        );
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getCommentsByPostId(long posId){

        List<Comment> comments = commentRepository.findByPostId(posId);
        List<CommentDto> dtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

        return dtos;
    }

    @Override
//    public CommentDto updateComment(long commentId, CommentDto commentDto) {
////        Comment com = commentRepository.findById(commentId).get();
////        Post post = postRepository.findById(com.getPost().getId()).get();
////        Comment comment = mapToEntity(commentDto);
////        comment.setPost(post);
////        comment.setId(commentId);
////        Comment savedComment = commentRepository.save(comment);
////        CommentDto dto = mapToDto(savedComment);
////        return dto;

        public CommentDto updateComment(long commentId, CommentDto commentDto) {
            Optional<Comment> commentOptional = commentRepository.findById(commentId);

            if (commentOptional.isPresent()) {
                Comment com = commentOptional.get();
                Optional<Post> postOptional = postRepository.findById(com.getPost().getId());

                if (postOptional.isPresent()) {
                    Post post = postOptional.get();
                    Comment comment = mapToEntity(commentDto);
                    comment.setPost(post);
                    comment.setId(commentId);

                    Comment savedComment = commentRepository.save(comment);
                    CommentDto dto = mapToDto(savedComment);
                    return dto;
                } else {
                    throw new ResourceNotFound("Post not found for Comment with ID: " + commentId);
                }
            } else {
                throw new ResourceNotFound("Comment not found with ID: " + commentId);
            }
        }






    Comment mapToEntity(CommentDto dto){


//        Comment comment = new Comment();
//        comment.setName(dto.getName());
//        comment.setBody(dto.getBody());
//        comment.setEmail(dto.getEmail());
        Comment comment = modelMapper.map(dto, Comment.class);


        return comment;
    }

    CommentDto mapToDto(Comment comment){
//
//        CommentDto dto = new CommentDto();
//        dto.setName(comment.getName());
//        dto.setBody(comment.getBody());
//        dto.setEmail(comment.getEmail());

        CommentDto dto = modelMapper.map(comment, CommentDto.class);

        return dto;
    }
}
