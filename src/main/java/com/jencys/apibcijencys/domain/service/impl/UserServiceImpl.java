package com.jencys.apibcijencys.domain.service.impl;

import com.jencys.apibcijencys.config.RegexProperties;
import com.jencys.apibcijencys.data.entity.Phone;
import com.jencys.apibcijencys.data.entity.User;
import com.jencys.apibcijencys.data.repository.PhoneRepository;
import com.jencys.apibcijencys.data.repository.UserRepository;
import com.jencys.apibcijencys.domain.service.UserService;
import com.jencys.apibcijencys.dto.UserRequestDTO;
import com.jencys.apibcijencys.dto.UserResponse;
import com.jencys.apibcijencys.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String PASSWORD_ERROR_MESSAGE = "Contrasenha invalida debe contener al menos: Un digito. Una letra minuscula. Una letra mayuscula. Un caracter especila(@#$%^&+=). Longitud entre 8 y 20 caracteres";
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final RegexProperties regexProperties;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    public UserResponse registerUser(@Valid UserRequestDTO userRequest) {
        if (validateMatch(userRequest.getPassword(), regexProperties.getPassword())) {
            throw new IllegalArgumentException(PASSWORD_ERROR_MESSAGE);
        }

        if (validateMatch(userRequest.getEmail(), regexProperties.getEmail())){
            throw new IllegalArgumentException("Porfavor ingrese un email valido. Ejemplo: aaaaaaa@dominio.cl");
        }

        Optional<User> userByEmail = this.userRepository.findByEmail(userRequest.getEmail());
        if (userByEmail.isPresent()){
            throw new IllegalArgumentException("El correo ya registrado");
        }

        User user = User.builder()
                .modified(new Date())
                .lastLogin(new Date())
                .token(jwtTokenUtil.generateToken(userRequest.getEmail()))
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        User userSaved = userRepository.save(user);

        List<Phone> phones = userRequest.getPhones()
                .stream()
                .map(phoneDTO -> Phone.builder()
                        .user(user)
                        .number(phoneDTO.getNumber())
                        .cityCode(phoneDTO.getCityCode())
                        .countryCode(phoneDTO.getCountryCode())
                        .build())
                .toList();

        phoneRepository.saveAll(phones);

        return UserResponse.builder()
                .id(userSaved.getId())
                .created(userSaved.getCreated())
                .modified(userSaved.getModified())
                .lastLogin(userSaved.getLastLogin())
                .token(userSaved.getToken())
                .isActive(userSaved.getIsActive())
                .build();
    }

    private boolean validateMatch(String toEvaluate, String patternString){
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(toEvaluate);
        return !matcher.matches();
    }
}
