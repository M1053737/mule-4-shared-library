import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()

    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()
    API_NAME_FORMATTED = pipelinePlaceholders.getApiAssetIdFormatted()
	//RUNTIME_VERSION = pipelinePlaceholders.getMuleRuntime()
    GROUP_ID = Constants.POM_GROUPID_PREFIX
    FOLDER_NAME=pipelinePlaceholders.getEnvironment()
    API_AUTODISCOVERYID= pipelinePlaceholders.getApiAutoDiscoveryId()
    //sh "mv pom.cloudhub.xml pom.xml" 
   echo API_AUTODISCOVERYID

    // UPDATE MULE PROJECT FILES
    sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' src/main/resources/log4j2.xml"
	sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' src/main/mule/global.xml"
	sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' src/main/resources/api/api.raml"
	sh "mv src/main/mule/entity src/main/mule/${API_NAME_FORMATTED}-impl"
	sh "mv src/main/mule/api.xml src/main/mule/${API_NAME_FORMATTED}.xml"
	//sh "mv src/main/resources/api/api.raml src/main/resources/api/${API_NAME_FORMATTED}.raml"
    sh "mv src/main/mule/${API_NAME_FORMATTED}-impl/TEMPLATE_API_NAME.xml src/main/mule/${API_NAME_FORMATTED}-impl/${API_NAME_FORMATTED}-impl.xml"
    
	//Rename Properties
	sh "mv src/main/resources/properties/TEMPLATE_API_NAME-test.yaml src/main/resources/properties/${API_NAME_FORMATTED}-test.yaml"
	sh "mv src/main/resources/properties/TEMPLATE_API_NAME-int.yaml src/main/resources/properties/${API_NAME_FORMATTED}-int.yaml"
	sh "mv src/main/resources/properties/TEMPLATE_API_NAME-prod.yaml src/main/resources/properties/${API_NAME_FORMATTED}-prod.yaml"
	
    // UPDATE JENKINSFILE
    sh "sed -i \'s/TEMPLATE_API_AUTODISCOVERY_ID/${pipelinePlaceholders.getApiAutoDiscoveryId()}/\' Jenkinsfile"
    //sh "sed -i \'s/TEMPLATE_API_ASSET_ID/${pipelinePlaceholders.getApiAssetId()}/\' Jenkinsfile"
    //sh "sed -i \'s/TEMPLATE_ORGANIZATION/${pipelinePlaceholders.getOrganization()}/\' Jenkinsfile"
	//sh "sed -i \'s/RUNTIME_VERSION/${pipelinePlaceholders.getMuleRuntime()}/\' Jenkinsfile"
	//if(FOLDER_NAME.contains("CS-Region")) {
	//sh "sed -i \'s/WH-CRM-TEST/WH-CRM-CS-TEST/\' Jenkinsfile"
	//}
    

    // UPDATE POM FILE
    SCM_REPO_URL = pipelinePlaceholders.getSshUrlToRepo().replaceAll("/", "\\\\/");
    SCM_CREDENTIALS_ID = Constants.GH_CREDENTIALS_ID 
    
    sh "sed -i \'s/TEMPLATE_GROUP_ID/${GROUP_ID}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_ORGANIZATION/${pipelinePlaceholders.getOrganization()}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' pom.xml"
   
   

    // UPDATE JENKINS BUILD CONFIG FILE
    sh "sed -i \'s/TEMPLATE_SCM_REPO/${SCM_REPO_URL}/\' buildConfig-test.xml"
    sh "sed -i \'s/TEMPLATE_SCM_CREDENTIALS_ID/${SCM_CREDENTIALS_ID}/\' buildConfig-test.xml"
	
	//sh "sed -i \'s/TEMPLATE_SCM_REPO/${SCM_REPO_URL}/\' buildConfig-int.xml"
    //sh "sed -i \'s/TEMPLATE_SCM_CREDENTIALS_ID/${SCM_CREDENTIALS_ID}/\' buildConfig-int.xml"

	//sh "sed -i \'s/TEMPLATE_SCM_REPO/${SCM_REPO_URL}/\' buildConfig-prod.xml"
   // sh "sed -i \'s/TEMPLATE_SCM_CREDENTIALS_ID/${SCM_CREDENTIALS_ID}/\' buildConfig-prod.xml"
    

}