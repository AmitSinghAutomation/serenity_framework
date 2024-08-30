Narrative:
This is the demo API story file which will have few API endpoint automated in order to check its desired response

Scenario: Validate the response of a particular user by providing its id
Meta:
@Tests
TC001-WebAPI-Request Response Service-"Get user detail"-OK-Validate the user detail desired response providing user ID

@API
Given User prepares endpoint <EndPoint> and writes value as <ValueToBeAddedInEndPoint>
When User makes GET request with empty token and empty header
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>

Examples:
|EndPoint   |ValueToBeAddedInEndPoint|IdToBeReplacedWithEndPoint|ExpectedResponseCode |ExpectedResponseBody |ResponseBodyFolderName |DataNotToCompare |
|userDetails|validUserID             |userID                    |200                  |GetUserDetails.json  |GetUserDetailsAPI      |url              |
