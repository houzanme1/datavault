package org.datavaultplatform.webapp.controllers;

import org.datavaultplatform.common.model.Dataset;
import org.datavaultplatform.common.model.FileStore;
import org.datavaultplatform.common.model.Group;
import org.datavaultplatform.common.model.RetentionPolicy;
import org.datavaultplatform.common.request.CreateVault;
import org.datavaultplatform.common.response.VaultInfo;
import org.datavaultplatform.webapp.services.RestService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User: Tom Higgins
 * Date: 27/05/2015
 * Time: 14:44
 */

@Controller
//@RequestMapping("/vaults")
public class VaultsController {

    private RestService restService;

    public void setRestService(RestService restService) {
        this.restService = restService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getVaultsListing(ModelMap model) {
        // Decide what page the user should see, is it the first time they have logged on?
        FileStore[] userStores = restService.getFileStoreListing();
        if (userStores.length == 0) {
            return "users/storage";
        } else {
            model.addAttribute("vaults", restService.getVaultsListing());
            return "vaults/index";
        }

        //String username;
        //Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //if (principal instanceof UserDetails) {
         ///   username = ((UserDetails)principal).getUsername();
        //} else {
        //    username = principal.toString();
        //}

        //if (!restService.keysExist(username)) {
        //    model.addAttribute("publicKey", restService.addKeys(username));
        //    model.addAttribute("userID", username);
        //    return "users/storage";
        //}


    }

    @RequestMapping(value = "/vaults/{vaultid}", method = RequestMethod.GET)
    public String getVault(ModelMap model, @PathVariable("vaultid") String vaultID) {
        VaultInfo vault = restService.getVault(vaultID);

        model.addAttribute("vault", vault);

        model.addAttribute(restService.getRetentionPolicy(vault.getPolicyID()));
        model.addAttribute(restService.getGroup(vault.getGroupID()));
        model.addAttribute("deposits", restService.getDepositsListing(vaultID));

        return "vaults/vault";
    }

    // Return an empty 'create new vault' page
    @RequestMapping(value = "/vaults/create", method = RequestMethod.GET)
    public String createVault(ModelMap model) {

        // pass the view an empty Vault since the form expects it
        model.addAttribute("vault", new CreateVault());

        Dataset[] datasets = restService.getDatasets();
        model.addAttribute("datasets", datasets);
        
        RetentionPolicy[] policies = restService.getRetentionPolicyListing();
        model.addAttribute("policies", policies);

        Group[] groups = restService.getGroups();
        model.addAttribute("groups", groups);

        return "vaults/create";
    }

    // Process the completed 'create new vault' page
    @RequestMapping(value = "/vaults/create", method = RequestMethod.POST)
    public String addVault(@ModelAttribute CreateVault vault, ModelMap model, @RequestParam String action) {
        // Was the cancel button pressed?
        if ("cancel".equals(action)) {
            return "redirect:/";
        }

        VaultInfo newVault = restService.addVault(vault);
        String vaultUrl = "/vaults/" + newVault.getID() + "/";
        return "redirect:" + vaultUrl;        
    }
}


