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