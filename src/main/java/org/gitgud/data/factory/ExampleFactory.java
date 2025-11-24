package org.gitgud.data.factory;

import org.gitgud.core.model.Post;
import org.gitgud.core.model.PostFactory;
import org.gitgud.core.model.StaticPost;

import java.util.List;

@SuppressWarnings("unused")
public class ExampleFactory implements PostFactory {
    @Override
    public List<Post> generatePosts() {
        return List.of(
                new StaticPost("Hello World!", false),
                new StaticPost("The quick brown fox\njumped over the lazy dog.", false),
                new StaticPost("""
                        How happy is the blameless vestal’s lot!
                        The world forgetting, by the world forgot.
                        Eternal sunshine of the spotless mind!
                        Each pray’r accepted, and each wish resign’d
                        """, false)
        );
    }
}
