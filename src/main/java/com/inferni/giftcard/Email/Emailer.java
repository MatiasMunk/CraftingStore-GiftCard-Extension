package com.inferni.giftcard.Email;

import com.inferni.giftcard.APIHook.Hook;
import com.inferni.giftcard.APIHook.Manager;
import com.inferni.giftcard.GiftCard;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.bukkit.Bukkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Emailer {
    private static Emailer instance;

    public Emailer() {
        instance = this;
    }

    public static Emailer getInstance() {
        if (instance == null) {
            instance = new Emailer();
        }
        return instance;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        String host = GiftCard.config.getString("Host");
        int port = GiftCard.config.getInt("Port");
        String user = GiftCard.config.getString("Username");
        String pass = GiftCard.config.getString("Password");
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(user);
        mailSender.setPassword(pass);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.debug", "false");

        return mailSender;
    }
    @Autowired
    private JavaMailSender emailSender = getJavaMailSender();



    public void sendEmail(String CardCode, String player,
                          String targetEmail)
            throws IOException, TemplateException, MessagingException {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("cardCode", Hook.getCardCode(CardCode));
        model.put("player", player);
        Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_0);
        //From template hopefully
        MimeMessage msg = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        freemarkerConfig.setDirectoryForTemplateLoading(new File(GiftCard.getInstance().getDataFolder().getPath() + "/templates"));
        Template t = freemarkerConfig.getTemplate("email-template.ftl");
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        helper.setTo(targetEmail);
        helper.setText(text, true);
        helper.setSubject(GiftCard.config.getString("EmailSubject"));
        helper.setFrom(GiftCard.config.getString("EmailIdentity") + "<" + GiftCard.config.getString("Username") + ">");

        //Send Message Async
        Bukkit.getScheduler().scheduleAsyncDelayedTask(GiftCard.getInstance(), new Runnable(){
            @Override
            public void run() {
                System.out.println("Email Starting to send. Code: " + CardCode);
                emailSender.send(msg);
                System.out.println("Email Sent. Code: " + CardCode);
            }
        }, 1L);
    }

}
