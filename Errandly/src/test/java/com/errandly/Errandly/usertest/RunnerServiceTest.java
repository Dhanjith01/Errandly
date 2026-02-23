package com.errandly.Errandly.usertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.errandly.Errandly.exception.custom.RunnerNotFoundException;
import com.errandly.Errandly.user.entity.AvailabilityStatus;
import com.errandly.Errandly.user.entity.Runner;
import com.errandly.Errandly.user.repository.RunnerRepository;
import com.errandly.Errandly.user.service.RunnerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RunnerServiceTest {
    @Mock
    private RunnerRepository runnerRepository;

    @InjectMocks
    private RunnerServiceImpl runnerService;

    private Runner runner;

    @BeforeEach
    void setUp() {
        runner = Runner.builder()
                       .availabilityStatus(AvailabilityStatus.UNAVAILABLE)
                       .id(1L)
                       .build();
    }

    @Test
    void enableRunner_shouldSetAvailabilityToAvailable_whenRunnerExists() {
        when(runnerRepository.findById(1L)).thenReturn(Optional.of(runner));

        runnerService.enableRunner(1L);

        assertEquals(AvailabilityStatus.AVAILABLE, runner.getAvailabilityStatus());
        verify(runnerRepository).save(runner);
    }

    @Test
    void disableRunner_shouldSetAvailabilityToUnavailable_whenRunnerExists() {
        runner.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        when(runnerRepository.findById(1L)).thenReturn(Optional.of(runner));

        runnerService.disableRunner(1L);

        assertEquals(AvailabilityStatus.UNAVAILABLE, runner.getAvailabilityStatus());
        verify(runnerRepository).save(runner);
    }

    @Test
    void enableRunner_shouldThrowException_whenRunnerDoesNotExist() {
        when(runnerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RunnerNotFoundException.class,
                () -> runnerService.enableRunner(1L));

        verify(runnerRepository, never()).save(any());
    }

    @Test
    void disableRunner_shouldThrowException_whenRunnerDoesNotExist() {
        when(runnerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RunnerNotFoundException.class,
                () -> runnerService.disableRunner(1L));

        verify(runnerRepository, never()).save(any());
    }

}
