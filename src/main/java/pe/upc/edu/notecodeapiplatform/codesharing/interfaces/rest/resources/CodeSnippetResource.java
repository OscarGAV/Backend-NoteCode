package pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.resources;

public record CodeSnippetResource(
        Long snippetId,
        String content,
        String language,
        Long userId,
        Boolean isPublic,
        String shareUrl
) {
}