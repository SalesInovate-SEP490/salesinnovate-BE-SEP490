package fpt.capstone.iOpportunity.controllers;

import fpt.capstone.iOpportunity.dto.request.OpportunityDTO;
import fpt.capstone.iOpportunity.dto.request.OpportunityPriceBookProductDTO;
import fpt.capstone.iOpportunity.dto.response.ResponseData;
import fpt.capstone.iOpportunity.dto.response.ResponseError;
import fpt.capstone.iOpportunity.services.OpportunityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/opportunity")
public class OpportunityController {

    @Autowired
    private final OpportunityService opportunityService;


    @PostMapping("/create-opportunity")
    public ResponseData<?> createOpportunity(
            @RequestBody OpportunityDTO opportunityDTO
    ) {
        try{
            long opportunity = opportunityService.createOpportunity(opportunityDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Create Opportunity Success",opportunity, 1);
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

    }


    @DeleteMapping("/delete/{id}")
    public ResponseData<?> deleteOpportunity(
            @PathVariable(name = "id") Long id
    ) {
        try{
            opportunityService.deleteOpportunity(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Delete opportunity success", 1);
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    public ResponseData<?> detailOpportunity(
            @PathVariable(name = "id") Long id
    ) {
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(), opportunityService.getDetailOpportunity(id));
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/get-list-forecast-category")
    public ResponseData<?> getListForecastCategory() {
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(), opportunityService.getListForecastCategory());
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "Get list forecast error");
        }
    }

    @GetMapping("/get-list-stage")
    public ResponseData<?> getListStage() {
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(), opportunityService.getListStage());
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "Get list stage error");
        }
    }

    @GetMapping("/get-list-type")
    public ResponseData<?> getListType() {
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(), opportunityService.getListType());
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "Get list type error");
        }
    }

    @GetMapping("/get-list-leadsource")
    public ResponseData<?> getListLeadSource() {
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(), opportunityService.getLeadSource());
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "Get list lead source error");
        }
    }

    @GetMapping("/get-list-opportunity")
    public ResponseData<?> getListOpportunity(
            @RequestParam(value = "currentPage", defaultValue = "0") int pageNo,
            @RequestParam(value = "perPage", defaultValue = "10") int pageSize
    ) {
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.getListOpportunity( pageNo, pageSize));
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "Get list opportunity failed");
        }
    }

    @GetMapping("/list-opportunity-by-account")
    public ResponseData<?> getListOpportunityByAccount( @RequestParam(value = "currentPage", defaultValue = "0") int pageNo,
                                                        @RequestParam(value = "perPage", defaultValue = "10") int pageSize,
                                                        @RequestParam long accountId){
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.getListOpportunityByAccount(pageNo,pageSize,accountId));
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "Get list opportunity failed");
        }
    }

    @GetMapping("/list-opportunity-by-contact")
    public ResponseData<?> getListOpportunityByContact( @RequestParam(value = "currentPage", defaultValue = "0") int pageNo,
                                                        @RequestParam(value = "perPage", defaultValue = "10") int pageSize,
                                                        @RequestParam long accountId){
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.getListOpportunityByAccount(pageNo,pageSize,accountId));
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "Get list opportunity failed");
        }
    }

    @PostMapping("/convert-new-from-lead")
    public ResponseData<?> convertNewFromLead(@RequestParam String opportunityName,@RequestParam long leadId,
                                              @RequestParam long accountId,@RequestParam long contactId){
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.convertNewOpportunity(opportunityName,leadId,accountId,contactId));
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "convert new opportunity failed");
        }
    }

    @PostMapping("/convert-exist-from-lead")
    public ResponseData<?> convertExistFromLead(@RequestParam long contactId,@RequestParam long opportunityId){
        try{
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.convertExistOpportunity(contactId,opportunityId));
        }catch (Exception e){
            return new ResponseError(0,  HttpStatus.BAD_REQUEST.value(), "Convert opportunity failed");
        }
    }

    @PatchMapping("/patch-opportunity/{id}")
    public ResponseData<?> patchOpportunity(@RequestBody OpportunityDTO opportunityDTO, @PathVariable(name = "id") long id) {
        return opportunityService.patchOpportunity(opportunityDTO, id) ?
                new ResponseData<>(HttpStatus.OK.value(), "Update Opportunity success", 1)
                : new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update Opportunity fail");
    }

    @PatchMapping("/patch-list-opportunity")
    public ResponseData<?> patchListOpportunity(@RequestParam Long[] id, @RequestBody OpportunityDTO opportunityDTO) {
        return opportunityService.patchListOpportunity(id, opportunityDTO) ?
                new ResponseData<>(1, HttpStatus.OK.value(), "Update Opportunity success") :
                new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "Update Opportunity fail");
    }

    @GetMapping("/opportunity-filter")
    public ResponseData<?> filterOpportunity(Pageable pageable,
                                            @RequestParam(required = false) String[] search) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.filterOpportunity(pageable, search));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "list filter Opportunity fail");
        }
    }

    @PostMapping("/add-pricebook")
    public ResponseData<?> addPricebook(@RequestParam long opportunityId,@RequestParam long pricebookId) {
        return opportunityService.addPricebook(opportunityId, pricebookId) ?
                new ResponseData<>(1, HttpStatus.OK.value(), "add Pricebook success") :
                new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "add Pricebook fail");
    }

    @GetMapping("/count-product")
    public ResponseData<?> countProduct(@RequestParam long opportunityId) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.countProduct(opportunityId));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "can not count products");
        }
    }

    @PostMapping("/add-product")
    public ResponseData<?> addProductToOpportunity(@RequestBody OpportunityPriceBookProductDTO dto) {
        return opportunityService.addProductToOpportunity(dto) ?
                new ResponseData<>(1, HttpStatus.OK.value(), "add Product to Opportunity success") :
                new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "add Product to Opportunity fail");
    }

    @GetMapping("/get-product")
    public ResponseData<?> getProduct(@RequestParam long opportunityId) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.getProduct(opportunityId));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "can not get products");
        }
    }

    @GetMapping("/search-pricebook")
    public ResponseData<?> searchPriceBookToAdd(@RequestParam long opportunityId,@RequestParam(defaultValue = "", required = false) String search) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.searchPriceBookToAdd(opportunityId,search));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "can search pricebook");
        }
    }

    @GetMapping("/search-product")
    public ResponseData<?> searchProductToAdd(@RequestParam long opportunityId,
                                              @RequestParam(defaultValue = "", required = false) String search) {
        try {
            return new ResponseData<>(1, HttpStatus.OK.value(),
                    opportunityService.searchProductToAdd(opportunityId,search));
        } catch (Exception e) {
            return new ResponseError(0, HttpStatus.BAD_REQUEST.value(), "can search pricebook");
        }
    }

}
