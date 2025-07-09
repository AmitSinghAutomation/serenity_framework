# SERENITY FRAMEWORK 
Currently, API Framework cover below mentioned request:
GET
POST
PUT
PATCH
DELETE

Command for executing based on Story File only:
clean install -DexeEnvironment=QA -s settings.xml -DstoryName=UpdateUserDetailsPutAPI.story

Command for executing based on Multiple Story Files:
clean install -DexeEnvironment=QA -s settings.xml -DstoryName=UpdateUserDetailsPutAPI.story;GetUserDetailsAPI.story;CreateUserDataAPI.story

Command for executing a specific scenario in a story file using specific tag:
clean install -DexeEnvironment=QA -s settings.xml -DstoryName=UpdateUserDetailsPutAPI.story -Dmetafilter=+API1

Command for executing based on tag only no story file name:
clean install -DexeEnvironment=QA -s settings.xml -Dmetafilter=+API1

Command for executing bases on multiple tag with no story file name:
clean install -DexeEnvironment=QA -s settings.xml -Dmetafilter=+API1,+API2

This framework also include table concept where we provide test data separately in a table
And passing its path to the respective story file

This framework also include the creation of test cycle, test folder, adding testcases, execution and update testcases status
with zephyr end point

This framework also include fetching the token from okta authentication

Framework also include the creation of Jira Defect. It also parses the name of CI Job from GitHub Work Flow.
Also handle the resolution for the browser.

Command for mentioning the resolution at the run time is given below
clean install -DexeEnvironment=QA -s settings.xml -DstoryName=UpdateUserDetailsPutAPI.story -Dmetafilter=+UI -Dwebdriver.driver="chrome" -Dresolution="1024,768"

This framework also handle runtime required response data saving for further use

Implementation of WebUI POC using chrome browser using different resolution
clean install -DexeEnvironment=QA-Smoke -s settings.xml -DstoryName=WebPoc.story -Dwebdriver.driver=chrome -Dresolution="1024,768"