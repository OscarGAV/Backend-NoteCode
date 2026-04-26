package pe.upc.edu.notecodeapiplatform.codesharing.application.internal.queryservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.aggregates.CodeSnippet;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.queries.GetCodeSnippetByIdQuery;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.queries.GetCodeSnippetByShareUrlQuery;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.queries.GetCodeSnippetsByUserIdQuery;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.services.CodeSnippetQueryService;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.valueobjects.ShareableUrl;
import pe.upc.edu.notecodeapiplatform.codesharing.infrastructure.persistence.jpa.repositories.CodeSnippetRepository;
import pe.upc.edu.notecodeapiplatform.shared.application.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CodeSnippetQueryServiceImpl implements CodeSnippetQueryService {

    private final CodeSnippetRepository codeSnippetRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeSnippetQueryServiceImpl.class);

    public CodeSnippetQueryServiceImpl(CodeSnippetRepository codeSnippetRepository) {
        this.codeSnippetRepository = codeSnippetRepository;
    }

    @Override
    public Optional<CodeSnippet> handleGetById(GetCodeSnippetByIdQuery query) {
        LOGGER.info("Searching for code snippet with ID: {}", query.snippetId());
        return codeSnippetRepository.findById(query.snippetId())
                .map(snippet -> {
                    LOGGER.info("Code snippet found with ID {}", query.snippetId());
                    return snippet;
                })
                .or(() -> {
                    LOGGER.warn("No code snippet found with ID: {}", query.snippetId());
                    throw new ResourceNotFoundException("No code snippet found with ID: " + query.snippetId());
                });
    }

    @Override
    public Optional<CodeSnippet> handleGetByShareUrl(GetCodeSnippetByShareUrlQuery query) {
        LOGGER.info("Searching for code snippet with share URL: {}", query.urlCode());
        return codeSnippetRepository.findByShareableUrl(new ShareableUrl(query.urlCode()))
                .map(snippet -> {
                    LOGGER.info("Code snippet found with share URL: {}", query.urlCode());
                    return snippet;
                })
                .or(() -> {
                    LOGGER.warn("No code snippet found with share URL: {}", query.urlCode());
                    throw new ResourceNotFoundException("No code snippet found with share URL: " + query.urlCode());
                });
    }

    @Override
    public List<CodeSnippet> handleGetByUserId(GetCodeSnippetsByUserIdQuery query) {
        LOGGER.info("Searching for code snippets by user ID: {}", query.userId());
        var snippets = codeSnippetRepository.findByUserId(query.userId());
        LOGGER.info("Found {} snippets for user ID {}", snippets.size(), query.userId());
        return snippets;
    }
}