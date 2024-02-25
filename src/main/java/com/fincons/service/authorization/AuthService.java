package com.fincons.service.authorization;

import com.fincons.dto.AbilityDto;
import com.fincons.dto.UserDto;
import com.fincons.entity.Ability;
import com.fincons.entity.Role;
import com.fincons.entity.User;
import com.fincons.exception.UserDataException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.jwt.LoginDto;
import com.fincons.mapper.UserAndRoleMapper;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.RoleRepository;
import com.fincons.repository.UserRepository;
import com.fincons.utility.EmailValidator;
import com.fincons.utility.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class AuthService implements IAuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);


    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserAndRoleMapper userAndRoleMapper;


    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Value("${admin.password}")
    private String passwordAdmin;

    @Value("${tutor.password}")
    private String passwordTutor;

    @Autowired
    private AbilityRepository abilityRepository;

    @Override
    public String registerStudent(UserDto userDto) throws UserDataException {

        String emailDto = userDto.getEmail().toLowerCase().replace(" ", "");
        if (userRepository.existsByEmail(emailDto) || emailDto.isEmpty() || !EmailValidator.isValidEmail(emailDto)) {
            throw new UserDataException(UserDataException.emailInvalidOrExist());
        }
        if (!PasswordValidator.isValidPassword(userDto.getPassword())) {
            throw new UserDataException(UserDataException.passwordDoesNotRespectRegexException());
        }
        if (userDto.getAbilities() == null || userDto.getAbilities().isEmpty()) {
            throw new UserDataException(UserDataException.userMustHaveAbilities());
        }

        User userToSave = userAndRoleMapper.dtoToUser(userDto);
        userToSave.setFirstName(userDto.getFirstName());
        userToSave.setLastName(userDto.getLastName());
        userToSave.setEmail(emailDto);
        userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userToSave.setBirthDate(userDto.getBirthDate());

        List<Ability> abilitiesForNewUser = new ArrayList<>();

        // Associa le abilità all'utente
        userToSave.setAbilities(assignAbilitties(userDto, abilitiesForNewUser));


        Role role = roleToAssign("ROLE_STUDENT");
        userToSave.setRoles(Set.of(role));


       User userSaved = userRepository.save(userToSave);
        if(userRepository.findByEmail(userSaved.getEmail()) == null){
            throw new UserDataException(UserDataException.somethingGoesWrong());
        }
        LOG.info("Student registered: " + userSaved.getEmail());
        return "Student registered successfully";
    }

    private List<Ability> assignAbilitties(UserDto userDto, List<Ability> abilitiesForNewUser) {
        // Per ogni abilità nel DTO dell'utente
        for (AbilityDto abilityDto : userDto.getAbilities()) {

            // Controlla se l'abilità esiste già nel repository
            Ability existingAbility = abilityRepository.findByName(abilityDto.getName());

            if (existingAbility != null) {
                // Se l'abilità esiste già, associa quella esistente all'utente
                abilitiesForNewUser.add(existingAbility);

            } else {
                // Se l'abilità non esiste, crea una nuova abilità nel repository e associa all'utente
                Ability newAbility = new Ability();
                newAbility.setName(abilityDto.getName()) ;
                abilityRepository.save(newAbility);
                abilitiesForNewUser.add(newAbility);
            }
        }
        return abilitiesForNewUser;
    }


    @Override
    public String registerTutor(UserDto userDto) throws UserDataException {
        String emailDto = userDto.getEmail().toLowerCase().replace(" ", "");
        if (
                userRepository
                        .existsByEmail(emailDto) ||
                        emailDto.isEmpty() ||
                        !EmailValidator.isValidEmail(emailDto)
        ) {
            throw new UserDataException(UserDataException.emailInvalidOrExist());
        }
        if (!PasswordValidator.isValidPassword(userDto.getPassword())) {
            throw new UserDataException(UserDataException.passwordDoesNotRespectRegexException());
        }
        if(userDto.getAbilities().isEmpty()){
            throw new UserDataException(UserDataException.userMustHaveAbilities());
        }
        User userToSave = userAndRoleMapper.dtoToUser(userDto);
        userToSave.setFirstName(userDto.getFirstName());
        userToSave.setLastName(userDto.getLastName());
        userToSave.setEmail(userDto.getEmail().toLowerCase().replace(" ", ""));
        userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userToSave.setBirthDate(userDto.getBirthDate());//Controllo dell'età ?

        List<Ability> abilitiesForNewUser = new ArrayList<>();
        assignAbilitties(userDto, abilitiesForNewUser);

        userToSave.setAbilities(abilitiesForNewUser);
        Role role = roleToAssign("ROLE_TUTOR");
        userToSave.setRoles(Set.of(role));
        User userSaved = userRepository.save(userToSave);
        if (!userRepository.existsByEmail(userSaved.getEmail())) {
            throw new UserDataException("Something goes wrong!");
        }
        LOG.info("Tutor registered: " + userSaved.getEmail());
        return "Tutor registered successfully";
    }

    @Override
    public String registerAdmin(UserDto userDto) throws UserDataException {
        String emailDto = userDto.getEmail().toLowerCase().replace(" ", "");
        if (userRepository.existsByEmail(emailDto) || emailDto.isEmpty() || !EmailValidator.isValidEmail(emailDto)) {
            throw new UserDataException(UserDataException.emailInvalidOrExist());
        }
        if (!PasswordValidator.isValidPassword(userDto.getPassword())) {
            throw new UserDataException(UserDataException.passwordDoesNotRespectRegexException());
        }
        User userToSave = userAndRoleMapper.dtoToUser(userDto);
        userToSave.setFirstName(userDto.getFirstName());
        userToSave.setLastName(userDto.getLastName());
        userToSave.setEmail(userDto.getEmail().toLowerCase().replace(" ", ""));
        userToSave.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleToAssign("ROLE_ADMIN");

        userToSave.setRoles(Set.of(role));

        User userSaved = userRepository.save(userToSave);
        if (!userRepository.existsByEmail(userSaved.getEmail())) {
            throw new UserDataException("Something goes wrong!");
        }
        return "Admin registered successfully";
    }


    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }



    public Role roleToAssign(String nomeRuolo) {
        Role role = roleRepository.findByName(nomeRuolo);
        if (role == null) {
            Role newRole = new Role();
            newRole.setName(nomeRuolo);
            role = roleRepository.save(newRole);
        }
        return role;
    }



}
