package com.myblog.controller;

import com.myblog.exception.ResourceNotFound;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/posts")
public class PostController {


    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto =postService.savePost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(ADMIN)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable long id){

        postService.deletPostById(id);

        return new ResponseEntity<>("Post is deleted",HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id){

        PostDto postDto = postService.getPostById(id);

        return  new ResponseEntity<>(postDto,HttpStatus.OK);
    }

    @PreAuthorize("hasRole(ADMIN)")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @RequestBody PostDto postDto,
            @PathVariable long id){
       PostDto dto=  postService.updatePost(id,postDto);
       return new ResponseEntity<>(dto,HttpStatus.OK);
    }



//   URL: http://localhost:9090/api/posts/?pageNo=0&pageSize=5&sortBy=id&sortDir=desc

    // we can write name or value it's fine
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(name="pageNo", required = false,defaultValue = "0") int pageNo,
            @RequestParam(name ="pageSize", required = false,defaultValue = "5")int pageSize,
            @RequestParam(name ="sortBy",required = false,defaultValue = "id") String sortBy,
            @RequestParam(name ="sortDir", required = false,defaultValue = "asc")String sortDir
    )

    {
        PostResponse response = postService.gettAllPosts(pageNo,pageSize,sortBy,sortDir);
        return response;

    }

}
