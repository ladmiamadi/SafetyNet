package com.openclassrooms.safetynet.repository;

import com.openclassrooms.safetynet.repository.HelperRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelperRepositoryTest {

    @Test
    public void testCalculateAge() {
        long result = HelperRepository.calculateAge("07/18/1985");

        assertThat(result).isEqualTo(36);
    }
}
