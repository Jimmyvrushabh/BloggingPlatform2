package DTO;



import java.time.LocalDateTime;
import java.util.List;

import com.Spring.Model.BlogPost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class BlogpostDto {
    public BlogpostDto(BlogPost post) {
		// TODO Auto-generated constructor stub
	}
	private Long id;
    private String title;
    private String content;
    private AuthorDTO author;  // ✅ Accepts AuthorDTO instead of just authorName
    private List<CommentDTO> comments;
    private Long categoryId;
    private LocalDateTime createdAt;
}