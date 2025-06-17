package com.iris.notification.utils;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sendgrid.helpers.mail.objects.Email;

public class MailParams {
    
    private final static String SENDER_NAME_SECTION = "sender-name";
    private final static String SENDER_EMAIL_SECTION = "sender-email";
    private final static String REPLYTO_EMAIL_SECTION = "replyto-email";
    private final static String SUBJECT_SECTION = "subject";
    private final static String PLAINTEXT_BODY_SECTION = "plaintext-body";
    private final static String HTML_BODY_SECTION = "html-body";
    
    @Inject(optional = true) @Named("email.filter.domain") private String defaultEmailFilterDomain;
    @Inject @Named("email.sendername") 	private String defaultSenderName;
    @Inject @Named("email.senderemail") 	private String defaultSenderEmail;
    @Inject @Named("email.replyto") 	private String defaultReplyToEmail;
    @Inject @Named("email.subject") 	private String defaultSubject;

    private String recipientName;
    private String emailFilterDomain = defaultEmailFilterDomain;
    private String senderName = defaultSenderName;
    private String senderEmail = defaultSenderEmail;
    private String replyToEmail = defaultReplyToEmail;
    private String subject = defaultSubject;
    private String plaintextBody;
    private String htmlBody;
    private Email toEmail;
    private Email fromEmail;

    public MailParams() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReplyToEmail() {
        return replyToEmail;
    }

    public void setReplyToEmail(String replyToEmail) {
        this.replyToEmail = replyToEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlaintextBody() {
        return plaintextBody;
    }

    public void setPlaintextBody(String plaintextBody) {
        this.plaintextBody = plaintextBody;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public Email getToEmail() {
        return toEmail;
    }

    public void setToEmail(Email toEmail) {
        this.toEmail = toEmail;
    }

    public Email getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(Email fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getEmailFilterDomain() { return emailFilterDomain; }

    public void setEmailFilterDomain(String emailFilterDomain) { this.emailFilterDomain = emailFilterDomain; }

    public static MailParams fromMap(Map<String, String> messageParts) {
        
        MailParams mailParams = new MailParams();

        if (messageParts.containsKey(SENDER_NAME_SECTION)) {
            mailParams.setSenderName(messageParts.get(SENDER_NAME_SECTION));
        }

        if (messageParts.containsKey(SENDER_EMAIL_SECTION)) {
            mailParams.setSenderEmail(messageParts.get(SENDER_EMAIL_SECTION));
        }

        if (messageParts.containsKey(REPLYTO_EMAIL_SECTION)) {
            mailParams.setReplyToEmail(messageParts.get(REPLYTO_EMAIL_SECTION));
        }

        if (messageParts.containsKey(SUBJECT_SECTION)) {
            mailParams.setSubject(messageParts.get(SUBJECT_SECTION));
        }

        mailParams.setPlaintextBody(messageParts.containsKey(PLAINTEXT_BODY_SECTION) ? messageParts.get(PLAINTEXT_BODY_SECTION) : messageParts.get(""));
        mailParams.setHtmlBody(messageParts.containsKey(HTML_BODY_SECTION) ? messageParts.get(HTML_BODY_SECTION) : mailParams.getPlaintextBody());

        return mailParams;
    }
}

