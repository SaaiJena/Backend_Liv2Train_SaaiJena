package com.liv2train.liv2train.dto;

import com.liv2train.liv2train.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public class TrainingCenterRequest {

    @NotBlank @Size(max = 40)
    private String centerName;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{12}$")
    private String centerCode;

    @Valid @NotNull
    private Address address;

    @PositiveOrZero
    private Integer studentCapacity;

    private List<@NotBlank String> coursesOffered = new ArrayList<>();

    @Email
    private String contactEmail;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String contactPhone;

    // getters/setters
    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }
    public String getCenterCode() { return centerCode; }
    public void setCenterCode(String centerCode) { this.centerCode = centerCode; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public Integer getStudentCapacity() { return studentCapacity; }
    public void setStudentCapacity(Integer studentCapacity) { this.studentCapacity = studentCapacity; }
    public List<String> getCoursesOffered() { return coursesOffered; }
    public void setCoursesOffered(List<String> coursesOffered) { this.coursesOffered = coursesOffered; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
}
