Narrative:
This is the demo API story file which will have few API endpoint automated in order to check its desired response

Scenario: Validate the response of a particular user by providing its id without token and header
Meta:
@Tests
TC001-WebAPI-Request Response Service-"Get User Detail"-OK-Validate the user detail desired response providing user ID without token and header
TC002-WebAPI-Request Response Service-"Get User Detail"-NOK-Validate the user detail desired response providing invalid user ID without token and header
@API
Given User prepares endpoint <EndPoint> and writes value as <ValueToBeAddedInEndPoint>
When User makes GET request with empty token and empty header
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>

Examples:
|EndPoint   |ValueToBeAddedInEndPoint|IdToBeReplacedWithEndPoint|ExpectedResponseCode |ExpectedResponseBody        |ResponseBodyFolderName |DataNotToCompare |
|userDetails|validUserID             |userID                    |200                  |GetUserDetails.json         |GetUserDetailsAPI      |url              |
|userDetails|invalidUserID           |userID                    |404                  |GetInvalidUserDetails.json  |GetUserDetailsAPI      |url              |

Scenario: Validate the response of a particular user by providing its id without token and with header
Meta:
@Tests
TC003-WebAPI-Request Response Service-"Get User Detail"-OK-Validate the user detail desired response providing user ID with header without token
TC004-WebAPI-Request Response Service-"Get User Detail"-NOK-Validate the user detail desired response providing invalid user ID with header without token
@API
Given User prepares endpoint <EndPoint> and writes value as <ValueToBeAddedInEndPoint>
Given User has a request header for Content-Type as application/json
Given User has a request header for <HeaderKey> as <HeaderValue>
When User makes GET request with header and empty token
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>

Examples:
|EndPoint   |HeaderKey|HeaderValue|ValueToBeAddedInEndPoint|IdToBeReplacedWithEndPoint|ExpectedResponseCode |ExpectedResponseBody        |ResponseBodyFolderName |DataNotToCompare |
|userDetails|x-api-key|tokenValue |validUserID             |userID                    |200                  |GetUserDetails.json         |GetUserDetailsAPI      |url              |
|userDetails|x-api-key|tokenValue |invalidUserID           |userID                    |404                  |GetInvalidUserDetails.json  |GetUserDetailsAPI      |url              |

Scenario: Validate the response of a particular user by providing its id without token and header using table data
Meta:
@Tests
TC008-WebAPI-Request Response Service-"Get User Detail"-OK-Validate the user detail desired response providing user ID without token and header
TC009-WebAPI-Request Response Service-"Get User Detail"-NOK-Validate the user detail desired response providing invalid user ID without token and header
@API1
Given User prepares endpoint <EndPoint> and writes value as <ValueToBeAddedInEndPoint>
Given User has a request header for <HeaderKey> as <HeaderValue>
When User makes GET request with empty token and empty header
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>

Examples:
tables/GetUserDetailsAPI.table

Scenario: Validate the response of a particular user by providing its id without token and with header using table data
Meta:
@Tests
TC010-WebAPI-Request Response Service-"Get User Detail"-OK-Validate the user detail desired response providing user ID with header without token
TC011-WebAPI-Request Response Service-"Get User Detail"-NOK-Validate the user detail desired response providing invalid user ID with header without token
@API1 @Action
Given User prepares endpoint <EndPoint> and writes value as <ValueToBeAddedInEndPoint>
Given User has a request header for Content-Type as application/json
Given User has a request header for <HeaderKey> as <HeaderValue>
When User makes GET request with header and empty token
Then Response should have a response code as <ExpectedResponseCode>
And Response should have a response body as <ExpectedResponseBody>

Examples:
tables/GetUserDetailsAPI.table
