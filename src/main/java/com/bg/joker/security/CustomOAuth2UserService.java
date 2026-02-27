package com.bg.joker.security;

import com.bg.joker.entity.User;
import com.bg.joker.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Call parent to fetch user details from LINE
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 2. Extract profile details
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oauth2User.getName(); // For LINE, this maps to 'userId' via user-name-attribute
        String displayName = oauth2User.getAttribute("displayName");
        String email = oauth2User.getAttribute("email");

        if (email == null) {
            email = providerId + "@line.me"; // Fallback email
        }

        // 3. Find or Create User
        User user = null;

        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            user = byEmail.get();
            // Link if needed
            if (!provider.toUpperCase().equals(user.getProvider())) {
                user.setProvider(provider.toUpperCase());
                user.setProviderId(providerId);
                user = userRepository.save(user);
            }
        } else {
            Optional<User> byProviderId = userRepository.findByProviderId(providerId);
            if (byProviderId.isPresent()) {
                user = byProviderId.get();
            } else {
                // First time login - auto register
                user = User.builder()
                        .email(email)
                        .displayName(displayName != null ? displayName : "Line User")
                        .username(provider + "_" + providerId)
                        .provider(provider.toUpperCase())
                        .providerId(providerId)
                        .build();
                user = userRepository.save(user);
            }
        }

        // 4. Return our unified CustomUserDetails containing both User Entity and
        // OAuth2 attributes
        return new CustomUserDetails(user, oauth2User.getAttributes());
    }
}
