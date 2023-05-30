package com.example.EmployeeRegistration.controller
import com.example.EmployeeRegistration.dto.EmployeeDTO
import com.example.EmployeeRegistration.dto.EmployeeUpdateDTO
import com.example.EmployeeRegistration.service.EmployeeServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import spock.lang.Specification
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureWebMvc
@WebMvcTest(controllers = [EmployeeController])
class EmployeeControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @MockBean
    private EmployeeServiceImpl employeeService

    def "should create an employee"() {
        given:

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        Integer employeeId=1
        EmployeeDTO employeeDTO = new EmployeeDTO(id: 1,name: "john",aadhar: "2234 5678 9012" ,age: 18,department: "cse",city: "varanasi",dob: LocalDate.parse("05/10/2000", dateFormatter))
        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        def employeeJson = objectMapper.writeValueAsString(employeeDTO)
        employeeService.addEmployee(employeeJson) >> employeeId


        expect:
        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isCreated())
                .andReturn()
                .response
    }

    def "should get all employees"() {
        given:

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        List<EmployeeDTO> employeeList = [new EmployeeDTO(id: 1,name: "john",aadhar: "2234 5678 9010",age: 18,department: "cse",dob: LocalDate.parse("05/10/2000", dateFormatter)), new EmployeeDTO(id: 2,name: "john",aadhar: "2234 5678 9019",age: 18,department: "cse",dob: LocalDate.parse("05/10/2000", dateFormatter))]
        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        def employeeJson = objectMapper.writeValueAsString(employeeList)
        when(employeeService.getAllEmployees()).thenReturn(employeeList)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().response

    }

    def "should get employee details by id"() {
        given:
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        Integer employeeId=1
        def employeeDTO = new EmployeeDTO(id: 1,name: "john",aadhar: "2234 5678 9012" ,age: 18,department: "cse",city: "varanasi",dob: LocalDate.parse("05/10/2000", dateFormatter))
        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        employeeService.getEmployee(employeeId) >> employeeDTO
        def employeeJson = objectMapper.writeValueAsString(employeeDTO)



        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/employee/{employeeId}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().response

    }

    def "should get employee details by aadhar"() {
        given:
        String aadhar = "2234 5678 9012"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        EmployeeDTO employeeDTO = new EmployeeDTO(id: 1,name: "john",aadhar: "2234 5678 9012" ,age: 18,department: "cse",city: "varanasi",dob: LocalDate.parse("05/10/2000", dateFormatter))
        def objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        employeeService.getEmployee(aadhar) >> employeeDTO
        def employeeJson = objectMapper.writeValueAsString(employeeDTO)


        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/employee/{employeeAadhar}",aadhar)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().response
    }

    def "should update an employee"() {
        given:
        String aadhar = "2234 5678 9012"
        EmployeeUpdateDTO employeeUpdateDTO=new EmployeeUpdateDTO(department: "IT")
        def objectMapper = new ObjectMapper()
        def employeeJson = objectMapper.writeValueAsString(employeeUpdateDTO)

        when:
        employeeService.updateEmployee(aadhar,employeeUpdateDTO.department)

        then:
        mockMvc.perform(MockMvcRequestBuilders.put("/employee/{employeeAadhar}", aadhar, employeeUpdateDTO.department)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andExpect(content().string('{"message":"UPDATE_SUCCESS","status":200}'))


    }
}
