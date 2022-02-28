package com.messanger.engine.uc.service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.context.IoSessionContext;
import com.messanger.engine.uc.dao.ICommonDao;
import com.messanger.engine.uc.model.MessageTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;

public class SendMailService {

    protected static final Log LOG = LogFactory.getLog(SendMailService.class);

    private Config config;
    private ICommonDao commonDao;
    private String adminEmail;
    private String smtpHost;
    private String smtpPort;
    private String smtpPWD;
    private Session session;

    IoSessionContext ctx = IoSessionContext.getInstance();
    private TemplateLoader loader = new ClassPathTemplateLoader("/com/messanger/mail/templates", ".html");
    private Handlebars handlebars = new Handlebars(loader);

    public void setCommonDao(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }

    public void setConfig(Config config) {
        this.config = config;
        this.smtpHost = config.get("mail.smtp.host");
        this.smtpPort = config.get("mail.smtp.port");
        this.smtpPWD = config.get("mail.admin.pwd");
        this.adminEmail = config.get("mail.admin.email");
    }

    public void init() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", smtpHost);
        prop.put("mail.smtp.port", smtpPort);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        this.session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, smtpPWD);
            }
        });
    }

    public void send(String toMail, String subject, String content) {
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(adminEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getReceiver(toMail)));
            message.setSubject(subject);
            message.setText(content, "text/html;charset=UTF-8");
            Transport.send(message);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void send(String toMail, String mailTemplateId, Map<String, String> content) {
        try {
            MessageTemplate messageTemplate = commonDao.selectOneByTemplateIdAndEMail(mailTemplateId);
            Template template = handlebars.compile(messageTemplate.getTemplateName());
            String contentString = template.apply(content);
            this.send(toMail, messageTemplate.getTitle(), contentString);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String getReceiver(String toMail) {

        String[] receiverArray = StringUtils.split(toMail, Constants.PROP_DELIM);
        List<String> receiverList = new ArrayList<>(receiverArray.length);

        for(String receiver : receiverArray) {
            if(receiver.indexOf("@@") > 0) {
                receiverList.addAll(commonDao.selectAllGroupEmailByDept(receiver));
                continue;
            }

            receiverList.add(receiver);
        }

        return String.join(";", receiverList);
    }
}
