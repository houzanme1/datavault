package org.datavaultplatform.broker.controllers;

import java.util.List;
import org.datavaultplatform.common.model.FileInfo;
import org.datavaultplatform.broker.services.MacFilesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * User: Robin Taylor
 * Date: 01/05/2015
 * Time: 13:21
 */


@RestController
public class FilesController {
    
    private MacFilesService macFilesService;

    public void setMacFilesService(MacFilesService macFilesService) {
        this.macFilesService = macFilesService;
    }

    @RequestMapping("/files/**")
    public List<FileInfo> getFilesListing(@RequestHeader(value = "X-UserID", required = true) String userID,
                                          HttpServletRequest request) {
        
        // "GET /files/" will display files from the base directory.
        // "GET /files/abc" will display files from the "abc" directory under the base.
        
        // TODO: is there a cleaner way to extract the request path?
        String requestPath = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String filePath = requestPath.replaceFirst("^/files", "");
        
        return macFilesService.getFilesListing(filePath);
    }
}