package pe.upc.edu.notecodeapiplatform.iam.application.internal.commandservices;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import pe.upc.edu.notecodeapiplatform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.upc.edu.notecodeapiplatform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.upc.edu.notecodeapiplatform.iam.domain.model.aggregates.User;
import pe.upc.edu.notecodeapiplatform.iam.domain.model.commands.SignInCommand;
import pe.upc.edu.notecodeapiplatform.iam.domain.model.commands.SignUpCommand;
import pe.upc.edu.notecodeapiplatform.iam.domain.model.valueobjects.Roles;
import pe.upc.edu.notecodeapiplatform.iam.domain.services.UserCommandService;
import pe.upc.edu.notecodeapiplatform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.upc.edu.notecodeapiplatform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.upc.edu.notecodeapiplatform.shared.application.exceptions.InvalidValueException;
import pe.upc.edu.notecodeapiplatform.shared.application.exceptions.ResourceAlreadyException;
import pe.upc.edu.notecodeapiplatform.shared.application.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username()))
            throw new ResourceAlreadyException("User already exists");

        // Usar ArrayList mutable para poder hacer .add()
        var roles = new ArrayList<>(command.roles());

        if (roles.isEmpty()) {
            var defaultRole = roleRepository.findByName(Roles.ROLE_USER);
            defaultRole.ifPresent(roles::add);
        }

        var resolvedRoles = roles.stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Role " + role.getName() + " does not exist")))
                .toList();

        var user = new User(command.username(), hashingService.encode(command.password()), resolvedRoles, command.email());
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username());
        if (user.isEmpty()) throw new ResourceNotFoundException("User not found");
        if (!hashingService.matches(command.password(), user.get().getPassword()))
            throw new InvalidValueException("Invalid password");
        var currentUser = user.get();
        var token = tokenService.generateToken(currentUser.getUsername());
        return Optional.of(ImmutablePair.of(currentUser, token));
    }
}