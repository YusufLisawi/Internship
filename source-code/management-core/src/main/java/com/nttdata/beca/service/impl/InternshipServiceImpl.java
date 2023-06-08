package com.nttdata.beca.service.impl;

import com.nttdata.beca.config.response.MessageResponse;
import com.nttdata.beca.config.services.EmailService;
import com.nttdata.beca.dto.InternshipDTO;
import com.nttdata.beca.entity.Docs;
import com.nttdata.beca.entity.Internship;
import com.nttdata.beca.repository.DocsRepository;
import com.nttdata.beca.repository.InternshipRepository;
import com.nttdata.beca.service.InternshipService;
import com.nttdata.beca.transformer.InternshipTransformer;
import com.nttdata.beca.transformer.Transformer;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class InternshipServiceImpl implements InternshipService {

    private static final Transformer<Internship, InternshipDTO> internshipTransformer = new InternshipTransformer();

    private InternshipRepository internshipRepository;

    private DocsRepository docsRepository;

    EmailService emailService;


    @Override
    public List<InternshipDTO> getAllInternships() {
        return internshipTransformer.toDTOList(internshipRepository.findAll());
    }

    @Override
    public List<InternshipDTO> getInternshipsBySessionId(Long sessionId) {
        return internshipTransformer.toDTOList(internshipRepository.getInternshipsBySessionId(sessionId));
    }

    @Override
    public List<InternshipDTO> getCurrentSessionInternships() {
        return internshipTransformer.toDTOList(internshipRepository.getCurrentSessionInternships());
    }

    @Override
    public InternshipDTO getInternshipsById(long internship_id) {
        return internshipTransformer.toDTO(internshipRepository.findById(internship_id));
    }

    @Override
    public InternshipDTO addInternship(InternshipDTO internshipDTO) throws Exception {
        List<InternshipDTO> internships = getCurrentSessionInternships()
                .stream().filter(dto -> dto.getCandidate().equals(internshipDTO.getCandidate())).filter(dto -> dto.isDeleted()==false).toList();
        if (internships.size()>0) {
            throw new Exception("This Candidate is already exists in the current session !!");
        } else {
            internshipDTO.setInternshipStatus("in progress");
            return internshipTransformer.toDTO(internshipRepository.save(internshipTransformer.toEntity(internshipDTO)));
        }
    }

    @Override
    public MessageResponse deleteInternshipById(long internship_id) {
        InternshipDTO internshipDTO = internshipTransformer.toDTO(internshipRepository.findById(internship_id));
        internshipDTO.setDeleted(true);
        internshipRepository.save(internshipTransformer.toEntity(internshipDTO));
        return new MessageResponse("Internship has been deleted successfully");
    }

    @Override
    public InternshipDTO updateInternship(InternshipDTO dto) {

        InternshipDTO internshipDTO = internshipTransformer.toDTO(internshipRepository.findById(dto.getInternshipId()));
        internshipDTO.setInternshipStatus(dto.getInternshipStatus());
        internshipDTO.setStartDate(dto.getStartDate());
        internshipDTO.setEndDate(dto.getEndDate());
        internshipDTO.setSubject(dto.getSubject());
        internshipDTO.setInternshipRating(dto.getInternshipRating());
        internshipDTO.setReportRating(dto.getReportRating());
        internshipDTO.setType(dto.getType());
        internshipDTO.setSupervisor(dto.getSupervisor());
        internshipDTO.setSupervisorPhone(dto.getSupervisorPhone());
        internshipDTO.setSupervisorEmail(dto.getSupervisorEmail());
        internshipDTO.setDeleted(dto.isDeleted());
        internshipDTO.setCandidate(dto.getCandidate());
        internshipDTO.setSession(dto.getSession());
        internshipDTO.setLastReportReminderSent(dto.isLastReportReminderSent());
        internshipDTO.setLastReportReminderDate(dto.getLastReportReminderDate());

        return internshipTransformer.toDTO(internshipRepository.save(internshipTransformer.toEntity(dto)));
    }

    @Scheduled(cron = "0 0 */12 * * *") // Runs every 12 hours
    public void sendInternshipEndDateEmail() throws MessagingException, TemplateException, IOException {
        Iterable<Internship> internships = internshipRepository.findAll();
        System.out.println("Checking email");
        for (Internship internship : internships) {
            Iterable<Docs> docs = docsRepository.findByInternship_InternshipId(internship.getInternshipId());
            boolean hasReport = false;
            for (Docs doc : docs) {
                if ("report".equalsIgnoreCase(doc.getType())) {
                    hasReport = true;
                    break;
                }
            }
            if (internship.getEndDate().isBefore(LocalDate.now()) && !hasReport) {
                LocalDate now = LocalDate.now();
                LocalDate lastReminderDate = internship.getLastReportReminderDate();
                if (lastReminderDate == null) {
                    emailService.sendEndOfInternshipEmail(internship);
                    internship.setLastReportReminderDate(now);
                    internshipRepository.save(internship);
                } else if (lastReminderDate.plusDays(2).isBefore(now)
                        && !internship.isLastReportReminderSent()) {
                    emailService.sendEndOfInternshipEmail(internship);
                    internship.setLastReportReminderDate(now);
                    internship.setLastReportReminderSent(true);
                    internshipRepository.save(internship);
                }
            }
        }
    }

}
