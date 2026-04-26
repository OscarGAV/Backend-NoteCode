package pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.transform;

import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.aggregates.CodeSnippet;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.resources.CodeSnippetResource;

public class CodeSnippetResourceFromEntityAssembler {
    public static CodeSnippetResource toResourceFromEntity(CodeSnippet entity) {
        return new CodeSnippetResource(
                entity.getId(),
                entity.getContent(),
                entity.getLanguage().language(),
                entity.getUserId(),
                entity.getIsPublic(),
                entity.getShareUrl()
        );
    }
}