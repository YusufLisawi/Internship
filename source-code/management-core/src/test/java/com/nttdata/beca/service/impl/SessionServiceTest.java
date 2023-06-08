package com.nttdata.beca.service.impl;

import com.nttdata.beca.dto.SessionDTO;
import com.nttdata.beca.entity.Session;
import com.nttdata.beca.repository.SessionRepository;
import com.nttdata.beca.service.impl.SessionServiceImpl;
import com.nttdata.beca.transformer.SessionTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;
    @InjectMocks
    private SessionServiceImpl sessionService;

    @InjectMocks
    private SessionTransformer sessionTransformer;

    private  Session session;
    private SessionDTO sessionDTO;
    private long sessionId=1l;
    private String sessionName="session1";
    private String sessionStatus="current";

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        sessionDTO = SessionDTO.builder()
                .sessionId(1L)
                .sessionName("session1")
                .sessionDate(new Date(22,12,2022))
                .sessionStatus("current")
                .dayInterviewNumber(3)
                .admittedNumber(10)
                .technology("java")

                .build();
        session= sessionTransformer.toEntity(sessionDTO);
    }

    @Test
    public void save(){
        when(sessionRepository.findBySessionName(anyString())).thenReturn(null);
        Mockito.when(sessionRepository.save(session)).thenReturn(session);

        SessionDTO SaveSession = sessionService.save(sessionDTO);
        SessionDTO findByStatus = sessionService.findSessionByStatus(sessionDTO.getSessionStatus());

        assertThat(SaveSession).isNotNull();
    }
    @DisplayName("Testing findAll Method")
    @Test
    public void FindAll(){
        when(sessionRepository.findAll()).thenReturn(List.of(session));
        List<SessionDTO> AllSession=sessionService.findAll();
        assertThat(AllSession).isNotNull();
        assertThat(AllSession.size()).isEqualTo(1);
    }
    @DisplayName("Testing findSessionById Method")
    @Test
    public void FindSessionById_Test(){
        when(sessionRepository.findBySessionId(anyLong())).thenReturn(session);
        SessionDTO sessionF = sessionService.findSessionById(sessionId);
        assertThat(sessionF).isNotNull();
        assertThat(sessionF.getSessionName()).isEqualTo("session1");
    }
    @DisplayName("Testing findSessionByName Method")
    @Test
    public void FindSessionByName_Test(){
        when(sessionRepository.findBySessionName(anyString())).thenReturn(session);
        SessionDTO sessionF = sessionService.findSessionByName(sessionName);
        assertThat(sessionF).isNotNull();
        assertThat(sessionF.getSessionDate()).isEqualTo(session.getSessionDate());
    }
    @DisplayName("Testing findSessionByStatus Method")
    @Test
    public void FindSessionByStatus_Test(){
        when(sessionRepository.findBySessionStatus(anyString())).thenReturn(session);
        SessionDTO sessionF = sessionService.findSessionByStatus(sessionStatus);
        assertThat(sessionF).isNotNull();
        assertThat(sessionF.getSessionName()).isEqualTo(session.getSessionName());
        assertThat(sessionF.getSessionDate()).isEqualTo(session.getSessionDate());
    }
    @DisplayName("Testing getLatestSessionId Method")
    @Test
    public void Get_LatestSessionId_Test(){
        when(sessionRepository.getLatestSessionId()).thenReturn(1l);
        long sessionId = sessionService.getLatestSessionId();
        assertThat(sessionId).isNotNull();
        assertThat(sessionId).isEqualTo(1l);
    }
    @DisplayName("Testing delete Method")
    @Test
    public void Delete_Test(){
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        sessionService.delete(sessionDTO);
    }
}
