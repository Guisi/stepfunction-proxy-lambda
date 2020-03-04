package com.rumolog.lambda.stepfunctionproxy;

import java.io.ByteArrayOutputStream;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;

public class StepFunctionProxyRequestHandler implements RequestHandler<SNSEvent, Object> {

	public ByteArrayOutputStream handleRequest(SNSEvent request, Context context) {
		
		try {
			String paramsStr = request.getRecords().get(0).getSNS().getMessage();

			context.getLogger().log("Recebeu request: " + paramsStr);
			
			final AWSStepFunctions stepFunctionsClient = AWSStepFunctionsClientBuilder.standard().withRegion(Regions.fromName("sa-east-1")).build();
			stepFunctionsClient.startExecution(new StartExecutionRequest().withStateMachineArn("arn:aws:states:sa-east-1:060877748249:stateMachine:MyStateMachine")
					.withInput(paramsStr));
			
			context.getLogger().log("Iniciou step function!");
			
		} catch (Exception e) {
			context.getLogger().log(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
}