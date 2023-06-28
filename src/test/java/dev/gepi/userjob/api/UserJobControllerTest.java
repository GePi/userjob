package dev.gepi.userjob.api;


import dev.gepi.userjob.api.dto.CompanyWithUsersDTO;
import dev.gepi.userjob.api.dto.UserJobDTO;
import dev.gepi.userjob.api.dto.UserWithCompaniesDTO;
import dev.gepi.userjob.model.Company;
import dev.gepi.userjob.model.UserJobInfo;
import dev.gepi.userjob.model.Users;
import dev.gepi.userjob.services.UserJobService;
import dev.gepi.userjob.utils.ModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserJobControllerTest {

    @Mock
    private UserJobService userJobService;

    @InjectMocks
    private UserJobController userJobController;

    @Test
    public void testGetUserJob_ReturnUserWithCompanies() {
        Users user = new Users(1L, "Ivanov", "Ivanovich", "Ivan", LocalDate.now(), "М", null, "Description", null, null, false, null);
        List<Company> companies = List.of(
                new Company(1L, "Company1", "CompanyDescr1", null, null, true, null),
                new Company(2L, "Company2", null, null, null, true, null));
        doReturn(user).when(this.userJobService).getUserById(1L);
        doReturn(companies).when(this.userJobService).getCompaniesByUserId(1L);

        UserWithCompaniesDTO returnBody = new UserWithCompaniesDTO();
        returnBody.setUser(ModelMapper.toDto(user));
        returnBody.setCompanies(companies.stream().map(ModelMapper::toDto).collect(Collectors.toList()));

        UserJobDTO.Users usersParam = new UserJobDTO.Users();
        usersParam.setUserId(1L);
        UserJobDTO.Company companyParam = new UserJobDTO.Company();
        var responseEntity = this.userJobController.getUserJob(usersParam, companyParam);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(returnBody, responseEntity.getBody());
    }

    @Test
    public void testGetUserJob_ReturnCompanyWithUsers() {
        Company company = new Company(1L, "Company1", "CompanyDescr1", null, null, true, null);
        List<Users> users = List.of(
                new Users(1L, "Ivanova", "Alina", "Sergeevna", LocalDate.now(), "F", null, "Description", null, null, false, null),
                new Users(2L, "Petrov", "Petrovich", "Petr", LocalDate.now(), "М", null, "Description", null, null, false, null));

        doReturn(company).when(this.userJobService).getCompanyById(1L);
        doReturn(users).when(this.userJobService).getUsersByCompanyId(1L);

        CompanyWithUsersDTO returnBody = new CompanyWithUsersDTO();
        returnBody.setCompany(ModelMapper.toDto(company));
        returnBody.setUsers(users.stream().map(ModelMapper::toDto).collect(Collectors.toList()));

        UserJobDTO.Users usersParam = new UserJobDTO.Users();
        UserJobDTO.Company companyParam = new UserJobDTO.Company();
        companyParam.setIdCompany(1L);
        var responseEntity = this.userJobController.getUserJob(usersParam, companyParam);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(returnBody, responseEntity.getBody());
    }

    @Test
    public void testGetUserJob_ResponseNotFound1() {
        UserJobDTO.Users usersParam = new UserJobDTO.Users();
        UserJobDTO.Company companyParam = new UserJobDTO.Company();

        var responseEntity = this.userJobController.getUserJob(usersParam, companyParam);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @Test
    public void testGetUserJob_ResponseNotFound2() {
        doReturn(null).when(this.userJobService).getCompanyById(1L);

        UserJobDTO.Users usersParam = new UserJobDTO.Users();
        UserJobDTO.Company companyParam = new UserJobDTO.Company();
        companyParam.setIdCompany(1L);

        var responseEntity = this.userJobController.getUserJob(usersParam, companyParam);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @Test
    public void testGetUserJob_ResponseNotFound3() {
        doReturn(null).when(this.userJobService).getUserById(1L);

        UserJobDTO.Users usersParam = new UserJobDTO.Users();
        UserJobDTO.Company companyParam = new UserJobDTO.Company();
        usersParam.setUserId(1L);

        var responseEntity = this.userJobController.getUserJob(usersParam, companyParam);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @Test
    public void testCreateUserJob_ReturnOk() {
        UserJobDTO userJobDTO = createSampleUserJobDto();

        Company company = new Company();
        Users user = new Users();
        UserJobInfo userJobInfo = new UserJobInfo();
        ModelMapper.toModel(userJobDTO, user, company, userJobInfo);

        OffsetDateTime createdAt = OffsetDateTime.now();
        ModelMapper.setCreated(createdAt, user, company, userJobInfo);
        user.addUserJobInfo(userJobInfo);
        company.addUserJobInfo(userJobInfo);

        try (MockedStatic<OffsetDateTime> theMock = Mockito.mockStatic(OffsetDateTime.class)) {
            theMock.when(OffsetDateTime::now).thenReturn(createdAt);
            var responseEntity = userJobController.createUserJob(userJobDTO);
            verify(userJobService, times(1)).save(company, user);

            assertNotNull(responseEntity);
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        }
    }

    @Test
    public void testUpdateUserJob_ReturnOk() throws IllegalAccessException {
        UserJobDTO userJobDTO = createSampleUserJobDto();
        userJobDTO.getUsers().setUserId(1L);
        userJobDTO.getCompany().setIdCompany(1L);

        Company prevCompany = new Company();
        Users prevUser = new Users();
        UserJobInfo prevUserJobInfo = new UserJobInfo();
        ModelMapper.toModel(userJobDTO, prevUser, prevCompany, prevUserJobInfo);
        prevUser.setId(1L);
        prevCompany.setId(1L);
        prevUserJobInfo.setId(1L);
        prevUser.addUserJobInfo(prevUserJobInfo);
        prevCompany.addUserJobInfo(prevUserJobInfo);

        Company updatedCompany = new Company();
        Users updatedUser = new Users();
        UserJobInfo updatedUserJobInfo = new UserJobInfo();
        ModelMapper.toModel(userJobDTO, updatedUser, updatedCompany, updatedUserJobInfo);
        updatedUser.setId(1L);
        updatedCompany.setId(1L);
        updatedUserJobInfo.setId(1L);
        updatedUser.addUserJobInfo(updatedUserJobInfo);
        updatedCompany.addUserJobInfo(updatedUserJobInfo);

        doReturn(prevCompany).when(this.userJobService).getCompanyById(1L);
        doReturn(prevUser).when(this.userJobService).getUserById(1L);

        userJobDTO.getUsers().setFamilyName("Karnauhova");
        userJobDTO.getCompany().setIsActivity(true);
        userJobDTO.getUserJobInfo().setDescription("Changed");

        OffsetDateTime updatedAt = OffsetDateTime.now();
        ModelMapper.setUpdated(updatedAt, updatedUser, updatedCompany, updatedUserJobInfo);
        updatedUser.setFamilyName("Karnauhova");
        updatedCompany.setIsActivity(true);
        updatedUserJobInfo.setDescription("Changed");

        List<String> returnBody = new ArrayList<>(List.of("Users.familyName", "Company.isActivity", "UserJobInfo.description"));

        try (MockedStatic<OffsetDateTime> theMock = Mockito.mockStatic(OffsetDateTime.class)) {
            theMock.when(OffsetDateTime::now).thenReturn(updatedAt);
            var responseEntity = userJobController.updateUserJob(userJobDTO);
            verify(userJobService, times(1)).save(updatedCompany, updatedUser);

            assertNotNull(responseEntity);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
            assertEquals(returnBody, responseEntity.getBody());
        }
    }

    private UserJobDTO createSampleUserJobDto() {
        UserJobDTO userJobDTO = UserJobDTO.createInitial();
        userJobDTO.getCompany().setCompanyName("Company 1");
        userJobDTO.getCompany().setIsActivity(false);
        userJobDTO.getUsers().setFirstName("Arina");
        userJobDTO.getUsers().setBirthday(LocalDate.of(1980, 3, 9));
        userJobDTO.getUserJobInfo().setDescription("SEO");
        userJobDTO.getUserJobInfo().setIsActivity(true);
        return userJobDTO;
    }
}
