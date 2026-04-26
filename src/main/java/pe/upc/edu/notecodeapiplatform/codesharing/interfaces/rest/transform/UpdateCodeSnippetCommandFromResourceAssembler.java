package pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.transform;

import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.commands.UpdateCodeSnippetCommand;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.resources.UpdateCodeSnippetResource;

public class UpdateCodeSnippetCommandFromResourceAssembler {
    public static UpdateCodeSnippetCommand toCommandFromResource(Long snippetId, UpdateCodeSnippetResource resource) {
        return new UpdateCodeSnippetCommand(
                snippetId,
                resource.content(),
                resource.isPublic()
        );
    }
}