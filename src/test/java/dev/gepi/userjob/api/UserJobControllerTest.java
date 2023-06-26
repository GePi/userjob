package dev.gepi.userjob.api;


import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.Users;
import dev.gepi.userjob.services.UserJobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserJobControllerTest {

    @Mock
    private UserJobService userJobService;

    @InjectMocks
    private UserJobController userJobController;

    @Test
    public void testPostUserJobRequest() {
// Arrange
        UserJobRequestDTO userJobRequestDTO = new UserJobRequestDTO();
        UserJobRequestDTO.UserJobInfo userJobInfo = new UserJobRequestDTO.UserJobInfo();
        userJobInfo.setUserId(1L);
        userJobInfo.setIdCompany(2L);
        userJobRequestDTO.setUserJobInfo(userJobInfo);

        Company company = new Company();
        company.setId(2L);
        Users user = new Users();
        user.setId(1L);
        when(userJobService.getUserById(1L)).thenReturn(user);
        when(userJobService.getCompanyById(2L)).thenReturn(company);

        // Act
        userJobController.postUserJob(userJobRequestDTO);

        // Assert
        verify(userJobService, times(1)).save(company, user);
    }
}
