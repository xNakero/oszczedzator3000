package pl.pz.oszczedzator3000.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pz.oszczedzator3000.exceptions.token.InvalidTokenException;
import pl.pz.oszczedzator3000.model.JwtSecret;
import pl.pz.oszczedzator3000.repository.JwtSecretRepository;

import java.util.Base64;

@Service
public class JwtSecretService {

    private final JwtSecretRepository jwtSecretRepository;

    @Autowired
    public JwtSecretService(JwtSecretRepository jwtSecretRepository) {
        this.jwtSecretRepository = jwtSecretRepository;
    }

    public void saveToRedis(JwtSecret jwtSecret) {
        jwtSecretRepository.save(jwtSecret);
    }

    public JwtSecret getFromRedis(String subject) {
        return jwtSecretRepository.findById(subject).orElseThrow(() -> new InvalidTokenException(""));
    }

    public boolean secretExists(String subject) {
        return jwtSecretRepository.findById(subject).isPresent();
    }

    public String getSubject(String header) {
        String[] chunks = header.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        return StringUtils.substringBetween(payload, "\"sub\":\"", "\"");
    }
}
