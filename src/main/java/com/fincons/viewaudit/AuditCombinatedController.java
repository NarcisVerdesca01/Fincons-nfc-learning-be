package com.fincons.viewaudit;

import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AuditCombinatedController {

    @Autowired
    private IAuditCombinatedService iAuditCombinatedService;


    @GetMapping("${audit-combinated.list}")
    public ResponseEntity<ApiResponse<List<AuditCombinated>>> getAllAuditCombinated(){
        List<AuditCombinated> auditCombinatedList= iAuditCombinatedService.findAllAuditCombinated();
        return ResponseEntity.ok().body(ApiResponse.<List<AuditCombinated>>builder()
                .data(auditCombinatedList)
                .build());
    }


    @GetMapping("${audit-combinated.auditcourse}")
    public ResponseEntity<ApiResponse<Long[]>> getAllAuditCombinatedCourseInformation(){
        Long[] auditCourse = iAuditCombinatedService.findAllAuditCombinatedCourseInformation();
        return ResponseEntity.ok().body(ApiResponse.<Long[]>builder()
                .data(auditCourse)
                .build());
    }

    @GetMapping("${audit-combinated.auditquiz}")
    public ResponseEntity<ApiResponse<Long[]>> getAllAuditCombinatedQuizInformation(){
        Long[] auditCourse = iAuditCombinatedService.findAllAuditCombinatedQuizInformation();
        return ResponseEntity.ok().body(ApiResponse.<Long[]>builder()
                .data(auditCourse)
                .build());
    }






}
