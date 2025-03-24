Narrative:
This is the demo API story file which will have few API endpoint automated in order to check its desired response

Scenario: Validate the response of create user api endpoint
Meta:
@Tests
TC005-WebAPI-Request Response Service-"Create user"-OK-Validate the response of create user api endpoint
@API
Given User prepares endpoint as <EndPoint> and request body using <RequestBody>
And User has a request header for Content-Type as application/json
When User makes POST request
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>

Examples:
|EndPoint   |RequestBodyFolderName|RequestBody           |ExpectedResponseCode |ExpectedResponseBody        |ResponseBodyFolderName |RequestBodyFilePath    |DataNotToCompare |
|createUser |CreateUserAPI        |CreateUserRequest.json|201                  |CreateUserResponse.json     |CreateUserAPI          |apiRequestBody.filePath|createdAt,id     |
