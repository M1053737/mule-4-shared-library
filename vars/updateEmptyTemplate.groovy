import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call(createRepoResponse) {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()

    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()
    API_NAME_FORMATTED = pipelinePlaceholders.getApiAssetIdFormatted()
    FULL_API_NAME_FORMATTED = ORGANIZATION_FORMATTED + "-" + API_NAME_FORMATTED
    
    // UPDATE MULE PROJECT FILES
    sh "sed -i \'s/TEMPLATE_API_NAME/${API_NAME_FORMATTED}/\' src/main/resources/log4j2.xml"

    sh "mv src/main/mule/TEMPLATE_API_NAME.xml src/main/mule/${API_NAME_FORMATTED}.xml"
    
    // UPDATE JENKINSFILE
    sh "sed -i \'s/TEMPLATE_API_ASSET_ID/${pipelinePlaceholders.getApiAssetId()}/\' Jenkinsfile"
    sh "sed -i \'s/TEMPLATE_ORGANIZATION/${pipelinePlaceholders.getOrganization()}/\' Jenkinsfile"

    // UPDATE JENKINSDEPLOYFILE
    sh "sed -i \'s/TEMPLATE_API_ASSET_ID/${pipelinePlaceholders.getApiAssetId()}/\' Jenkinsdeployfile"
    sh "sed -i \'s/TEMPLATE_ORGANIZATION/${pipelinePlaceholders.getOrganization()}/\' Jenkinsdeployfile"

    // UPDATE POM FILE
    SCM_REPO_URL = pipelinePlaceholders.getSshUrlToRepo().replaceAll("/", "\\\\/");
    SCM_CREDENTIALS_ID = Constants.GITLAB_CREDENTIALS_ID // DONT KNOW IF THIS IS STILL REQUIRED...
    
    sh "sed -i \'s/TEMPLATE_FQN_PREFIX/${Constants.FQN_PREFIX}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_ORGANIZATION_FORMATTED/${ORGANIZATION_FORMATTED}/\' pom.xml"
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