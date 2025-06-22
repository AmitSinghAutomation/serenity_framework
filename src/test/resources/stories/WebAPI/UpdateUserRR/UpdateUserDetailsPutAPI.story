Narrative:
This is the demo API story file which will have few API endpoint automated in order to check its desired response

Scenario: Validate the response of update user details endpoint using PUT request
!-- Author    : Amit Singh
!-- Module    : Update User
!-- Epic      : User Management
!-- Defect ID : No Defect
Meta:
@Tests
TC006-WebAPI-Request Response Service-"Update User"-OK-Validate the response of update user api endpoint using PUT request
@API
Given User prepares endpoint as <EndPoint> and request body using <RequestBody>
And User prepares endpoint <EndPoint> and writes value as <ValueToBeAddedInEndPoint>
And User has a request header for Content-Type as application/json
When User makes PUT request
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>

Examples:
|EndPoint   |RequestBodyFolderName|RequestBody              |ExpectedResponseCode |ExpectedResponseBody     |ResponseBodyFolderName |RequestBodyFilePath    |DataNotToCompare |ValueToBeAddedInEndPoint|IdToBeReplacedWithEndPoint|
|updateUser |UpdateUserAPI        |UpdateUserPutRequest.json|200                  |UpdateUserPutRequest.json|UpdateUserAPI          |apiRequestBody.filePath|updatedAt        |validUserID             |userID                    |

Scenario: Validate the response of update user details endpoint using PATCH request
!-- Author    : Amit Singh
!-- Module    : Update User
!-- Epic      : User Management
!-- Defect ID : No Defect
Meta:
@Tests
TC007-WebAPI-Request Response Service-"Update User"-OK-Validate the response of update user api endpoint using PATCH request
@API
Given User prepares endpoint as <EndPoint> and request body using <RequestBody>
And User prepares endpoint <EndPoint> and writes value as <ValueToBeAddedInEndPoint>
And User has a request header for Content-Type as application/json
When User makes PATCH request
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>

Examples:
|EndPoint   |RequestBodyFolderName|RequestBody              |ExpectedResponseCode |ExpectedResponseBody     |ResponseBodyFolderName |RequestBodyFilePath    |DataNotToCompare |ValueToBeAddedInEndPoint|IdToBeReplacedWithEndPoint|
|updateUser |UpdateUserAPI        |UpdateUserPutRequest.json|200                  |UpdateUserPutRequest.json|UpdateUserAPI          |apiRequestBody.filePath|updatedAt        |validUserID             |userID                    |
