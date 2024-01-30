package com.myblog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private long id;

    @NotEmpty
    @Size(min=10, message = "Post Description should be min 10 character")
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private String content;

}
