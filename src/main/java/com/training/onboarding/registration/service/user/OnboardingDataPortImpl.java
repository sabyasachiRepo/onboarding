package com.training.onboarding.registration.service.user;

import com.training.onboarding.registration.bean.InstituteResponse;
import com.training.onboarding.registration.bean.Institutes;
import com.training.onboarding.registration.bean.User;
import com.training.onboarding.registration.ports.api.OnboardingDataPort;
import com.training.onboarding.registration.ports.spi.OnboardingPersistencePort;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@AllArgsConstructor
public class OnboardingDataPortImpl implements OnboardingDataPort {

    private OnboardingPersistencePort onboardingPersistencePort;

    private PasswordEncoder passwordEncoder;

    private JavaMailSender mailSender;



    public OnboardingDataPortImpl(OnboardingPersistencePort onboardingPersistencePort, PasswordEncoder passwordEncoder) {
        this.onboardingPersistencePort = onboardingPersistencePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Institutes getInstitutes() {
        Institutes institutes=new Institutes();
        institutes.setInstitutes(onboardingPersistencePort.getInstitutes());
        return institutes;
    }

    @Override
    public void registerNewUser(User user,HttpServletRequest httpServletRequest) {
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setIsEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        onboardingPersistencePort.saveUser(user);
        onboardingPersistencePort.addRoleToUser(user.getEmail(), "ROLE_STUDENT");

        try {
            // sendEmail(user,getSiteURL(httpServletRequest));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean validateVerificationCode(String code) {
        User user = onboardingPersistencePort.findUserByVerificationCode(code);

        if (user == null || user.getIsEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setIsEnabled(true);
            onboardingPersistencePort.saveUser(user);
            return true;
        }

    }

    @Override
    public User getUserByEmail(String userEmail) {
        return onboardingPersistencePort.getUserByEmail(userEmail);
    }

    private void sendEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = "sabyasachi.biswal1@gmail.com";
        String fromAddress = "sabyasachi.biswal1@gmail.com";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}
