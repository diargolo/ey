package com.ey.desafiotecnico.tool;

import com.ey.desafiotecnico.dto.ResponseCreateUserDto;
import com.ey.desafiotecnico.dto.UserDto;
import com.ey.desafiotecnico.dto.UserTable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

@Component
public class Tool {

    public static final String SECRET_KEY = "SECRET_KEY";

    public String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String getDate() {
        Locale locale = new Locale("es", "CL");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        return dateFormat.format(new Date());
    }

    public boolean validateEmail(String email) {
        String regex = "^(.+)@(.+[cl])$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePW(String email) {
        String regex = "([A-Z]+[a-z]+[0-9]{2,})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String createJWT(String email) {

        // Expiracion Corta, solo con propositos de la prueba
        long limitMillis = 1000000000;
        long nowMillis = System.currentTimeMillis();
        SignatureAlgorithm sign = SignatureAlgorithm.HS256;
        Date now = new Date(nowMillis);

        byte[] bytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key key = new SecretKeySpec(bytes, sign.getJcaName());

        JwtBuilder jwt = Jwts.builder()
                .setId("01")
                .setIssuedAt(now)
                .setSubject(email)
                .setIssuer("ey")
                .signWith(sign, key)
                .setExpiration(new Date(nowMillis + limitMillis));

        return jwt.compact();
    }

    public Claims decodeJWT(String jwt) {

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public UserTable fill(UserDto userDto, ResponseCreateUserDto userResponse) {

        UserTable user = new UserTable();

        user.setId(userResponse.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setCreated(userResponse.getCreated());
        user.setModified(userResponse.getModified());
        user.setLastLogin(userResponse.getLastLogin());
        user.setToken(userResponse.getToken());
        user.setIsActive(userResponse.getIsActive());

        return user;
    }
}
