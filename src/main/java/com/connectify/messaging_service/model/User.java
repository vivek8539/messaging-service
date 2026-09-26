package com.connectify.messaging_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String userId;
    private String userName;
    private String userEmail;
    private String mobileNo;
}
