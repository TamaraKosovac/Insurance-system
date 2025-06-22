package org.unibl.etf.sigurnost.insurancesystem.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;
import org.unibl.etf.sigurnost.insurancesystem.model.User;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    public byte[] generatePolicyPdf(Policy policy, User user, String transactionId) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();

            PdfWriter.getInstance(document, outputStream);
            document.open();

            Paragraph title = new Paragraph("Insurance Policy Confirmation");
            title.setSpacingAfter(15f);
            document.add(title);

            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

            document.add(new Paragraph("Policy Name: " + policy.getName()));
            document.add(new Paragraph("Client: " + user.getFirstname() + " " + user.getLastname()));
            document.add(new Paragraph("Email: " + user.getEmail()));
            document.add(new Paragraph("Amount: $" + policy.getAmount()));
            document.add(new Paragraph("Transaction ID: " + transactionId));
            document.add(new Paragraph("Payment Date & Time: " + formattedDateTime));

            Paragraph confirmation = new Paragraph("You have successfully paid for your insurance policy.");
            confirmation.setSpacingBefore(10f);
            document.add(confirmation);

            Paragraph thankYou = new Paragraph("Thank you for your purchase.");
            thankYou.setSpacingBefore(20f);
            document.add(thankYou);

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
