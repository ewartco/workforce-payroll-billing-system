package model;

import java.util.concurrent.atomic.AtomicInteger;

public class Contractor {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(2000);

    private final int contractorId;
    private final String firstName;
    private final String lastName;
    private final String email;

    public Contractor(String firstName, String lastName, String email) {

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name is required.");
        }

        this.contractorId = ID_SEQUENCE.incrementAndGet();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getContractorId() {
        return contractorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return contractorId + " - " + getFullName();
    }
}