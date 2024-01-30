package com.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name="title",nullable = false, unique = true )
    private String title;

    //Unique = true : don't want to give anybody to give same title

    @Column(name="description",nullable = false, unique = true )
    private String description;

    @Column(name="content",nullable = false, unique = true )
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) //Composition rule applied
    private  List<Comment> comments= new ArrayList<>();

}
