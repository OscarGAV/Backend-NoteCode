package pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.commands.DeleteCodeSnippetCommand;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.queries.GetCodeSnippetByIdQuery;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.queries.GetCodeSnippetByShareUrlQuery;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.queries.GetCodeSnippetsByUserIdQuery;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.services.CodeSnippetCommandService;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.services.CodeSnippetQueryService;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.resources.CodeSnippetResource;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.resources.CreateCodeSnippetResource;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.resources.UpdateCodeSnippetResource;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.transform.CodeSnippetResourceFromEntityAssembler;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.transform.CreateCodeSnippetCommandFromResourceAssembler;
import pe.upc.edu.notecodeapiplatform.codesharing.interfaces.rest.transform.UpdateCodeSnippetCommandFromResourceAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/code-snippets", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Code Snippets", description = "Code Snippet Management Endpoints")
public class CodeSnippetController {

    private final CodeSnippetCommandService codeSnippetCommandService;
    private final CodeSnippetQueryService codeSnippetQueryService;

    public CodeSnippetController(CodeSnippetCommandService codeSnippetCommandService,
                                 CodeSnippetQueryService codeSnippetQueryService) {
        this.codeSnippetCommandService = codeSnippetCommandService;
        this.codeSnippetQueryService = codeSnippetQueryService;
    }

    @Operation(summary = "Create a new code snippet")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CodeSnippetResource> createCodeSnippet(@Valid @RequestBody CreateCodeSnippetResource resource) {
        var createCommand = CreateCodeSnippetCommandFromResourceAssembler.toCommandFromResource(resource);
        var codeSnippet = codeSnippetCommandService.handleCreate(createCommand);
        if (codeSnippet.isEmpty()) return ResponseEntity.badRequest().build();
        var snippetResource = CodeSnippetResourceFromEntityAssembler.toResourceFromEntity(codeSnippet.get());
        return new ResponseEntity<>(snippetResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing code snippet")
    @PutMapping(value = "/{snippetId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CodeSnippetResource> updateCodeSnippet(
            @PathVariable Long snippetId,
            @Valid @RequestBody UpdateCodeSnippetResource resource) {
        var updateCommand = UpdateCodeSnippetCommandFromResourceAssembler.toCommandFromResource(snippetId, resource);
        var codeSnippet = codeSnippetCommandService.handleUpdate(updateCommand);
        if (codeSnippet.isEmpty()) return ResponseEntity.badRequest().build();
        var snippetResource = CodeSnippetResourceFromEntityAssembler.toResourceFromEntity(codeSnippet.get());
        return ResponseEntity.ok(snippetResource);
    }

    @Operation(summary = "Delete a code snippet")
    @DeleteMapping(value = "/{snippetId}")
    public ResponseEntity<Void> deleteCodeSnippet(@PathVariable Long snippetId) {
        var deleteCommand = new DeleteCodeSnippetCommand(snippetId);
        codeSnippetCommandService.handleDelete(deleteCommand);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get code snippet by ID")
    @GetMapping(value = "/{snippetId}")
    public ResponseEntity<CodeSnippetResource> getCodeSnippetById(@PathVariable Long snippetId) {
        var query = new GetCodeSnippetByIdQuery(snippetId);
        var codeSnippet = codeSnippetQueryService.handleGetById(query);
        if (codeSnippet.isEmpty()) return ResponseEntity.notFound().build();
        var snippetResource = CodeSnippetResourceFromEntityAssembler.toResourceFromEntity(codeSnippet.get());
        return ResponseEntity.ok(snippetResource);
    }

    @Operation(summary = "Get code snippet by share URL")
    @GetMapping(value = "/share/{urlCode}")
    public ResponseEntity<CodeSnippetResource> getCodeSnippetByShareUrl(@PathVariable String urlCode) {
        var query = new GetCodeSnippetByShareUrlQuery(urlCode);
        var codeSnippet = codeSnippetQueryService.handleGetByShareUrl(query);
        if (codeSnippet.isEmpty()) return ResponseEntity.notFound().build();
        var snippetResource = CodeSnippetResourceFromEntityAssembler.toResourceFromEntity(codeSnippet.get());
        return ResponseEntity.ok(snippetResource);
    }

    @Operation(summary = "Get all code snippets by user")
    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<List<CodeSnippetResource>> getCodeSnippetsByUserId(@PathVariable Long userId) {
        var query = new GetCodeSnippetsByUserIdQuery(userId);
        var codeSnippets = codeSnippetQueryService.handleGetByUserId(query);
        var snippetResources = codeSnippets.stream()
                .map(CodeSnippetResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(snippetResources);
    }
}