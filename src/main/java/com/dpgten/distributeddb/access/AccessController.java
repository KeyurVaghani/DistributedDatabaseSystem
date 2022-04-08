package com.dpgten.distributeddb.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/access")
public class AccessController {


    @Autowired
    CommonRestClientUtil commonRestClientUtil;

//    @PostMapping("/user/details")
//    @Consumes(value = MediaType.APPLICATION_JSON)
//    @Produces(value = MediaType.APPLICATION_JSON)
//    public @ResponseBody
//    ShiftDetailsResponse inputShiftDetails(@RequestBody ShiftDetailsRequest shiftDetailsRequest) {
//        return schedulerService.saveShiftDetails(shiftDetailsRequest);
//    }
}
