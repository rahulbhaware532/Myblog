package com.myblog.service.impl;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFound;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.repository.PostRepository;
import com.myblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }


    private ModelMapper modelMapper;
    private PostRepository postRepository;

    @Override
    public PostDto savePost(PostDto postDto) {
       Post post = mapToEntity(postDto);
       Post savePost = postRepository.save(post);
        System.out.println(" Data is stored "+post.getContent());
       PostDto dto =mapToDto(savePost);

        return dto;
    }

    @Override
    public void deletPostById(long id) {
        postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound("Post Not Found with Id :" + id)
        );
        postRepository.deleteById(id);
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post Is Not Found With Id: " + id)
        );
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post Is Not Found With Id: " + id)
        );

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post savePost = postRepository.save(post);
        PostDto dto = mapToDto(savePost);
        return dto;
    }


//    Applied pagination and sorting
    @Override
    public PostResponse gettAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        //http://localhost:9090/api/posts/?pageNo=0&pageSize=5&sortBy=id&sortDir=desc
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

//       URL:   http://localhost:9090/api/posts/?pageNo=0&pageSize=3
//       Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy)); //before asc &Desc

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> pagePostObjects = postRepository.findAll(pageable);
        List<Post> posts = pagePostObjects.getContent();

        List<PostDto> dtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());


        PostResponse response = new PostResponse();
        response.setDto(dtos);
        response.setPageNo(pagePostObjects.getNumber());
        response.setTotalPages(pagePostObjects.getTotalPages());
        response.setLastPage(pagePostObjects.isLast());
        response.setPageSize(pagePostObjects.getSize());

        return response;
    }


    Post mapToEntity(PostDto postDto){
//        Post post= new Post();
        Post post = modelMapper.map(postDto, Post.class);
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return  post;
    }
    PostDto mapToDto(Post post){

        //reducing no of line of code
        PostDto postDto = modelMapper.map(post, PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());

        return  postDto;
    }
}
