package com.devteria.post.repository;

import com.devteria.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
//    List<Post> findAllByUserId(String userId);
    Page<Post> findAllByUserId(String userId, Pageable pageable);
}
