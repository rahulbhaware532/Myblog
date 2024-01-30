package com.myblog.controller;

import com.myblog.payload.CommentDto;
import com.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

//    http://localhost:9090/api/comments?postId=1
    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @RequestParam long postId,
            @RequestBody CommentDto commentDto
    ) {
        CommentDto dto = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    //    http://localhost:9090/api/comments?postId=1?commnetId=1

    @DeleteMapping
    public ResponseEntity<String> deleteCommentById(
            @RequestParam long commentId,
            @RequestParam long postId
    ){

        return new ResponseEntity<>("Comment is Deleted:",HttpStatus.OK);
    }



//    http://localhost:9090/api/comments?postId=1
    @GetMapping
    public List<CommentDto>   getCommentByPostId(
            @RequestParam long postId){
        List<CommentDto> commentDtos = commentService.getCommentsByPostId(postId);
        return commentDtos;
    }



//    http://localhost:9090/api/comments?commentId=1
    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestParam long commentId,@RequestBody CommentDto commentDto){

       CommentDto dtos =  commentService.updateComment(commentId,commentDto);

       return  new ResponseEntity<>(dtos,HttpStatus.OK);
    }

}
