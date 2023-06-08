package com.nttdata.beca.config.services;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.nttdata.beca.config.jwt.JwtUtils;
import com.nttdata.beca.dto.CandidateDTO;
import com.nttdata.beca.dto.InterviewDTO;
import com.nttdata.beca.dto.SessionDTO;
import com.nttdata.beca.entity.Candidate;
import com.nttdata.beca.entity.Internship;
import com.nttdata.beca.entity.Recruiter;
import com.nttdata.beca.service.CandidateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final Configuration configuration;
    private final JavaMailSender javaMailSender;
    private final JwtUtils jwtUtils;
    @Autowired
    private CandidateService candidateService;
    @Value("${frontend}")
    private String frontendUrl;
    private final static String field = "fullname";

    public void sendEmail(Recruiter recruiter) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Reset your password - NTT DATA");
        helper.setTo(recruiter.getEmail());

        String token = jwtUtils.generateToken(recruiter);
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put(field, recruiter.getFirstName() + " " + recruiter.getLastName());
        model.put("email", recruiter.getEmail());
        model.put("url", frontendUrl + "reset-password/" +  token);
        configuration.getTemplate("reset-password.ftl").process(model, stringWriter);
        String emailContent = stringWriter.getBuffer().toString();
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendEmailToAcceptedCandidate(List<String> emails) throws IOException, MessagingException, TemplateException {
            for (String email : emails) {
                List<CandidateDTO> candidateDTOList = candidateService.findByEmail(email);
                if(!candidateDTOList.isEmpty()){
                    for(CandidateDTO candidateDTO:candidateDTOList){
                        SessionDTO sessionDTO = candidateDTO.getSession();
                        if (sessionDTO.getSessionStatus().equals("current") && candidateDTO.getInterview()!=null) {
                            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                            helper.setSubject("NTT DATA");
                            helper.setTo(email);
                            String emailContent = generateEmailContent(candidateDTO);
                            byte[] pdfBytes = generatePdfAttachment(candidateDTO);
                            helper.setText(emailContent, true);
                            helper.addAttachment("Lettre de convocation entretien d'embauche.pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));
                            javaMailSender.send(mimeMessage);
                        }
                    }
                }
            }
    }

    private String generateEmailContent(CandidateDTO candidateDTO) throws IOException, TemplateException {
        Template template = configuration.getTemplate("candidate.ftl");
        Map<String, Object> model = new HashMap<>();
        model.put(field, candidateDTO.getFirstName() + " " + candidateDTO.getLastName());
        model.put("date", new SimpleDateFormat("dd MMMM yyyy").format(candidateDTO.getInterview().getDate()));
        model.put("time", candidateDTO.getTimeOfInterview());
        StringWriter stringWriter = new StringWriter();
        template.process(model, stringWriter);
        return stringWriter.toString();
    }

    private byte[] generatePdfAttachment(CandidateDTO candidateDTO) throws IOException, TemplateException {
        Template convocation = configuration.getTemplate("convocation.ftl");
        InterviewDTO interviewDTO = candidateDTO.getInterview();
        SessionDTO sessionDTO = candidateDTO.getSession();
        Date interviewDate = interviewDTO.getDate();
        Map<String, Object> model = new HashMap<>();
        model.put(field, candidateDTO.getFirstName() + " " + candidateDTO.getLastName());
        model.put("date", new SimpleDateFormat("dd MMMM yyyy").format(interviewDate));
        model.put("time", candidateDTO.getTimeOfInterview());
        model.put("DateNow",new SimpleDateFormat("dd MMMM yyyy").format(new Date()));
        model.put("recruiter",sessionDTO.getRecruiter().getFirstName()+ " " +sessionDTO.getRecruiter().getLastName());
        model.put("jobTitle","développeur "+sessionDTO.getTechnology());
        StringWriter stringWriterConv = new StringWriter();
        convocation.process(model,stringWriterConv);
        String convocationContent = stringWriterConv.toString();
        String htmlContent = new String(convocationContent.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(htmlContent, pdf, converterProperties);
        pdf.close();
        return baos.toByteArray();
    }

    public void sendEmailToAdmittedCandidate(List<String> emails) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("NTT DATA");
        helper.setTo(emails.stream().toArray(String[]::new));

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        configuration.getTemplate("admittedCandidate.ftl").process(model, stringWriter);
        String emailContent = stringWriter.getBuffer().toString();
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendEndOfInternshipEmail(Internship internship) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        Candidate candidate = internship.getCandidate();
        helper.setSubject("End of Internship - NTT DATA");
        helper.setTo(candidate.getEmail());

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put(field, candidate.getFirstName() + " " + candidate.getLastName());
        model.put("email", candidate.getEmail());
        configuration.getTemplate("internshipEndDate.ftl").process(model, stringWriter);
        String emailContent = stringWriter.getBuffer().toString();
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

}