package org.one.corporatesocialmediaapp_backend.Service;


import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.Mapper.DTOMapper;
import org.one.corporatesocialmediaapp_backend.Repositories.PostRepository;
import org.one.corporatesocialmediaapp_backend.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    final PostRepository postRepository;
    final UserRepository userRepository;
    final DTOMapper dtoMapper;
}
