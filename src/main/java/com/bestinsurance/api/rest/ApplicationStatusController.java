package com.bestinsurance.api.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class ApplicationStatusController {
    @Value("${api.version}")
    private String apiVersion;

    @GetMapping
    public Status getStatus() {
        return new Status(this.apiVersion, "running");
    }

    class Status {
        private String apiVersion;
        private String status;

        public Status(String apiVersion, String status) {
            this.apiVersion = apiVersion;
            this.status = status;
        }

        public String getApiVersion() {
            return apiVersion;
        }

        public String getStatus() {
            return status;
        }
    }
}
