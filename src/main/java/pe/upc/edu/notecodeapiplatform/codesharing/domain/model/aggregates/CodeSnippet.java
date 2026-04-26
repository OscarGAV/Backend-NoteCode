package pe.upc.edu.notecodeapiplatform.codesharing.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.commands.CreateCodeSnippetCommand;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.commands.UpdateCodeSnippetCommand;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.valueobjects.*;
import pe.upc.edu.notecodeapiplatform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Entity
public class CodeSnippet extends AuditableAbstractAggregateRoot<CodeSnippet> {

    @Embedded
    private SnippetContent snippetContent;

    @Embedded
    private ShareableUrl shareableUrl;

    @Column
    private Long userId;

    @Column(nullable = false)
    private Boolean isPublic;

    public CodeSnippet() {
        // Default constructor for JPA
    }

    public CodeSnippet(CreateCodeSnippetCommand command) {
        this.snippetContent = new SnippetContent(command.content());
        this.shareableUrl = new ShareableUrl();
        this.userId = command.userId();
        this.isPublic = command.isPublic() != null ? command.isPublic() : true;
    }

    public void update(UpdateCodeSnippetCommand command) {
        if (command.content() != null && !command.content().isBlank()) {
            this.snippetContent = new SnippetContent(command.content());
        }
        if (command.isPublic() != null) {
            this.isPublic = command.isPublic();
        }
    }

    public String getContent() {
        return this.snippetContent.content();
    }

    public String getShareUrl() {
        return this.shareableUrl.urlCode();
    }
}