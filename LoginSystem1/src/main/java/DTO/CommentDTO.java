package DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long blogPostId;
 private AuthorDTO commenter ; // ✅ Ensures the username is sent to the frontend
}
