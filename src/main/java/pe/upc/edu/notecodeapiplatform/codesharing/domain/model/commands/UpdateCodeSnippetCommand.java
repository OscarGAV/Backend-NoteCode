package pe.upc.edu.notecodeapiplatform.codesharing.domain.model.commands;

public record UpdateCodeSnippetCommand(Long snippetId,
                                       String content,
                                       Boolean isPublic) {
}
