package pe.upc.edu.notecodeapiplatform.codesharing.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.aggregates.CodeSnippet;
import pe.upc.edu.notecodeapiplatform.codesharing.domain.model.valueobjects.ShareableUrl;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, Long> {
    Optional<CodeSnippet> findByShareableUrl(ShareableUrl shareableUrl);
    List<CodeSnippet> findByUserId(Long userId);
}