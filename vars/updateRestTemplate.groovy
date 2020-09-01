import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()

    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()
    API_NAME_FORMATTED = pipelinePlaceholders.getApiAssetIdFormatted()
    FULL_API_NAME_FORMATTED = ORGANIZATION_FORMATTED + "-" + API_NAME_FORMATTED

    if(pipelinePlaceholders.getDeploymentType() != "onprem-standalone" || pipelinePlaceholders.getDeploymentType() != "onprem-domain" || pipelinePlaceholders.getDeploymentType() != "onprem-docker") {
        sh "rm src/main/mule/consul.dockerProject.xml"
        sh "rm src/main/mule/consul.standaloneProject.xml"
    }

   // if(pipelinePlaceholders.getDeploymentType() != "onprem-docker") {
       // sh "rm Dockerfile"
   // }

    if(pipelinePlaceholders.getDeploymentType() == "rtf") {
        GROUP_ID = pipelinePlaceholders.getApStructure().organizations[pipelinePlaceholders.getOrganization()].id
        sh "rm pom.cloudhub.xml"
        sh "mv pom.rtf.xml pom.xml"
        sh "rm Jenkinsfile.cloudhub"
        sh "rm Jenkinsfile.onprem-docker"
        sh "mv Jenkinsfile.rtf Jenkinsfile"
    }
    else if(pipelinePlaceholders.getDeploymentType() == "cloudhub") {
        GROUP_ID = Constants.FQN_PREFIX + "." + pipelinePlaceholders.getOrganizationFormatted()
        sh "rm pom.rtf.xml"
        sh "mv pom.cloudhub.xml pom.xml"
        sh "rm Jenkinsfile.rtf"
        sh "rm Jenkinsfile.onprem-docker"
        sh "mv Jenkinsfile.cloudhub Jenkinsfile"
    }

    // UPDATE MULE PROJECT FILES
    sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' src/main/resources/log4j2.xml"
    sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' src/main/mule/TEMPLATE_API_NAME.xml"
    sh "mv src/main/mule/TEMPLATE_API_NAME.xml src/main/mule/${API_NAME_FORMATTED}.xml"
    sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' src/main/mule/globals.xml"

    // UPDATE JENKINSFILE
    sh "sed -i \'s/TEMPLATE_API_ASSET_ID/${pipelinePlaceholders.getApiAssetId()}/\' Jenkinsfile"
    sh "sed -i \'s/TEMPLATE_ORGANIZATION/${pipelinePlaceholders.getOrganization()}/\' Jenkinsfile"

    // UPDATE JENKINSDEPLOYFILE
    //sh "sed -i \'s/TEMPLATE_API_ASSET_ID/${pipelinePlaceholders.getApiAssetId()}/\' Jenkinsdeployfile"
   // sh "sed -i \'s/TEMPLATE_ORGANIZATION/${pipelinePlaceholders.getOrganization()}/\' Jenkinsdeployfile"

    // UPDATE POM FILE
   // SCM_REPO_URL = pipelinePlaceholders.getSshUrlToRepo().replaceAll("/", "\\\\/");
   // SCM_REPO_URL = pipelinePlaceholders.getHttpUrlToRepo().replaceAll("/", "\\\\/");
  //  SCM_CREDENTIALS_ID = Constants.GITLAB_CREDENTIALS_ID // DONT KNOW IF THIS IS STILL REQUIRED...
    
    sh "sed -i \'s/TEMPLATE_GROUP_ID/${GROUP_ID}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_ORGANIZATION/${pipelinePlaceholders.getOrganization()}/\' pom.xml"
    sh "sed -i \'s#TEMPLATE_DOMAIN_DEPENDENCY#${pipelinePlaceholders.getDomainDependency()}#\' pom.xml"
    sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_SCM_CREDENTIALS_ID/${SCM_CREDENTIALS_ID}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_SCM_REPO/${SCM_REPO_URL}/\' pom.xml"
    
    def encoded_snapshots_repo_url = "${Constants.NEXUS_SNAPSHOTS_REPO_URL}".replaceAll("/", "\\\\/")
    def encoded_releases_repo_url = "${Constants.NEXUS_RELEASES_REPO_URL}".replaceAll("/", "\\\\/")

    sh "sed -i \'s/TEMPLATE_NEXUS_SNAPSHOTS_REPO_ID/${Constants.NEXUS_SNAPSHOTS_REPO_ID}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_NEXUS_SNAPSHOTS_REPO_URL/${encoded_snapshots_repo_url}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_NEXUS_RELEASES_REPO_ID/${Constants.NEXUS_RELEASES_REPO_ID}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_NEXUS_RELEASES_REPO_URL/${encoded_releases_repo_url}/\' pom.xml"

    // UPDATE JENKINS BUILD CONFIG FILE
    sh "sed -i \'s/TEMPLATE_SCM_REPO/${SCM_REPO_URL}/\' buildConfig.xml"
    sh "sed -i \'s/TEMPLATE_SCM_CREDENTIALS_ID/${SCM_CREDENTIALS_ID}/\' buildConfig.xml"

    // UPDATE JENKINS DEPLOY CONFIG FILE
    sh "sed -i \'s/TEMPLATE_SCM_REPO/${SCM_REPO_URL}/\' deployConfig.xml"
    sh "sed -i \'s/TEMPLATE_SCM_CREDENTIALS_ID/${SCM_CREDENTIALS_ID}/\' deployConfig.xml"

}
