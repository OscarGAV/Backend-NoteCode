package pe.upc.edu.notecodeapiplatform.codesharing.domain.model.commands;

public record CreateCodeSnippetCommand(
        String content,
        Long userId,
        Boolean isPublic
) {
}