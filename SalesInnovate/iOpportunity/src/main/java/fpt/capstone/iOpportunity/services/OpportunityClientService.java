package fpt.capstone.iOpportunity.services;

import fpt.capstone.proto.lead.GetLeadRequest;
import fpt.capstone.proto.lead.GetLeadResponse;
import fpt.capstone.proto.lead.LeadDtoProto;
import fpt.capstone.proto.lead.LeadServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class OpportunityClientService {

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
