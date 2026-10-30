package com.iesce.maintenance.service;

import com.iesce.maintenance.dao.MaintenanceRequestDAO;
import com.iesce.maintenance.model.MaintenanceRequest;
import com.iesce.maintenance.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestService {

    private final MaintenanceRequestDAO requestDAO = new MaintenanceRequestDAO();
    private final AuthenticationService authService = new AuthenticationService();

    public int submitRequest(User faculty, String location, String category, String description) {
        if (!authService.isFaculty(faculty)) {
            throw new SecurityException("Only faculty can submit maintenance requests.");
        }

        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Room/location is required.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required.");
        }

        MaintenanceRequest request = new MaintenanceRequest();
        request.setFacultyId(faculty.getId());
        request.setLocation(location.trim());
        request.setCategory(category.trim());
        request.setDescription(description.trim());
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        return requestDAO.create(request);
    }

    public List<MaintenanceRequest> getFacultyRequests(User faculty) {
        if (!authService.isFaculty(faculty)) {
            throw new SecurityException("Only faculty can view their own requests.");
        }
        return requestDAO.findByFaculty(faculty.getId());
    }

    public List<MaintenanceRequest> getAllRequests(User hod) {
        if (!authService.isHOD(hod)) {
            throw new SecurityException("Only HOD can view all requests.");
        }
        return requestDAO.findAll();
    }

    public void approveRequest(User hod, int requestId, String remarks) {
        if (!authService.isHOD(hod)) {
            throw new SecurityException("Only HOD can approve requests.");
        }
        validateTransition(requestId, "PENDING", "APPROVED");
        requestDAO.updateStatusAndRemarks(requestId, "APPROVED", remarks);
    }

    public void rejectRequest(User hod, int requestId, String remarks) {
        if (!authService.isHOD(hod)) {
            throw new SecurityException("Only HOD can reject requests.");
        }
        validateTransition(requestId, "PENDING", "REJECTED");
        requestDAO.updateStatusAndRemarks(requestId, "REJECTED", remarks);
    }

    public void completeRequest(User hod, int requestId, String remarks) {
        if (!authService.isHOD(hod)) {
            throw new SecurityException("Only HOD can mark requests as completed.");
        }
        validateTransition(requestId, "APPROVED", "COMPLETED");
        requestDAO.updateStatusAndRemarks(requestId, "COMPLETED", remarks);
    }

    public void updateRemarks(User hod, int requestId, String remarks) {
        if (!authService.isHOD(hod)) {
            throw new SecurityException("Only HOD can update remarks.");
        }
        requestDAO.updateRemarks(requestId, remarks);
    }

    private void validateTransition(int requestId, String expectedFrom, String expectedTo) {
        java.util.List<MaintenanceRequest> all = requestDAO.findAll();
        MaintenanceRequest req = all.stream()
                .filter(r -> r.getId() == requestId)
                .findFirst()
                .orElse(null);
        if (req == null) {
            throw new IllegalArgumentException("Request #" + requestId + " not found.");
        }
        if (!expectedFrom.equalsIgnoreCase(req.getStatus())) {
            throw new IllegalArgumentException(
                    "Cannot transition request #" + requestId + " from '" + req.getStatus() + "' to '" + expectedTo + "'.");
        }
    }
}
