package com.example.futurebank;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;

import uk.co.jakebreen.sendgridandroid.SendGrid;
import uk.co.jakebreen.sendgridandroid.SendGridMail;
import uk.co.jakebreen.sendgridandroid.SendGridResponse;
import uk.co.jakebreen.sendgridandroid.SendTask;

public class EmailSending {

    private final String key="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";
    public void sendEmail( String receiver, String subject, String body) {


        try {
            SendGrid sendGrid = SendGrid.create( key);

            SendGridMail mail = new SendGridMail();
            mail.addRecipient(receiver,"Name");
            mail.setFrom("futurebank0094@gmail.com", "Future Bank");
            mail.setSubject(  subject);
            mail.setContent(  body);

            SendTask task = new SendTask(sendGrid);
            SendGridResponse response = task.send(mail);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
