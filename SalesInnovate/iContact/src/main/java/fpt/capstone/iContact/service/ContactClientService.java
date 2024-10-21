package fpt.capstone.iContact.service;

import com.google.protobuf.Descriptors;
import fpt.capstone.iContact.dto.request.AddressInformationDTO;
import fpt.capstone.proto.lead.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ContactClientService {

    @GrpcClient("iLead")
    LeadServiceGrpc.LeadServiceBlockingStub stub ;

    public LeadDtoProto getLead (Long leadId){
        GetLeadRequest request = GetLeadRequest.newBuilder()
                .setLeadId(leadId)
                .build();
        GetLeadResponse response = stub.getLead(request);
        return response.getResponse();
    }


}
