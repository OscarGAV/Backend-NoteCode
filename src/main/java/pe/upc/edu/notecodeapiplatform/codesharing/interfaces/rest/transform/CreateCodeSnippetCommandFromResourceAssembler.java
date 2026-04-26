package pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.transform;

import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.commands.CreateCodeSnippetCommand;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.resources.CreateCodeSnippetResource;

public class CreateCodeSnippetCommandFromResourceAssembler {
    public static CreateCodeSnippetCommand toCommandFromResource(CreateCodeSnippetResource resource) {
        return new CreateCodeSnippetCommand(
                resource.content(),
                resource.userId(),
                resource.isPublic()
        );
    }
}