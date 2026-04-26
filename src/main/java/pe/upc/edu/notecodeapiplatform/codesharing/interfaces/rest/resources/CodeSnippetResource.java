package pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.resources;

import java.util.Date;

public record CodeSnippetResource(
        Long snippetId,
        String content,
        Long userId,
        Boolean isPublic,
        String shareUrl,
        Date createdAt
) {
}