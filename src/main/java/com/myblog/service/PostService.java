package com.myblog.service;

import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto savePost(PostDto postDto);



    void deletPostById(long id);

    PostDto getPostById(long id);


    PostDto updatePost(long id, PostDto postDto);

    PostResponse gettAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
}
