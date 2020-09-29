import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call(createRepoResponse) {

    // THIS ALL NEEDS TO BE CHANGED

    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()
        
    // UPDATE POM FILE
    SCM_REPO_URL = pipelinePlaceholders.getSshUrlToRepo().replaceAll("/", "\\\\/");
    SCM_CREDENTIALS_ID = Constants.GITLAB_CREDENTIALS_ID // DONT KNOW IF THIS IS STILL REQUIRED...
    
    sh "sed -i \'s/TEMPLATE_FQN_PREFIX/${Constants.FQN_PREFIX}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_ORGANIZATION/${ORGANIZATION_FORMATTED}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_ORGANIZATION/${ORGANIZATION_FORMATTED}/\' mule-project.xml"
    sh "sed -i \'s/TEMPLATE_SCM_CREDENTIALS_ID/${SCM_CREDENTIALS_ID}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_SCM_REPO/${SCM_REPO}/\' pom.xml"

    def encoded_snapshots_repo_url = "${Constants.NEXUS_SNAPSHOTS_REPO_URL}".replaceAll("/", "\\\\/")
    def encoded_releases_repo_url = "${Constants.NEXUS_RELEASES_REPO_URL}".replaceAll("/", "\\\\/")

    sh "sed -i \'s/TEMPLATE_NEXUS_SNAPSHOTS_REPO_ID/${Constants.NEXUS_SNAPSHOTS_REPO_ID}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_NEXUS_SNAPSHOTS_REPO_URL/${encoded_snapshots_repo_url}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_NEXUS_RELEASES_REPO_ID/${Constants.NEXUS_RELEASES_REPO_ID}/\' pom.xml"
    sh "sed -i \'s/TEMPLATE_NEXUS_RELEASES_REPO_URL/${encoded_releases_repo_url}/\' pom.xml"

    // UPDATE PROPERTY FILES
    pipelinePlaceholders.getPorts().each { env, port ->
        sh "sed -i \'s/TEMPLATE_PORT/${port}/\' src/main/resources/${env}-domain.properties"
    }

    // UPDATE JENKINS CONFIG FILE (WILL LATER BE REMOVED AFTER CREATING JENKINS BUILD JOB)
    sh "sed -i \'s/TEMPLATE_SCM_REPO/${SCM_REPO}/\' config.xml"
    sh "sed -i \'s/TEMPLATE_SCM_CREDENTIALS_ID/${SCM_CREDENTIALS_ID}/\' config.xml"

    sh "mkdir tmp"
    sh "mv config.xml tmp/"
}