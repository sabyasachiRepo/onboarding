package com.apartment.onboarding;


import com.apartment.onboarding.registration.bean.student.Student;
import com.apartment.onboarding.registration.repo.StudentRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.HeaderTokenizer;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@Validated
public class StudentOnboardingController {

    @Autowired
    private StudentRepository studentRepository;


    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/register-student")
    public ResponseEntity studentRegistration(@Valid @RequestBody Student student,HttpServletRequest httpServletRequest){
        String randomCode = RandomString.make(64);
        student.setVerificationCode(randomCode);
        student.setIsEnabled(false);
        studentRepository.save(student);

        try{
            sendEmail(student,getSiteURL(httpServletRequest));
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    private void sendEmail(Student student,String siteURL) throws MessagingException, UnsupportedEncodingException {
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

        content = content.replace("[[name]]", student.getFirstName() +" "+student.getLastName());
        String verifyURL = siteURL + "/verify?code=" + student.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public boolean verify(String verificationCode) {
        Student student = studentRepository.findByVerificationCode(verificationCode);

        if (student == null || student.getIsEnabled()) {
            return false;
        } else {
            student.setVerificationCode(null);
            student.setIsEnabled(true);
            studentRepository.save(student);
            return true;
        }

    }
}
