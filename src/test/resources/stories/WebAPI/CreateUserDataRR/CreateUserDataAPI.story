Narrative:
This is the demo API story file which will have few API endpoint automated in order to check its desired response

Scenario: Validate the response of create user api endpoint
Meta:
@Tests
TC005-WebAPI-Request Response Service-"Create User"-OK-Validate the response of create user api endpoint
@API
Given User prepares endpoint as <EndPoint> and request body using <RequestBody>
And User has a request header for Content-Type as application/json
When User makes POST request
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>
And User store the parameter from response for <Key> as <Value>
!-- Deleting the user created above by passing the same id fetched above step
Given User prepares endpoint <DeleteEndPoint> and writes value as <ValueToBeAddedInEndPoint>
And User has a request header for Content-Type as application/json
When User makes DELETE request
Then Response should have a response code as <ExpectedResponseCodeForDelete>

Examples:
|EndPoint   |RequestBodyFolderName|RequestBody           |ExpectedResponseCode |ExpectedResponseBody        |ResponseBodyFolderName |RequestBodyFilePath    |DataNotToCompare |Key|Value         |DeleteEndPoint   |ValueToBeAddedInEndPoint|IdToBeReplacedWithEndPoint|ExpectedResponseCodeForDelete|
|createUser |CreateUserAPI        |CreateUserRequest.json|201                  |CreateUserResponse.json     |CreateUserAPI          |apiRequestBody.filePath|createdAt,id     |id |userIdToDelete|deleteUser       |userIdToDelete          |userID                    |204                          |
